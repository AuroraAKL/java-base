package loki.算法;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRULinkedHashMap {
    public int[] LRU (int[][] operators, int k) {
        // write code here
        int resultLen = 0;
        for(int[] op : operators) {
            if (op[0] == 2) {
                ++resultLen;
            }
        }

        int result[] = new int[resultLen];
        resultLen = 0;

        LinkedHashMap<Integer, Integer> lru = new LinkedHashMap<Integer, Integer>(operators.length << 1) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > k;
            }
        };
        for(int[] op : operators) {
            if (op[0] == 1) {
                // set
                lru.put(op[1], op[2]);
            } else {
                // get
                result[resultLen++] = lru.getOrDefault(op[1], -1);

            }
        }
        return result;
    }
}
