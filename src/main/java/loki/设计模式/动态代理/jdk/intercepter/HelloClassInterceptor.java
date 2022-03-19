package loki.设计模式.动态代理.jdk.intercepter;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 定义拦截器（代理对象
 *
 * @param <T>
 * @author Rnti
 */
public class HelloClassInterceptor<T> implements InvocationHandler {
    private T target;

    /**
     * @param target 需要代理的对象
     */
    public HelloClassInterceptor(T target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object obj, Method method, Object[] arg) throws Throwable {
        System.out.println("before ----" + System.currentTimeMillis());
        Object re = method.invoke(target, arg);                        // 调用原对象方法
        System.out.println("after ----" + System.currentTimeMillis());
        return re;
    }// 拦截器

}

