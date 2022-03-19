package loki.多线程.可见性;

import loki.多线程.util.Tools;

public class Inversion {


    public static void main(String[] args) {
        TimeConsumingTask task = new TimeConsumingTask();
        Thread thread = new Thread(task);
        thread.start();

        Tools.sleep(2000);
        task.cancel();
    }
    static class TimeConsumingTask implements Runnable{
        private boolean isCancel = false;// 是否取消，退出
        @Override
        public void run() {
            while(!isCancel){
                if(doExecute()){
                    break;
                }
            }
            if (isCancel){
                System.out.println("Task was canceled!");
            }else{
                System.out.println("Task done!");
            }
        }
        private boolean doExecute(){
            boolean isDone = false;
            Tools.randomSleep(50);
            return isDone;
        }

        public void cancel(){
            isCancel = true;
            System.out.println("取消成功！");
        }

    }

}
