import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import com.xunlei.cm.process.ShellUtilsEnhace;
import com.xunlei.cm.process.ShellUtilsEnhace.ExecuteResult;

public class TestFuture {
	
	private static final int NCPU = Runtime.getRuntime().availableProcessors();
	
	//private static final int NCPU = 1;
	
	private static final ExecutorService ex = Executors.newFixedThreadPool(NCPU);
	
	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		System.out.println(check());
		System.out.println(System.currentTimeMillis()-t1);
		ex.shutdown();
	}
	
	
	private static List<String> getUrls() {
		String[] cmds =  new String[]{"grep","-Eo","http://.*(float|biz5).*.(flv|swf|jpg|png|html)","cpm.xml"};
		Set<String> results = new HashSet<String>();
 		ExecuteResult result = null;
		try {
			result = ShellUtilsEnhace.executeShell(cmds, null);
			results.addAll(result.getOutputList());
		} catch ( Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<String>(results);
	}
	
	
	public static boolean check() {
		final AtomicBoolean valid = new AtomicBoolean(true);
		final AtomicInteger completeTask = new AtomicInteger();
		final List<String> urls = getUrls();
		System.out.println(urls.size());
		class CheckTask implements Runnable {
			int startIndex;
			int length;

			public CheckTask(int startIndex, int length) {
				this.startIndex = startIndex;
				this.length = length;
			}

			@Override
			public void run() {
System.out.println(Thread.currentThread().getName());
System.out.println(startIndex);
				for (int i = startIndex ,j=0; j < length && i < urls.size(); i++) {
					boolean v = isValid(urls.get(i));
					if (!v) {
						valid.set(false);
						return;
					}
					if (Thread.interrupted()) {
						System.out.println("中断");
						return;
					}
				}
				completeTask.incrementAndGet();
			}

		}
		List<Future<Void>> futures = new ArrayList<Future<Void>>();
		int pagerecord = urls.size()%NCPU == 0 ?  urls.size()/NCPU : urls.size()/NCPU+1;
		for (int i = 0; i < NCPU ; i++) {
			ex.submit(new CheckTask(i*pagerecord, pagerecord));
		}
		while( valid.get() == true &&  completeTask.get() < NCPU )  { //改进
			
		}
		if ( valid.get() == true ) { //检查全部通过 返回true
			return true ;
		}
		for( Future<Void> f : futures ) {
			if ( !f.isDone() ) {
				f.cancel(true);
			}
		}
		return false;
	}
	
	public static boolean isValid(String s) {//检查该地址对应的素材是否已经同步到素材服务器
		URL url = null ;
		URLConnection connection = null;
		try {
			url = new URL(s);
			connection = url.openConnection();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if ( connection instanceof HttpURLConnection ) {
			HttpURLConnection httpURLConnection = (HttpURLConnection)connection;
			try {
				return httpURLConnection.getResponseCode() == 200;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
