package loki.多线程;

import java.util.concurrent.*;

public class Demo_线程池 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);// 固定容量线程池
        ExecutorService executorService1 = Executors.newCachedThreadPool();// 缓存复用线程池

        executorService.submit(()->{
            System.out.println("run");
        });

        // 带返回值的执行方法
        Future<String> future = executorService.submit(new Callable<String >() {
            @Override
            public String call() {
                return "return";
            }
        });
        try {
            System.out.println( future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
