package loki.设计模式.动态代理.cglib.intercepter;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
/**
 * 定义一个代理实现类
 * @author Rnti
 *
 * @param <T>
 */
public class HelloClassInterceptor<T> implements MethodInterceptor {// 拦截器
	public T target;
	/**
	 * 产生一个代理对象
	 * @param targetObject 代理对象
	 * @return 生成的代理类对象
	 */
	@SuppressWarnings("unchecked")
	public T createProxyInstance(T targetObject){
		this.target = targetObject;
		Enhancer enhancer = new Enhancer();//用于生成代理对象
		enhancer.setSuperclass(this.target.getClass()); //设置目标类为代理对象的父类
		enhancer.setCallback(this); //设置回调对象为本身
		return (T)enhancer.create(); //生成一个代理类对象
	}
	@Override
	public Object intercept(Object obj, Method method,
							Object[] argvs, MethodProxy methodProxy) throws Throwable {
		System.out.println("begin time -----> "+ System.currentTimeMillis());
		Object returnObj = methodProxy.invoke(target, argvs);			// 调用原对象方法
		System.out.println("end time -----> "+ System.currentTimeMillis());
		return returnObj;
	}
}

