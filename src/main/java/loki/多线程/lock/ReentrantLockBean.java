package loki.多线程.lock;


import loki.多线程.util.Tools;

import java.util.concurrent.locks.ReentrantLock;

// 可重入互斥锁（一个线程如果已经获取改锁，如果多次获取，将立即返回）
// 对 读读，读写，写写 阻塞
public class ReentrantLockBean extends LockBean {

    ReentrantLock lock = new ReentrantLock();

    @Override
    public void setName(String name) {
        try{
           lock.lock();
           Tools.sleep(1000);
           this.name = name;
        }finally {
            lock.unlock();
        }
    }
    @Override
    public String getName() {
        try{
            lock.lock();
            Tools.sleep(1000);
            return name;
        }finally {
            lock.unlock();
        }
    }
}
