package interview;

import java.util.Random;

public class A7 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Sting #name ;
		
		String $student ;
		
		//String 3dog ;
		
		String Boolean ;
		
		char a = '1';
		
		for ( int i = 0 ; i < 100 ;i++) {
			int t = new Random().nextInt(6);
			//System.err.println(t);
		}
		
		
		Float f = 1.3f;
		Double d = 1.3;
		System.err.println(f.equals(d));//false
		
		
		test();
	}
	
	
	public static void test()  {
		
		try {
			int a  = 4 ;
			if ( a > 4 || a/0 >3 ) {
				System.out.println("try 1");
			} else {
				System.out.println("try 2");
			}
		}catch (Exception e) {
			System.out.println("Exception");
		}finally {
			System.out.println("finally");
		}
		System.out.println("last");
	}

}
