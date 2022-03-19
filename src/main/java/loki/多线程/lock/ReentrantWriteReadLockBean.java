package loki.多线程.lock;

import loki.多线程.util.Tools;

import java.util.concurrent.locks.ReentrantReadWriteLock;

// 对 读读不互斥，对 读写，写写互斥
public class ReentrantWriteReadLockBean extends LockBean {
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    @Override
    public void setName(String name) {
        try{
            lock.writeLock().lock();
            this.name = name;
            Tools.sleep(1000);
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public String getName() {
        try{
            lock.readLock().lock();
            Tools.sleep(1000);
            return name;
        }finally {
            lock.readLock().unlock();
        }
    }
}
