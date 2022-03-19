package loki.多线程.cas;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 通过使用CAS 来同步 多元的不变约束
 */
public class CasNumberRange {

    private static class IntPair {
        final int lower;
        final int upper;
        private IntPair(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }

    private final AtomicReference<CasNumberRange.IntPair> reference = new AtomicReference<>(new IntPair(0, 0));

    public int getUpper() {
        return reference.get().upper;
    }

    public int getLower() {
        return reference.get().lower;
    }

    public int setLower(int newLower) {
        while (true) {
            IntPair oldVal = reference.get();
            if (newLower > oldVal.upper) {
                throw new IllegalArgumentException();
            }
            // CAS 更新引用 -- 一次更新多个数据
            if(reference.compareAndSet(oldVal, new IntPair(newLower, oldVal.upper))) {
                return newLower;
            }
        }
    }

    public int setUpper(int newUpper) {
        while (true) {
            IntPair oldVal = reference.get();
            if (newUpper < oldVal.lower) {
                throw new IllegalArgumentException();
            }
            // CAS 更新引用 -- 一次更新多个数据
            if(reference.compareAndSet(oldVal, new IntPair(oldVal.lower, newUpper))) {
                return newUpper;
            }
        }
    }

    public static void main(String[] args) {

    }

}
