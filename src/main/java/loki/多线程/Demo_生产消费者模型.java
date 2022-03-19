package loki.多线程;

import loki.多线程.util.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * 利用wait和notify 实现生产者消费者模式
 * 消费者 当需要消费而没有产品时，唤醒生产者，消费者等待
 * 生产者 当有产品时等待，生产出产品后唤醒消费者
 *
 * 缺点：对于多消费者线程来说 需要频繁的唤醒和等待，大量的线程切换 性能低下
 */
public class Demo_生产消费者模型 {
    public static void main(String[] args) {

        ProductStore store = new ProductStore();
        Producer producer = new Producer(store, 2000);
        producer.setName("pro 1");
        for (int i = 0; i < 4; i++) {
            Consumer consumer = new Consumer(store, 2);
            consumer.setName(i + "");
            consumer.start();
        }
        producer.start();

    }

    /**
     * 仓库
     */
    static class ProductStore {
        private final List<Product> store = new ArrayList<>(10);
        private synchronized boolean isEmpty(){
            return store.isEmpty();
        }
        private synchronized void add(Product product){
            store.add(product);
        }
        private synchronized Product getAndRemove(){
            Product product = store.get(0);
            store.remove(0);
            return product;
        }
        public Product get(){
            Product product = null;
            synchronized (this){
                // 只唤醒生产者，而自己不苏醒
                if(isEmpty()){
                    notifyAll();
                    System.out.println("唤醒生产者!");
                }
                while (isEmpty()){// 唤醒生产者线程, 自己可能被其他消费者唤醒------需要避免
                    try {
                        System.out.println("消费者进入等待！");
                        wait();
                        System.out.println("消费者苏醒！");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 消费掉生产者生产的产品
                product = getAndRemove();
                System.out.println("消费:" + product);
            }
            return product;
        }
        public void put(Product product){
            synchronized (this){
                while(!isEmpty()){// 生产了就唤醒消费者线程
                    try {
                        System.out.println("生产者进入等待！");
                        wait();
                        System.out.println("生产者苏醒！");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                add(product);
                System.out.println("生产:" + product);
                notifyAll();
                System.out.println("唤醒消费者!");
            }
        }
    }

    /**
     * 产品
     */
    static class Product{

    }

    /**
     * 生产者
     */
    static class Producer extends Thread{
        ProductStore store;
        // 生产速度
        private int speed;

        public Producer(ProductStore store, int speed){
            this.store = store; this.speed = speed;
        }

        @Override
        public void run() {
            while (true){
                store.put(new Product());
                Tools.sleep(speed);
            }
        }
    }

    /**
     * 消费者
     */
    static class Consumer extends Thread{
        ProductStore store;
        // 需求数量
        private int requireCount;

        public Consumer(ProductStore store, int requireCount){
            this.store = store; this.requireCount = requireCount;
        }

        @Override
        public void run(){
            for (int i = 0; i < requireCount; i++) {
                store.get();
            }
            System.out.println("消费者" + getName() + " 消费结束!");
        }

    }
}
