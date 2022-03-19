package loki.多线程.concurrent.collection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class Demo_concurrenthashmap {
    public static void main(String[] args) {
        ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<Object, Object>();
        concurrentHashMap.put("123", "123");
        concurrentHashMap.put("1", "333");
        concurrentHashMap.remove("3");
    }
}
