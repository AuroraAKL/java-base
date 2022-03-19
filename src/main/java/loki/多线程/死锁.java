package loki.多线程;


import loki.多线程.util.Tools;

/**
 * 指两个及以上的进程/线程，在执行过程中出现的阻塞现象
 *
 */
public class 死锁 {
    public static void main(String[] args) {

        thread1.start();
        thread2.start();

    }
    static Thread thread1 = new Thread(new MyTask1());
    static Thread thread2 = new Thread(new MyTask2());

    static Object obj1 = new Object();
    static Object obj2 = new Object();

    static class MyTask1 implements Runnable{
        @Override
        public void run() {
            System.out.println("MyTask1 1");
            synchronized (obj1){// 锁 obj1
                Tools.sleep(500);
                synchronized (obj2){// 锁obj2
                    System.out.println("MyTask1 2");
                }
            }
        }
    }

    static class MyTask2 implements Runnable{
        @Override
        public void run() {
            System.out.println("MyTask2 1");
            synchronized (obj2){// 锁obj2
                Tools.sleep(500);
                synchronized (obj1){// 锁obj1
                    System.out.println("MyTask2 2");
                }
            }
        }
    }
}
