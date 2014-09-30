/** 
 * ����lambda���ʽ 
 * 
 * @author benhail 
 */ 
public class TestLambda { 
 
    public static void runThreadUseLambda() { 
        //Runnable��һ�������ӿڣ�ֻ�������и��޲����ģ�����void��run������ 
        //����lambda���ʽ���û�в������ұ�Ҳû��return��ֻ�ǵ����Ĵ�ӡһ�仰 
        new Thread(() ->System.out.println("lambdaʵ�ֵ��߳�")).start();  
    } 
 
    public static void runThreadUseInnerClass() { 
        //���ַ�ʽ�Ͳ��ི�ˣ���ǰ�ɰ汾�Ƚϳ��������� 
        new Thread(new Runnable() { 
            @Override 
            public void run() { 
                System.out.println("�ڲ���ʵ�ֵ��߳�"); 
            } 
        }).start(); 
    } 
 
    public static void main(String[] args) { 
        TestLambda.runThreadUseLambda(); 
        TestLambda.runThreadUseInnerClass(); 
    } 
} 