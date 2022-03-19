package loki.多线程;

public class TestStop {

    public static void testStop() {
        Thread thread = new Thread(() -> {
            try {
                for (int i = 0; i < 1000000; i++) {
                    System.out.println(i);
                    Thread.currentThread().stop();
                }
                System.out.println("end");
            } catch (ThreadDeath e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public static void testStop2() throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1000000; i++) {
                System.out.println(i);
            }
            System.out.println("end");
        });
        thread.start();
        Thread.sleep(100);
        try {
            thread.stop();
        } catch (ThreadDeath e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        testStop();
        testStop2();
    }
}
