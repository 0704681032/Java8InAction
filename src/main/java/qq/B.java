package qq;

/**
 * Created by jyy on 2014/10/21.
 *
 * 继承 类的初始化顺序
 *
 * 100 200 200
 */
class A {
    public int a = 100;
    public A() {
        System.out.println(this.a);
    }
}
public class B extends  A {

    public int a = 200;

    public B() {

        //super();

        System.out.println(this.a);

    }

    public static void main(String[] args) {

        System.out.println(new B().a);
    }

 }


