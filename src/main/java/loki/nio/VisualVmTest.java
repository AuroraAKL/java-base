package loki.nio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class VisualVmTest {
    public static void main(String[] args) throws InterruptedException {
        List<String> list2 = new ArrayList<>();
        new Thread(()->{
            List<Object> list = new LinkedList<>(); // 18019
            int count = 0;
            while(true){
                list.add(new HashSet<String>());
                ++count;
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(count);
            }
        }).start();
        new Thread(()->{
            List<String> list = new ArrayList<>(); // 18019
            int count = 0;
            while(true){
                list.add(String.valueOf(System.currentTimeMillis()));
                ++count;
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.err.println(count);
            }
        }).start();
    }
}
