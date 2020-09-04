package com.github.doobo.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Component
@Aspect
public class DesensitizationAop {

    @AfterReturning(value = "@annotation(DesensitizationController)", returning = "returnValue")
    public Object before(JoinPoint joinPoint, Object returnValue) throws Throwable {

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DesensitizationController desensitizationController = AnnotationUtils.getAnnotation(method , DesensitizationController.class);
        if(desensitizationController == null){
            return returnValue;
        }
        String rs = JSON.toJSONString(returnValue, new SimpleValueFilter(desensitizationController),SerializerFeature.WriteMapNullValue);
        throw new FastJsonCustomException(JSON.parse(rs));
    }
}