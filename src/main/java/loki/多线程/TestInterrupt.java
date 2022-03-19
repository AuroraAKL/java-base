package loki.多线程;

import java.util.concurrent.locks.ReentrantLock;

public class TestInterrupt {

    public static void testInterruptSleep() {
        Thread thread = new Thread(()->{
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // 中断时抛出异常
                e.printStackTrace();
                System.out.println("run end:" + Thread.currentThread().isInterrupted());
                // 将线程设置为中断状态, 将中断传递出去
                Thread.currentThread().interrupt();
            }
            System.out.println("run end:" + Thread.currentThread().getName() + "|" + Thread.currentThread().isInterrupted());
        });
        thread.start();
        thread.interrupt();
        System.out.println("run end:" + Thread.currentThread().getName());
    }

    public static void testInterruptSleep2() {
        Thread thread = new Thread(()->{
            try {
                for (int i = 0; i < 100000; i++) {
                }
                System.out.println("sleep start");
                Thread.sleep(3000);
                System.out.println("sleep end");
            } catch (InterruptedException e) {
                System.out.println("InterruptedException throw :" + Thread.currentThread().isInterrupted());
            }
        });
        thread.start();
        thread.interrupt();
        System.out.println("interrupt");
    }

    public static void testInterruptFor() throws InterruptedException {
        Thread thread = new Thread(()->{
            for (int i = 0; i < 1000000; i++) {
                if (Thread.interrupted()) {
                    // 线程已经中断, 这里退出运行
                    return;
                }
                System.out.println(i);
            }
            System.out.println("run end:" + Thread.currentThread().isInterrupted());
            System.out.println("run end:" + Thread.interrupted());
        });
        thread.start();
        Thread.sleep(10);
        thread.interrupt();
        System.out.println("run end:" + Thread.currentThread().getName());
    }

    /**
     * 测试中断获取锁
     * 如果线程被中断, 获取锁将会抛出中断异常. 而不是一直在等待.
     * @throws InterruptedException
     */
    public static void testInterruptLock() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Thread thread = new Thread(()->{
            try {
                lock.lockInterruptibly();
                System.out.println("get lock");
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        lock.lock();
        thread.start();

        Thread.sleep(3_000);
        thread.interrupt();
        System.out.println("run end:" + Thread.currentThread().getName());
    }

    /**
     * 测试主动引发中断
     */
    public static void testSetInterrupt() {

    }

    public static void main(String[] args) throws InterruptedException {
//        testInterruptSleep2();
        testInterruptLock();

    }
}
