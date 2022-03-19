package loki.多线程;


import loki.多线程.util.Tools;

public class Demo_wait线程通信 {
    public static void main(String[] args) {
        MyThread myThread = new MyThread();
        new Thread(myThread::fun1).start();

        new Thread(myThread::fun2).start();

    }

    static class MyThread{
        public void fun1(){
            for (int i = 0; i < 20; i++) {
                synchronized (MyThread.class){
                    if(i == 10){
                        try {
                            System.out.println("等待！");
                            MyThread.class.wait();// 等待,直到唤醒为止
                            System.out.println("醒来！");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("fun1 " + i);
                    Tools.sleep(120);
                }
            }
        }

        public void fun2(){
            for (int i = 0; i < 30; i++) {
                synchronized (MyThread.class){
                    if(i == 20){
                        MyThread.class.notify();// 唤醒线程
                        System.out.println("唤醒！");
                    }
                    System.out.println("fun2 " + i);
                    Tools.sleep(120);
                }
            }
        }

    }

}

