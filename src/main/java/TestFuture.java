import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFuture {
	
	private static final ExecutorService ex = Executors.newFixedThreadPool(4);
	
	public static void main(String[] args) {
		
	}
	
	public static boolean check() {
		AtomicBoolean valid = new AtomicBoolean(true);
		AtomicInteger completeTask = new AtomicInteger();
		final List<String> urls = new ArrayList<String>();//TODO 从配置文件中解析出所有的素材url,检查这些url是否已经同步到外网
		class CheckTask implements Runnable {
			int startIndex;
			int length;

			public CheckTask(int startIndex, int length) {
				super();
				this.startIndex = startIndex;
				this.length = length;
			}

			@Override
			public void run() {
				for (int i = startIndex ,j=0; j < length && i < urls.size(); i++) {
					boolean v = checkUrl(urls.get(i));
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

			private boolean checkUrl(String string) {// TODO 检查该url对应的素材是否已经同步到外网
				return false;
			}

		}
		List<Future<Void>> futures = new ArrayList<Future<Void>>();
		int pagerecord = 10000;
		int threadCount = 4 ; //开启四个线程
		for (int i = 0; i < threadCount ; i++) {
			ex.submit(new CheckTask((i-1)*pagerecord, pagerecord));
		}
		while( valid.get() == true &&  completeTask.get() < threadCount )  { //改进
			
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

}
