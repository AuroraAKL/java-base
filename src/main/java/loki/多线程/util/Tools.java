package loki.多线程.util;

public class Tools {

    /**
     * 暂停当前线程一段时间
     * @param time
     */
    public static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 随机暂停当前线程一段时间，时间由给定的time决定
     * @param time
     */
    public static void randomSleep(long time){
        sleep((long) (Math.random() * (time + 1)));
    }

    /**
     * 计时器
     * 记录方法时间间隔
     */
    public static class TimerRecode {
        private long startTime;
        public TimerRecode(){
            reset();
        }
        public void reset(){
            startTime = System.nanoTime();
        }
        public long utilMMTime(){
            return (System.nanoTime() - startTime) / 1000 / 1000;
        }
    }
}
