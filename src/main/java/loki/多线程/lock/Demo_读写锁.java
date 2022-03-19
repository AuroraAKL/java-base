package loki.多线程.lock;

import loki.多线程.util.Tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Demo_读写锁 {
    public static void main(String[] args) {
        LockBean lockBean = new ReentrantWriteReadLockBean();
        Runnable readTask =  ()-> lockBean.getName();
        Runnable writeTask = ()-> lockBean.setName("123");

        Tools.TimerRecode timerRecode = new Tools.TimerRecode();
        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 3; i++) {
            executorService.submit(readTask);
        }
        for (int i = 0; i < 3; i++) {
            executorService.submit(writeTask);
        }
        executorService.shutdown();

        while(!executorService.isTerminated()){
        }
        System.out.println(timerRecode.utilMMTime());
    }
}
