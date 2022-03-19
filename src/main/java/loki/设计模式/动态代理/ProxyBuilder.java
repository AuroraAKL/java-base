package loki.设计模式.动态代理;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

public class ProxyBuilder {
    /**
     * @param targetClass 原对象 类
     * @param proxyClass  代理对象类
     * @return 要代理的接口对象
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public static Object getProxyIntance(Class<?> targetClass, Class<?> proxyClass)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        Object target = targetClass.newInstance(); // 通过类的默认构造函数创建实例
        Object hander = proxyClass.getConstructor(Object.class).newInstance(target);// 创建代理对象实例
        return Proxy.newProxyInstance(hander.getClass().getClassLoader(),
                target.getClass().getInterfaces(), (InvocationHandler) hander);// 生成代理对象
    }

    /**
     * @param targetClassName 原对象 类名
     * @param proxyClassName  代理对象 类名
     * @return 要代理的接口对象
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public static Object getProxyIntance(String targetClassName, String proxyClassName) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?> targetClazz = Class.forName(targetClassName);    // 通过类名获取类型
        Object target = targetClazz.newInstance();                // 通过类的默认构造函数创建实例
        Class<?> proxyClass = Class.forName(proxyClassName);
        Object hander = proxyClass.getConstructor(Object.class).newInstance(target);// 创建代理对象实例
        return Proxy.newProxyInstance(proxyClass.getClassLoader(),
                targetClazz.getInterfaces(), (InvocationHandler) hander);// 生成代理对象
    }
}
