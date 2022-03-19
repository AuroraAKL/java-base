package loki.多线程.线程湖;

import loki.多线程.线程池.QueueingThreadExecutor;
import sun.misc.Unsafe;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 1. 多线程池共享线程池
 * 2. 长时间未使用时释放
 * 3. 线程池使用线程结束后 回收线程
 * 4. 最大线程树限制, 如果到达了最大线程数 只能让线程都等待释放.
 *
 * @author zhengquan
 */
public class ThreadLake implements ThreadFactory, WorkProcessor {

    private static final AtomicInteger COMMON_SEQ = new AtomicInteger();

    private final int commonSeq = COMMON_SEQ.incrementAndGet();

    private final AtomicInteger threadSeq = new AtomicInteger();

    private final LinkedBlockingQueue<Work> freeWorkQueue = new LinkedBlockingQueue<>(500);

    private final AtomicInteger threadCount = new AtomicInteger();

    private final int maxThreadSize;

    private final long keepAliveTime;

    /**
     * 当线程创建失败时 等待空闲线程的等待时间
     */
    private static final long waitTime = 30;

    /**
     * unsafe
     */
    private static final Unsafe unsafe = getUnsafe();

    private static Unsafe getUnsafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ThreadLake(int maxThreadSize, long keepAliveTime, @Nonnull TimeUnit keepAliveTimeUnit) {
        if (maxThreadSize < 1) {
            throw new IllegalArgumentException();
        }
        if (keepAliveTime < 0) {
            keepAliveTime = 0;
        }
        this.maxThreadSize = maxThreadSize;
        this.keepAliveTime = keepAliveTimeUnit.toNanos(keepAliveTime);
    }

    private boolean tryIncreaseWorkCount() {
        int i = threadCount.get();
        while (i < maxThreadSize) {
            if (threadCount.compareAndSet(i, i + 1)) {
                return true;
            }
            i = threadCount.get();
        }
        return false;
    }

    @Nullable
    private Work tryCreateWork(@Nonnull Runnable r) {
        if (tryIncreaseWorkCount()) {
            String name = "ThreadLake[" + commonSeq + "]-" + threadSeq.incrementAndGet();
            Work work = new Work(r);
            work.setName(name);
            work.setDaemon(false);
            work.processor = this;
            return work;
        }
        return null;
    }

    @Override
    public void processWorkRelease(@Nonnull Work work) {
        System.out.println("pooled:" + work.getName());
        release(work);
    }

    private void release(@Nullable Work work) {
        if (work == null) {
            return;
        }
        freeWorkQueue.add(work);
    }

    class Work extends Thread {
        private WorkProcessor processor;

        private volatile long lastWorkEndTime;

        private volatile boolean isStarted;

        private Runnable task;

        public Work(Runnable target) {
            this.task = target;
        }

        private boolean isStarted() {
            return isStarted;
        }

        private void started() {
            isStarted = true;
        }

        @Override
        public synchronized void start() {
            if (isStarted()) {
                return;
            }
            started();

            super.start();
        }

        @Override
        public void run() {
            for (; ; ) {
                while (task == null && (System.nanoTime() - lastWorkEndTime) <= keepAliveTime) {
                    unsafe.park(false, keepAliveTime);
                }
                if (task == null) {
                    // 空闲过久了, 并且没有获取到任务
                    freeWorkQueue.remove(this);
                    System.out.println("release:" + this.getName());
                    return;
                }
                // 获取到任务
                Runnable thisTask = task;
                task = null;
                try {
                    thisTask.run();
                } finally {
                    // 执行结束
                    lastWorkEndTime = System.nanoTime();
                    if (processor != null) {
                        processor.processWorkRelease(this);
                    }
                }
            }
        }



        public Work reset(@Nonnull Runnable r) {
            task = r;
            LockSupport.unpark(this);
            return this;
        }
    }

    @Override
    public Thread newThread(@Nonnull Runnable r) {
        Work work = null;
        if ((work = freeWorkQueue.poll()) != null) {
            System.out.println("get into pool:" + work.getName());
            return work.reset(r);
        }
        for (; ; ) {
            if ((work = tryCreateWork(r)) != null) {
                System.out.println("new:" + work.getName() + " queue size:" + freeWorkQueue.size());
                return work;
            }

            try {
                if ((work = freeWorkQueue.poll(waitTime, TimeUnit.MILLISECONDS)) != null) {
                    System.out.println("get into pool:" + work.getName());
                    return work.reset(r);
                }
            } catch (InterruptedException ignored) {
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadLake lake = new ThreadLake(1, 10, TimeUnit.SECONDS);

        QueueingThreadExecutor threadPoolExecutor = new QueueingThreadExecutor(2, 3, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        threadPoolExecutor.setThreadFactory(lake);

        QueueingThreadExecutor threadPoolExecutor2 = new QueueingThreadExecutor(2, 3, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        threadPoolExecutor2.allowCoreThreadTimeOut(true);
        threadPoolExecutor2.setThreadFactory(lake);

        AtomicInteger count = new AtomicInteger();
        int n = 10;
        CountDownLatch latch = new CountDownLatch(n * 2);
        for (AtomicInteger i = new AtomicInteger(); i.get() < n; i.incrementAndGet()) {
            new Thread(()->{
                threadPoolExecutor.execute(() -> {
                    count.incrementAndGet();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    System.out.println(Thread.currentThread().getName() + " task " + count.get() + " 1");
                    latch.countDown();
                });

                threadPoolExecutor2.execute(() -> {
                    count.incrementAndGet();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    System.out.println(Thread.currentThread().getName() + " task " + count.get() + " 2");
                    latch.countDown();
                });
            }).start();
        }

        latch.await();
        System.out.println(count.get());


//
//
//        System.out.println(Thread.currentThread().getName());
//        long start = System.nanoTime();
//        unsafe.park(false, TimeUnit.MILLISECONDS.toNanos(3000));
//        long end = System.nanoTime();
//        System.out.println((end - start));
//        System.out.println(Thread.currentThread().getName());


    }

}

interface WorkProcessor {
    public void processWorkRelease(ThreadLake.Work work);
}
