package loki.asm.create;

public class MyClassLoader extends ClassLoader {
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if ("simple.HelloWorld".equals(name)) {
            byte[] bytes = ASMBuild.build();
            Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
            return clazz;
        }
        return super.findClass(name);
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        MyClassLoader myClassLoader = new MyClassLoader();
        Class<?> aClass = myClassLoader.findClass("simple.HelloWorld");
        Object o = aClass.newInstance();
        System.out.println(o);
    }
}
