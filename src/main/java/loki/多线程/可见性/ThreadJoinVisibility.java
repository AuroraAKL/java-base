package loki.多线程.可见性;

import loki.多线程.util.Tools;

/**
 * 对于共享变量的更新对于调用该线程的join方法的线程，是可见的
1 */
public class ThreadJoinVisibility {
    static int data = 0;

    public static void main(String[] args) {

        Thread thread = new Thread(()->{
            Tools.randomSleep(50);
            data = 1; // 更新共享变量值
        });
        thread.start();

        try {
            thread.join();// thread加入、插入当前线程，当前线程会等待thread执行完成后才会运行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(data);// main线程调用了thread的join，可见共享变量
    }


}
