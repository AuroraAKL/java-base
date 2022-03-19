package loki.多线程.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockMain {

    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            try {
                lock.lock();
                condition.await(5L, TimeUnit.SECONDS);
                System.out.println("await 被唤醒");
            } catch (InterruptedException e) {
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            try {
                lock.lock();
                condition.await(5L, TimeUnit.SECONDS);
                System.out.println("await 被唤醒");
            } catch (InterruptedException e) {
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() ->{
            try {
                TimeUnit.SECONDS.sleep(1L);
                lock.lock();
                condition.signalAll();
                System.out.println("我去唤醒");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }).start();


    }


}
