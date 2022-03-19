package loki.设计模式.动态代理.jdk.test;

import loki.设计模式.动态代理.ProxyBuilder;
import loki.设计模式.动态代理.jdk.intercepter.HelloClassInterceptor;
import loki.设计模式.动态代理.jdk.HelloClass;
import loki.设计模式.动态代理.jdk.HelloInterface;

import java.lang.reflect.InvocationTargetException;

public class Main {
	public static void main(String[] args)  {
		try {
			HelloInterface proxyInstance = (HelloInterface) ProxyBuilder.getProxyIntance("dynamic_proxy.jdk.HelloClass",
					"dynamic_proxy.jdk.intercepter.HelloClassInterceptor");
			proxyInstance.hello();
			
			proxyInstance = (HelloInterface) ProxyBuilder.getProxyIntance(HelloClass.class, HelloClassInterceptor.class);
			proxyInstance.hello();
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
}
