package loki.多线程.lock;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class TestMyLock {


    private static int count = 0;
    public static void main(String[] args) throws InterruptedException {
        extracted();
//        testLock();

    }

    private static void testLock() {
        MyNoFairLock myNoFairLock = new MyNoFairLock();
        myNoFairLock.lock();

        myNoFairLock.lock();
    }

    private static void extracted() throws InterruptedException {
        MyNoFairLock lock = new MyNoFairLock();


        CountDownLatch countDownLatch = new CountDownLatch(1000);
        IntStream.range(0, 1000).forEach(i -> new Thread(() -> {
            lock.lock();
            try {
                IntStream.range(0, 10000).forEach(j -> count++);
            } finally {
                lock.unlock();
            }
            countDownLatch.countDown();
        }, "tt-" + i).start());


        countDownLatch.await();


        System.out.println(count);
    }
}
