package loki._static;

public class StaticBlock {

    // 1所有的static修饰的按照顺序执行
    static StaticBlock staticBlock1 = new StaticBlock("before");

    {// 2
        System.out.println("non-static block before!");
    }
    static {// 5 只调用一次
        System.out.println("static block！");
    }
    {// 3被创建一次调用一次，说明是一个成员
        System.out.println("non-static block after!");
    }
    // 6
    static StaticBlock staticBlock2 = new StaticBlock("after");

    public StaticBlock(String msg){// 4
        System.out.println("constructor!" + msg);
    }

    public static void main(String[] args) {

        new StaticBlock2();
//        StaticBlock2.func();
    }

}
