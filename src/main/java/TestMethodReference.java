import java.awt.FlowLayout; 
import java.awt.event.ActionEvent; 
import javax.swing.JButton; 
import javax.swing.JFrame; 
 
/** 
 * 
 * @author benhail 
 */ 
public class TestMethodReference { 
 
    public static void main(String[] args) { 
 
        JFrame frame = new JFrame(); 
        frame.setLayout(new FlowLayout()); 
        frame.setVisible(true); 
         
        JButton button1 = new JButton("����!"); 
        JButton button2 = new JButton("Ҳ����!"); 
         
        frame.getContentPane().add(button1); 
        frame.getContentPane().add(button2); 
        //����addActionListener�����Ĳ�����ActionListener����һ������ʽ�ӿ� 
        //ʹ��lambda���ʽ��ʽ 
        button1.addActionListener(e -> { System.out.println("������Lambdaʵ�ַ�ʽ"+e); }); 
        //ʹ�÷������÷�ʽ 
        button2.addActionListener(TestMethodReference::doSomething); 
         
    } 
    /** 
     * �����Ǻ���ʽ�ӿ�ActionListener��ʵ�ַ��� 
     * @param e  
     */ 
    public static void doSomething(ActionEvent e) { 
         
        System.out.println("�����Ƿ�������ʵ�ַ�ʽ"); 
         
    } 
} 