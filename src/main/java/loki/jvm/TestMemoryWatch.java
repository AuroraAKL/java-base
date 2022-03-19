package loki.jvm;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestMemoryWatch {

    public static final long mb = 1024 * 1024;
    public static final long ms = 1000 * 1000;
    byte[] bytes = new byte[(int) mb];
//    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 20, TimeUnit.SECONDS, new LinkedBlockingDeque<>());



    public static void function() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory
                .getOperatingSystemMXBean();
        // 总的物理内存
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize() / mb;
        // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / mb;
        // 已使用的物理内存
        long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / mb;

        System.out.println(totalMemorySize);
        System.out.println(freePhysicalMemorySize);
        System.out.println(usedMemory);

        // 获得线程总数
        ThreadGroup parentThread;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                .getParent() != null; parentThread = parentThread.getParent()) {
        }
        int totalThread = parentThread.activeCount();
        System.out.println("activeThreadCount:" + totalThread);

        // memeory
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage(); //椎内存使用情况
        long used = memoryUsage.getUsed();
        long max = memoryUsage.getMax();
        System.out.println(String.format("used:%dmb max:%dmb %%%f", used / mb, max / mb, used / 1.0 / max));

        // CPU
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long currentCpuTime = threadMXBean.getCurrentThreadCpuTime(); //当前线程的cpu使用时间
        long threadCount = threadMXBean.getThreadCount(); //当前线程的cpu使用时间
        long currentThreadUserTime = threadMXBean.getCurrentThreadUserTime();
        long peakThreadCount = threadMXBean.getPeakThreadCount();
        System.out.println(String.format("cpu: %d threadCount:%d currentThreadUserTime:%d peakThreadCount:%d",
                currentCpuTime / ms, threadCount, currentThreadUserTime / ms, peakThreadCount));

        // gc info
        List<GarbageCollectorMXBean> gcMXBeans = ManagementFactory.getGarbageCollectorMXBeans();
        StringBuilder stringBuilder = new StringBuilder();
        for (GarbageCollectorMXBean gcMXBean : gcMXBeans) {
            stringBuilder.append(gcMXBean.getName()).append(": count:")
                    .append(gcMXBean.getCollectionCount())
                    .append(" time:")
                    .append(gcMXBean.getCollectionTime()).append("ms").append(',');
        }
        System.out.println(String.format("gc: %s", stringBuilder.toString()));



    }

    public static void main(String[] args) throws InterruptedException {
        List<Object> objectList = new ArrayList<>(10000);
        for (int i = 1; i <= 100; i++) {
            objectList.add(new TestMemoryWatch());
            long free = Runtime.getRuntime().freeMemory();
            System.out.println(free / mb);
            function();
            Thread thread = new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            objectList.add(thread);
            thread.start();

            Thread.sleep(500);
        }
    }
}
