package loki.多线程;

/**
 * 暂停线程
 */
public class TestSuspendResume {

    public static void testSuspendResume() throws InterruptedException {
        Thread thread = new Thread(()->{
            for (int i = 0; i < 100000; i++) {
                System.out.println(i);
            }
        });
        thread.start();
        Thread.sleep(2);
        System.out.println("start");
        // 暂停
        thread.suspend();
        //System.out.println("suspend"); 这里可能会发送死锁, 因为 println 内部是有锁的, 但是 suspend不会释放正在占用的锁.
        // 恢复
        thread.resume();
        System.out.println("resume");
        thread.stop();
    }

    public static void main(String[] args) throws InterruptedException {
        testSuspendResume();
    }
}
