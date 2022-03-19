package loki.多线程.IDBuilder;

public class IDBuilder {
    private static int id = 0;

    public static int nextSequence(){
        synchronized (IDBuilder.class){// 保证线程安全
            if(id >= 999){
                id = 0;
            }else {
                id++;
            }
        }
        return id;
    }

    public static void main(String[] args) {
        int n = 30;
        Thread[] threads = new Thread[100];
        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(new TestRun());
        }
        for (int i = 0; i < n; i++) {
            threads[i].start();
        }
    }

    private static class TestRun implements Runnable{

        public void run() {
            System.out.println(Thread.currentThread().getName() + ":" + IDBuilder.nextSequence());
        }
    }
}
