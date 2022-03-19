package loki.多线程.cas;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class TestInteger {

    private int count = -1;

    private static final Unsafe unsafe = getUnsafe();

    private static long offset = 0;

    static {
        Field field = null;
        try {
            field = TestInteger.class.getDeclaredField("count");
            offset = unsafe.objectFieldOffset(field);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public int getValue() {
        return this.count;
    }

    public int increment() {
        int v;
        do {
            v = unsafe.getIntVolatile(this, offset);
        } while (!compareAndSet(v, v + 1));
        return this.count;
    }

    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, offset, expect, update);
    }

    public static Unsafe getUnsafe() {
        Field theUnsafe = null;
        try {
            theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws InterruptedException {
        int total = 10000;
        int threadN = 100;
        ConcurrentHashMap<Integer, Object> set = new ConcurrentHashMap<>();
        TestInteger integer = new TestInteger();

        CountDownLatch countDownLatch = new CountDownLatch(threadN);
        for (int i = 0; i < threadN; i++) {
            new Thread(()->{
                for (int j = 0; j < total / threadN; j++) {
                    int t = integer.increment();
                    set.put(t, t);
                    System.out.println(t);
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();

        System.out.println("set:" + set.size());

    }
}
