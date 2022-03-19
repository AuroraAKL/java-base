package loki.多线程.可见性;


import loki.多线程.util.Tools;

/**
 * 父线程在启动子线程之前，对于共享变量的更新，对于子线程来说是可见的
 */
public class ThreadStartVisibility {

    static int data = 0;

    public static void main(String[] args) {
        Thread thread = new Thread(()->{
            Tools.randomSleep(50);
            System.out.println(data);// 有时输出1，有时输出2
        });
        data = 1; // 在子线程启动之前， 子线程可见
        thread.start();
        Tools.randomSleep(50);
        data = 2;// 在子线程启动之后，子线程可见性不保证

    }
}
