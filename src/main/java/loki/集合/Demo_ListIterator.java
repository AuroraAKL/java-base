package loki.集合;

import java.util.*;

public class Demo_ListIterator {
    public static void main(String[] args) {
        List list = new ArrayList();
        list.add("123");
        list.add("234");
        ListIterator listIterator = list.listIterator();// 通过listiterator迭代时添加
        while (listIterator.hasNext()){
            Object ele = listIterator.next();
            if(ele.equals("234")){
                listIterator.add("add");
            }
        }
        System.out.println(list.toString());
    }
}
