package loki._static;

public class StaticBlock2 {
    static StaticBlock staticBlock1 = new StaticBlock("static 2 field");


    static {
        System.out.println("static2 block！");
    }

    public static void func() {
        System.out.println("static2 func");
    }

    public StaticBlock2() {
        System.out.println("static2 gou zhao");
    }
}

