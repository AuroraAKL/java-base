package loki.多线程.lock;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class TestMyReentrantNoFairLock {


    private static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        extracted();

    }

    private static void extracted() throws InterruptedException {
        MyReentrantNoFairLock lock = new MyReentrantNoFairLock();


        CountDownLatch countDownLatch = new CountDownLatch(1000);
        IntStream.range(0, 1000).forEach(i -> new Thread(() -> {
            lock.lock();
            lock.lock();
            lock.unlock();
            try {
                IntStream.range(0, 10000).forEach(j -> count++);
                System.out.println("run: " + Thread.currentThread().getName());
            } finally {
                lock.unlock();
            }
            countDownLatch.countDown();
        }, "tt-" + i).start());


        countDownLatch.await();


        System.out.println(count);
    }
}
