package loki.多线程.lock.使用wait实现lock;

public class MyLock {

    private volatile int state;

    public synchronized void lock() {
        if (state == 0) {
            state = 1;
            return;
        }
        // 获取锁失败 需要进入等待
        for (; ; ) {
            if (state == 0) {
                state = 1;
                return;
            }
            try {
                wait();
            } catch (InterruptedException ignored) {
            }
        }
    }

    public synchronized void unlock() {
        state = 0;
        notify();
    }

}
