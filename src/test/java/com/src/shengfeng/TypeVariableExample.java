package com.src.shengfeng;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.Method;

// 定义一个泛型方法
public class TypeVariableExample<T> extends ShengfengApplicationTests {

    public static <E extends Comparable<? super E>> void genericMethod(E obj) {
        // 方法体
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Method method = new TypeVariableExample<String>().
                getClass().getMethod("genericMethod", Comparable.class);
        
        // 获取方法的类型变量
        TypeVariable<?>[] typeVariables = method.getTypeParameters();
        
        // 输出类型变量信息
        for (TypeVariable<?> typeVariable : typeVariables) {
            System.out.println("Type Variable Name: " + typeVariable.getName());
            System.out.print("Bounds: ");
            for (Type bound : typeVariable.getBounds()) {
                System.out.print(bound.getTypeName() + " ");
            }
            System.out.println(typeVariable);
        }
    }
}