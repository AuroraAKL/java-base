package loki.设计模式.动态代理.cglib;

/**
 * ԭ����
 * @author Rnti
 *
 */
public class HelloClass{
	int a;
	public void hello() {
		System.out.println("hello" + this);
	}
	@Override
	public String toString() {
		return this.a + " HelloClass";
	}
}

