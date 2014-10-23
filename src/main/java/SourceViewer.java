import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SourceViewer {
	public static void main(String[] args) {
		List<String> languages = Arrays.asList("Java","Js");
		Collections.sort(languages, (s1,s2) -> { return s1.compareTo(s2);});
		languages.forEach(System.out::println);
		
		Class cls = Object.class;
		
		Vector v = new  Vector();
		
		HashMap map = new HashMap();
		
		TreeMap treeMap = new TreeMap();
		
		
		StringBuffer sb = new StringBuffer();
		
		Lock lock = new ReentrantLock();
		
	}

}
