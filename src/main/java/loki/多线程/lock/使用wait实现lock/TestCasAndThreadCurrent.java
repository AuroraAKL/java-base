package loki.多线程.lock.使用wait实现lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 测试 一次cas 和 获取当前运行的线程哪个速度更快
 */
public class TestCasAndThreadCurrent {
    int state;

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

    static final long stateOffset = getStateOffset();


    private static long getStateOffset() {
        try {
            Field field = TestCasAndThreadCurrent.class.getDeclaredField("state");
            return unsafe.objectFieldOffset(field);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean casState(int except, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, except, update);
    }

    public static void main(String[] args) {
        System.out.println(stateOffset);
        TestCasAndThreadCurrent current = new TestCasAndThreadCurrent();
        int n = 1000000;
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            Thread.currentThread();
        }
        long end = System.nanoTime();

        long start2 = System.nanoTime();
        for (int i = 0; i < n; i++) {
            current.casState(0, 1);
        }
        long end2 = System.nanoTime();

        long l = end - start;
        long l1 = end2 - start2;
        System.out.println(l + " " + l1 + " : " + (l - l1));
    }


}
