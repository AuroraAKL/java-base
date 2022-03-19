package loki.设计模式.动态代理.jdk;

/**
 * ԭ����
 * �̳�Ҫ����Ľӿ�
 * @author Rnti
 *
 */
public class HelloClass implements HelloInterface{
	int a;
	@Override
	public void hello() {
		System.out.println("hello" + this);
	}
	@Override
	public String toString() {
		return this.a + " HelloClass";
	}
}

