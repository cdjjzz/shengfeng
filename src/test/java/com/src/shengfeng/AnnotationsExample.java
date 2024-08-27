package com.src.shengfeng;

import java.lang.annotation.*;
import java.lang.reflect.Method;


@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {
    String value();
}
// 定义一个运行时可见的注解

public class AnnotationsExample {


    public @MyAnnotation(value = "1231") String myMethod(
            @MyAnnotation(value = "World") String param1, @MyAnnotation(value = "Java") String param2) {
        // 方法体
        return "Hello World";
    }

    public static void main(String[] args) throws NoSuchMethodException {
        Method method = AnnotationsExample.class.getMethod("myMethod", String.class,String.class);
        
        // 获取方法上的 MyAnnotation 注解
        MyAnnotation methodAnnotation = method.getAnnotation(MyAnnotation.class);
        System.out.println("Method Annotation: " + methodAnnotation.value());
        
        // 获取方法参数上的注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        
        // 遍历参数注解
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                if (annotation instanceof MyAnnotation) {
                    MyAnnotation myAnnotation = (MyAnnotation) annotation;
                    System.out.println("Parameter " + i + ": " + myAnnotation.value());
                }
            }
        }
        
        // 获取方法的返回类型
        Class<?> returnType = method.getReturnType();
        MyAnnotation returnTypeAnnotation = returnType.getAnnotation(MyAnnotation.class);
        System.out.println("Return Type Annotation: " + returnTypeAnnotation);
        
        // 获取方法声明的所有注解
        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
        for (Annotation declaredAnnotation : declaredAnnotations) {
            System.out.println("Declared Annotation: " + declaredAnnotation);
        }
        
        // 获取方法声明的所有指定类型的注解
        MyAnnotation[] declaredAnnotationsByType = method.getDeclaredAnnotationsByType(MyAnnotation.class);
        for (MyAnnotation declaredAnnotationByType : declaredAnnotationsByType) {
            System.out.println("Declared Annotation By Type: " + declaredAnnotationByType.value());
        }
    }
}