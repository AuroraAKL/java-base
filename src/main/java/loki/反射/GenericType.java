package loki.反射;


import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 泛型
 * 1. 动态获取泛型
 * 2. 获取泛型类型
 */
public class GenericType {

    public static Map<List<String>, Integer> func(List<List<StringBuilder>> listListSB, Map<Integer, String> mapIS, String str) {
        return null;
    }

    public static void main(String[] args) throws NoSuchMethodException {

        final Method method = GenericType.class.getMethod("func", new Class[]{List.class, Map.class, String.class});

        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Parameter[] parameters = method.getParameters();

        System.out.println("-------------------------- parameterTypes ----------- ");
        System.out.println(Arrays.toString(parameterTypes));
        for (Class<?> parameterType : parameterTypes) {
            final TypeVariable<? extends Class<?>>[] typeParameters = parameterType.getTypeParameters();
            for (TypeVariable<? extends Class<?>> typeParameter : typeParameters) {
                System.out.println(typeParameter.getGenericDeclaration());
            }
        }

        System.out.println("-------------------------- parameters ---------------");
        System.out.println(Arrays.toString(parameters));

        for (Parameter parameter : parameters) {
            System.out.println("parameterType: " + parameter.getParameterizedType());
            final Type type = parameter.getParameterizedType();

            if (type instanceof ParameterizedType) {
                // 内部有泛型的参数: List<String> 这种
                final ParameterizedType parameterizedType =  (ParameterizedType) type;
                // actualType: [class java.lang.Integer, class java.lang.String]
                System.out.println("actualType: " + Arrays.toString(parameterizedType.getActualTypeArguments()));
            }
        }

        // 获取函数泛型返回参数
        final Type genericReturnType = method.getGenericReturnType();
        System.out.println(genericReturnType);
        final ParameterizedType returnType = (ParameterizedType) genericReturnType;
        final Type[] actualTypeArguments = returnType.getActualTypeArguments();
        System.out.println(Arrays.toString(actualTypeArguments));


    }
}
