package loki.多线程.lock.使用wait实现lock;

public class MyReentrantLock {

    private volatile int state;

    private volatile Thread lockThread;

    public synchronized void lock() {
        try {
            if (state == 0 || lockThread == Thread.currentThread()) {
                state++;
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
        } finally {
            lockThread = Thread.currentThread();
        }

    }

    public synchronized void unlock() {
        state--;
        if (state == 0) {
            lockThread = null;
            notifyAll();
        }
    }

}
