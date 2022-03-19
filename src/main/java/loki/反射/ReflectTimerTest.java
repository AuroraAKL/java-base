package loki.反射;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试反射 和 反射缓存时间差
 */
public class ReflectTimerTest {

    public static class A {
        public String string;
        public List<List<String>> listList;
        public Long id;
        public String name;
        public LocalDateTime dateTime;
        public LocalDateTime dateTime2;
    }


    public static void testFields(int n) {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            Field[] declaredFields = A.class.getDeclaredFields();
        }
        long spend1 = System.nanoTime() - start;

        Map<Class<?>, Field[]> cache = new HashMap<>(1000);
        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            Field[] declaredFields = cache.get(A.class);
            if (declaredFields == null) {
                declaredFields = A.class.getDeclaredFields();
                cache.put(A.class, declaredFields);
            }
        }


        long spend2 = System.nanoTime() - start;
        System.out.println(spend1 + " ns | cache:" + spend2 + " ns");
        System.out.println((spend1 - spend2) + ":差距");
    }

    public static void testFields2(int n) {
        Map<Class<?>, Field[]> cache = new HashMap<>(1000);
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            Field[] declaredFields = cache.get(A.class);
            if (declaredFields == null) {
                declaredFields = A.class.getDeclaredFields();
                cache.put(A.class, declaredFields);
            }
        }

        long spend1 = System.nanoTime() - start;

        start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            Field[] declaredFields = A.class.getDeclaredFields();
        }

        long spend2 = System.nanoTime() - start;
        System.out.println(spend1 + " ns :cache | no cache:" + spend2 + " ns");
        System.out.println((spend2 - spend1) + ":差距");
    }

    public static void testSystemTime(int n) {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            System.currentTimeMillis();
        }
        System.out.println(System.nanoTime() - start);
    }

    public static void testSystemNanoTime(int n) {
        long start = System.nanoTime();
        for (int i = 0; i < n; i++) {
            System.nanoTime();
        }
        System.out.println(System.nanoTime() - start);
    }

    public static void testWaitTime(int n) {
        long start = System.currentTimeMillis();
        long now = start;
        do {
            now = System.currentTimeMillis();
        } while (now - start <= n);
    }

    public static void main(String[] args) {

//        testFields(10000);
//        testFields2(10000);
        int n = 10000000;
//        testSystemTime(n);
//        testSystemNanoTime(n);
        testWaitTime(10000000);


    }
}
