package interview;

import java.util.HashMap;
import java.util.LinkedHashMap;
/**
 * Integer≥£¡¡≥ÿ
 * @author jzy
 *
 */
public class T {
	public static void main(String[] args) {
		Integer t1 = 127;
		Integer t2 = 127;
		System.out.println(t1 == t2);
		
		Integer t3 = 128;
		Integer t4 = 128;
		System.out.println(t3 == t4);
		
		HashMap map = new HashMap();
		
		map = new LinkedHashMap();
	}
}
