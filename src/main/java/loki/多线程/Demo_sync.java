package loki.多线程;

import loki.多线程.util.Tools;

/**
 * 代码块锁
 * synchronized (锁对象){
 *
 * }
 */
public class Demo_sync {
    public static void main(String[] args) {
        Sync sync = new Sync();
        Thread thread = new Thread() {
            @Override
            public void run() {
                sync.print1();
            }
        };
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                sync.print2();
            }
        };
        thread.start();
        thread1.start();
    }
}
class Sync{
    public void print1(){
        for (int i = 0; i < 20; i++) {
            synchronized (Sync.class){// 保证对于Sync这个类， 对于print1和print2是同步的
                System.out.print("1");
                System.out.print("2");
                System.out.print("3");
                System.out.print("4");
                System.out.println();
                Tools.sleep(70);
            }
        }
    }
    public void print2(){
        for (int i = 0; i < 20; i++) {
            synchronized (Sync.class) {
                System.out.print("a");
                System.out.print("b");
                System.out.print("c");
                System.out.print("d");
                System.out.println();
                Tools.sleep(70);
            }
        }
    }
}
