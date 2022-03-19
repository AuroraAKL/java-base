package loki.多线程.concurrent.collection;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Demo_ConcurrentLinkedQueue {
    public static void main(String[] args) {
        ConcurrentLinkedQueue<Object> queue = new ConcurrentLinkedQueue<Object>();
        queue.offer("123");
    }
}
