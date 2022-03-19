package loki.多线程.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS 并发栈
 */
public class ConcurrentStack<T> {

    private T[] stack;
    private volatile AtomicInteger size = new AtomicInteger(10);
    private volatile AtomicInteger top = new AtomicInteger(0);

    public T push(T data) {
        while (true) {
            if(isEmpty()) {
                throw new IllegalArgumentException();
            }
            int t = top.get();
            if(top.compareAndSet(t, t + 1)) {
                stack[t] = data;
                return data;
            }
        }
    }

    public T pop() {
        while (true) {
            if(isEmpty()) {
                throw new IllegalArgumentException();
            }
            int t = top.get();
            if(top.compareAndSet(t, t - 1)) {
                T data = stack[t];
                stack[t] = null;
                return data;
            }
        }
    }

    public boolean isEmpty() {
        return top.get() <= 0;
    }

    public int size() {
        return size.get();
    }


    public static void main(String[] args) {

    }


}
