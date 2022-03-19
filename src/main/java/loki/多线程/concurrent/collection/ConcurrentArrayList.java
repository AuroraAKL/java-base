package loki.多线程.concurrent.collection;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentArrayList<E> extends ArrayList<E>{

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    @Override
    public boolean add(E e) {
        try{
            writeLock.lock();
            return super.add(e);
        }finally {
            writeLock.unlock();
        }
    }

    @Override
    public E remove(int index) {
        try{
            writeLock.lock();
            return super.remove(index);
        }finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean remove(Object o) {
        try{
            writeLock.lock();
            return super.remove(o);
        }finally {
            writeLock.unlock();
        }
    }
}
