package loki.asm.read.clazz;

/**
 * @author zhengquan
 */
public class HelloWorld implements Cloneable{
    public static final int initValue = 0;
    public long test(int a, int b) {
        int c = a + 1 + b;
        int d = a / b;
        return c + d;
    }
}
