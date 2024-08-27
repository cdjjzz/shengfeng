package com.src.shengfeng;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;

public class AnnotatedTypeExample {

    @Deprecated
    public static void main(String[] args) {
        Class<MyClass> clazz = MyClass.class;
        
        // 获取类的 AnnotatedType
        AnnotatedType annotatedType = clazz.getAnnotatedSuperclass();
        
        // 获取类的字段并检查其 AnnotatedType
        try {
            Field field = clazz.getDeclaredField("myField");
            
            // 获取字段的 AnnotatedType
            AnnotatedType fieldAnnotatedType = field.getAnnotatedType();
            
            // 检查字段是否有 @Deprecated 注解
            boolean deprecated = fieldAnnotatedType.isAnnotationPresent(Deprecated.class);
            if (deprecated) {
                System.out.println("The field is deprecated.");
            } else {
                System.out.println("The field is not deprecated.");
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    
    @Deprecated
    private static class MyClass {
        @Deprecated
        private String myField;
    }
}