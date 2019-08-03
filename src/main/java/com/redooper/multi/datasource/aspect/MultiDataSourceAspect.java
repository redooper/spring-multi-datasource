package com.redooper.multi.datasource.aspect;

import com.redooper.multi.datasource.annotation.LookupKey;
import com.redooper.multi.datasource.core.MultiDataSourceManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * @Auther: Jackie
 * @Date: 2019-08-03 17:20
 * @Description:
 */
@Aspect
public class MultiDataSourceAspect {

    @Pointcut("@annotation(com.redooper.multi.datasource.annotation.LookupKey) || @within(com.redooper.multi.datasource.annotation.LookupKey)")
    public void lookupKeyPointcut() {
    }

    @Around("com.redooper.multi.datasource.aspect.MultiDataSourceAspect.lookupKeyPointcut()")
    public Object lookupKeyAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        LookupKey lookupKey = AnnotationUtils.getAnnotation(method, LookupKey.class);
        if (lookupKey == null) {
            Class clazz = signature.getDeclaringType();
            lookupKey = AnnotationUtils.findAnnotation(clazz, LookupKey.class);
        }

        try (MultiDataSourceManager multiDataSourceManager = new MultiDataSourceManager(lookupKey.value())) {
            return pjp.proceed();
        }
    }
}
