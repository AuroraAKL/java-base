package loki.string;

public class StringDemo {
    public static void main(String[] args) {

        String newStr = new String("123");
        String str = "123";
        System.out.println(str == newStr);// false

        System.out.println("------------");


        String newStr2 = new String("1232");
        newStr2 = newStr2.intern();// 在pool中找值，并且存入pool
        String str2 = "1232";
        System.out.println(str2 == newStr2);// true

        System.out.println("变量拼接常量 会新建一个字符串对象");

        // 变量拼接常量 会新建一个字符串对象
        String str3 = "ab";
        String str4 = "abc";
        String str33 = str3 + "c";
        System.out.println(str4 == str33);// false

        Integer i = Integer.valueOf(100);
        i = 3;
        int a = i;

    }
}
