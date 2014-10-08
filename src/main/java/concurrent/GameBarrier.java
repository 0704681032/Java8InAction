package concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * http://blog.csdn.net/flyingpig4/article/details/5813945
 */

/**
 * edit by jyy
 * 对代码进行改造
 * 需求如下:等所有玩家都过了某一关的时候才能玩下一关
 */

public class GameBarrier {

    public static final int TOTAL= 5 ;

    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                System.out.println("全部已经过关 ");
            }
        });
        for (int i = 0; i < 4; i++) {
            new Thread(new Player(i, cyclicBarrier)).start();
        }
        System.out.println("测试结束");
    }
}
class Player implements Runnable {
    private CyclicBarrier cyclicBarrier;
    private int id;
    public Player(int id, CyclicBarrier cyclicBarrier) {
        this.cyclicBarrier = cyclicBarrier;
        this.id = id;
    }
    private int current = 1;


    @Override
    public void run() {
        while( current <= GameBarrier.TOTAL ) {
            try {
                System.out.println("玩家" + id + "正在玩第"+current+"关...");
                cyclicBarrier.await();
                current++;
                //System.out.println("玩家" + id + "进入第二关...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

    }
}