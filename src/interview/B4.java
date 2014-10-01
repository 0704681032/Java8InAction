package interview;

import java.io.Reader;

public class B4 {
	public static void main(String[] args) {
		String str = "Jacky:25|Marry:23|Mark:28";
		
		String[] result = str.split("\\|");
		
		System.out.println(result[0]);
		
		String[] arr = result[0].split(":");
		System.out.println(arr[0]);
		
		
		Reader r = null;
	}
}
