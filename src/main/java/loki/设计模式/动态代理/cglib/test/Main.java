package loki.设计模式.动态代理.cglib.test;


import loki.设计模式.动态代理.cglib.intercepter.HelloClassInterceptor;
import loki.设计模式.动态代理.jdk.HelloClass;

public class Main {
	public static void main(String[] args) {
        HelloClass cGsubject =
        		new HelloClassInterceptor<HelloClass>().createProxyInstance(new HelloClass());// ���ɴ���
        cGsubject.hello();
	}
}
