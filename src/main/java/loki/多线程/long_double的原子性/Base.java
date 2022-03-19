package loki.多线程.long_double的原子性;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * 在32位java虚拟机中 long/double的写操作不具有原子性
 */
public class Base  {
    static long value = 0L;
//    static volatile long value = 0l; // volatile使得写操作为原子操作

    public static void main(String[] args) {

        Thread thread = new Thread(new Run(-1L));
        Thread thread1 = new Thread(new Run(0L));
        thread.start();
        thread1.start();

        PrintStream printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        });

        long snapshot;
        while (0 == (snapshot = value) || -1 == snapshot){
            printStream.print(snapshot);
        }
        System.out.printf("unexpected data: %d(0x%016x)", snapshot, snapshot);
        printStream.close();
        System.exit(0);
    }
    private static class Run implements Runnable{
        private final long valueToSet;
        public Run(long value){
            this.valueToSet = value;
        }
        public void run() {
            for (;;){
                value = this.valueToSet;
            }
        }
    }
}
