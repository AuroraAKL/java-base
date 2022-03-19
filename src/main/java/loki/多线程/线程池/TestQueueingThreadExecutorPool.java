package loki.多线程.线程池;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestQueueingThreadExecutorPool {
    public static void main(String[] args) throws InterruptedException {

        testQueueingThreadExecutor();
//        testThreadExecutor();
//        testQueueingThreadExecutor();
//        testThreadExecutor();


    }

    private static void testThreadExecutor() throws InterruptedException {
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 10,
                30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(20), new ThreadPoolExecutor.CallerRunsPolicy());
        int n = 1000;
        CountDownLatch latch = new CountDownLatch(n);

        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            executorPool.execute(()->{
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
            TimeUnit.MILLISECONDS.sleep(10);
        }

        latch.await();

        long end = System.nanoTime();
        System.out.println((end - start) + " (ns)");
        executorPool.shutdownNow();
    }

    private static void testQueueingThreadExecutor() throws InterruptedException {
        QueueingThreadExecutor executorPool = new QueueingThreadExecutor(2, 10,
                30, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2000), new QueueingThreadExecutor.CallerRunsPolicy());
        int n = 1000;
        CountDownLatch latch = new CountDownLatch(n);

        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            executorPool.execute(()->{
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                    System.out.println(String.format("queue:%d work:%d active:%d",
                            executorPool.getQueue().size(), executorPool.getPoolSize(), executorPool.getActiveCount()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            });
            TimeUnit.MILLISECONDS.sleep(10);
        }

        latch.await();

        long end = System.nanoTime();
        System.out.println((end - start) + " (ns)");
        executorPool.shutdownNow();
    }
}
