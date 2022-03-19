package loki.反射;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 原因：
 * 泛型的生命周期：源码，编译期
 * 反射的生命周期：运行期
 *
 * 于是在运行期间泛型已经失去约束性，通过运行期的反射可以绕过泛型约束
 */
public class 反射越过泛型检查 {
    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();
        list.add("123");
        list.add("345");

        // 使用反射添加 非泛型约束数据
        Class<? extends List> aClass = list.getClass();
        try {
            Method add = aClass.getMethod("add", Object.class);
            add.invoke(list, true); // 添加一个boolean类型的数据
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(list.toString());

    }
}
