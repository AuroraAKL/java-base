package loki.json;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;

import java.util.HashMap;

public class TestJsonFromMap {
    public static void main(String[] args) {
//        testGson();
        testFastJson();
    }

    private static void testFastJson() {
        HashMap<String, String> src = new HashMap<String, String>();
        src.put("key", "value");
        String s = JSON.toJSONString(src);
        System.out.println(s);
    }

    private static void testGson() {
        HashMap<String, String> src = new HashMap<String, String>();
        src.put("key", "value");
        String s = new Gson().toJson(src);
        System.out.println(s);
    }
}
