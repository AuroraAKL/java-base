package loki.线程不安全;

public class LongWriter implements Runnable {

    private static final double CONST = 130009000000000L;

    static  double timer;

    public static void main(String[] args) {
        // 在 client模式下，java 7版本long类型的更新可能是线程不安全的，被分为两个32位进行更新。应该使用 volatile 保护起来
        LongWriter longWriter = new LongWriter();
        new Thread(longWriter).start();
        new Thread(longWriter).start();
        new Thread(longWriter).start();
        new Thread(longWriter).start();
        new Thread(longWriter).start();
        double t = CONST;
        do {
            t = timer;
        } while (t == CONST);
        System.out.println(t);
    }

    @Override
    public void run() {
        while (true) {
            timer = CONST;
        }
    }
}
