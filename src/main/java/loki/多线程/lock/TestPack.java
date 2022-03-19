package loki.多线程.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class TestPack {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {

            System.out.println("start" + Thread.currentThread().getName());
            LockSupport.park();
            System.out.println("end" + Thread.currentThread().getName());
        });
        thread.setName("tt");
        thread.setDaemon(false);
        thread.start();

        System.out.println("end");
        TimeUnit.SECONDS.sleep(3);
        thread.interrupt();
        System.out.println("end interrupt");
        TimeUnit.SECONDS.sleep(3);
        LockSupport.unpark(thread);
    }
}
