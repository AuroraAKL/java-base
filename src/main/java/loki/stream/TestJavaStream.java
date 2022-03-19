package loki.stream;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestJavaStream {


    public static void main(String[] args) {
        List<String> collect = Stream.of("star", "stat").sorted().collect(Collectors.toList());
        System.out.println(collect);
        // 分组
        Map<String, Set<String>> collect1 = Stream.of("star", "stat").sorted()
                .collect(Collectors.groupingBy(item -> item, Collectors.toSet()));

        // 枚举数组 --- 可以替代 位域模式
        EnumSet.of(null);
    }
}
