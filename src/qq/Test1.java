package qq;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by jyy on 2014/10/21.
 *
 * 测试 finally return
 */
public class Test1 {

    public int t = 1;

    public int inc() {
        t++;
        return t;
    }

    public int  dec() {
        t--;
        return t;
    }

    public int test1() {
        try {
            return inc();
        } finally {
            return dec();
        }
    }

    public boolean test2() {
        try {
            return true;
        } finally {
            return false;
        }
    }

    public int test3() {
        try {
            return inc();
        } finally {
            dec();
        }
    }

    public static void main(String[] args) {
        Test1 obj = new Test1();
        System.out.println(obj.test1());
        System.out.println(obj.t);
        System.out.println(obj.test2());

        System.out.println(obj.test3());
        System.out.println(obj.t);
    }

 }
