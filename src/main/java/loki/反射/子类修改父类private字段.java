package loki.反射;

import java.lang.reflect.Field;

public class 子类修改父类private字段 {

    public static class A {
        private String nameA = "nameA";
        private String nameB = "nameA B";
        @Override
        public String toString() {
            return "A{" +
                    "nameA='" + nameA + '\'' +
                    "nameB='" + nameB + '\'' +
                    '}';
        }
    }

    public static class B extends A {
        private String nameB = "nameB B";
        public void setName(String name) throws NoSuchFieldException, IllegalAccessException {
            Field field = A.class.getDeclaredField("nameA");
            field.setAccessible(true);
            field.set(this, name);
        }
        @Override
        public String toString() {
            return super.toString();
        }
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        B b = new B();
        b.setName("nameB");
        System.out.println(b); // A{nameA='nameB'nameB='nameA B'}
    }

}
