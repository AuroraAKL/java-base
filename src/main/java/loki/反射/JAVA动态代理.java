package loki.反射;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JAVA动态代理 {
    static interface Interface {
        public void add();
        public void delete();
    }
    static class Clazz implements Interface {
        @Override
        public void add(){
            System.out.println("add");
        }
        @Override
        public void delete(){
            System.out.println("delete");
        }
    }
    public static void main(String[] args) {
        Clazz cl = new Clazz();
        Interface in = (Interface) Proxy.newProxyInstance(Clazz.class.getClassLoader(), Clazz.class.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("before!");
                Object returnValue = method.invoke(cl, args);// 执行原方法
                System.out.println("after!");
                return null;
            }
        });
        in.add();
        in.delete();

    }
}
