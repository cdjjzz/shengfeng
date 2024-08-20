package com.src.shengfeng.secure;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class SensitiveAspect {
    public SensitiveAspect() {
    }

    @Pointcut("@annotation(com.eyedsion.his.secure.Desensitization)")
    public void getPoint() {
    }

    @Around("getPoint()")
    public Object sensitiveClass(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Desensitization desensitization = method.getAnnotation(Desensitization.class);
        return desensitization != null && desensitization.enable() ? this.sensitiveFormat(joinPoint) : joinPoint.proceed();
    }

    public Object sensitiveFormat(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.proceed();
        FieldSensitiveUtil.dealNode(obj);
        return obj;
    }
}