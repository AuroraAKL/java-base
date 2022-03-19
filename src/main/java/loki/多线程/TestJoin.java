package loki.多线程;

public class TestJoin {

    public static void main(String[] args) {
        Thread thread = new Thread(()->{
            System.out.println("thread run:" + Thread.currentThread().getName());
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("thread run:" + Thread.currentThread().getName());
    }
}
