package loki.jvm;


import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class TestClassLoader {

    public static class LokiClass {

    }


    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        String classPath = "D:\\classes\\loki\\LokiClass.class";
        //类的全称
        String packageNamePath = "loki.LokiClass";
        LokiClassLoader classLoader = new LokiClassLoader(classPath);
        Class<?> clazz = classLoader.loadClass(packageNamePath);
        Object o = clazz.newInstance();

        System.out.println("类加载器是:" + clazz.getClassLoader());

        Method method = clazz.getDeclaredMethod("main", String[].class);
        method.invoke(o, (Object) new String[]{});

        System.out.println(clazz.getName());

        System.out.println(Throwable.class.getClassLoader());



        //

//        ManagementFactory.
        Throwable throwable = new Throwable();
        throwable.printStackTrace();
    }


    public static void testClass() {
        // 只有被同一个类加载器实例加载并且文件名相同的class文件才被认为是同一个class.
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        ClassLoader classLoader1 = TestClassLoader.class.getClassLoader();
        ClassLoader classLoader2 = classLoader1.getParent();
        ClassLoader classLoader3 = classLoader2.getParent();

        System.out.println(classLoader);
        System.out.println(classLoader1);
        System.out.println(classLoader2);
        System.out.println(classLoader3);
    }
}
