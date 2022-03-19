package loki.多线程.concurrent.collection;

import java.util.Vector;

/**
 * vector是
 * 读读，读写，，写写，都互斥
 * 的多线程安全的集合
 *
 * add,remove方法上都有synchronized关键字修饰
 */
public class Demo_Vector {
    public static void main(String[] args) {
        Vector vector = new Vector<Object>();
        vector.add("123");
        vector.remove(0);
    }
}
