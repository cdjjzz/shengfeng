package com.src.shengfeng.secure;


import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DesensitizationProp {
    SensitiveTypeEnum value();

    int preLength() default 0;

    int sufLength() default 0;
}