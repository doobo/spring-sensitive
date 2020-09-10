package com.github.doobo.undo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.github.doobo.config.HandleType;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.github.doobo.config.SensitiveInfoUtils.CONF;
import static java.util.stream.Collectors.toList;

/**
 * @author qpc
 */
@Order(1)
@Slf4j
@Aspect
@Component
public class HyposensitizationAop {

    /**
     * 用于定位寻找注解
     */
    @Pointcut("@annotation(HyposensitizationParams)")
    public void methodCachePointcut() {
    }

    @Around("methodCachePointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        //如果未定义填充监听,不执行相关操作
        if(!UndoObserved.isObserver()){
            return joinPoint.proceed();
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        HyposensitizationParams params = method.getAnnotation(HyposensitizationParams.class);
        if(params == null){
            return joinPoint.proceed();
        }
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return joinPoint.proceed();
        }
        String[] parameterNames = methodSignature.getParameterNames();
        if(parameterNames == null || parameterNames.length == 0){
            return joinPoint.proceed();
        }
        List<HyposensitizationParam> ls = Arrays.stream(params.value()).collect(toList());
        for(HyposensitizationParam item : ls){
            if(item.argName().isEmpty()){
                UndoVO vo = convertVO(item, args[0]);
                if(vo == null){
                    continue;
                }
                UndoObserved.getInstance().sendResult(vo);
                continue;
            }
            UndoVO vo = convertVO(item, queryArg(item.argName(), args, parameterNames));
            if(vo == null){
                continue;
            }
            UndoObserved.getInstance().sendResult(vo);
        }
        return joinPoint.proceed();
    }

    /**
     * 获取入参
     * @param key
     * @param args
     * @param names
     */
    private Object queryArg(String key, Object[] args, String[] names){
        if(key == null || args == null || names == null || args.length != names.length){
            return null;
        }
        for(int i =0 ,len=names.length;i < len ;i++) {
            if (key.equals(names[i])) {
                return args[i];
            }
        }
        return null;
    }

    /**
     * 判断是基本类还是封装类
     * .isPrimitive()是用来判断是否是基本类型的：void.isPrimitive() //true;
     * @param clz
     */
    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * UndoVO对象转换
     * @param param
     * @param obj
     */
    private UndoVO convertVO(HyposensitizationParam param, Object obj){
        if(param.fields().length == 0){
            return null;
        }
        if(obj == null){
            return null;
        }
        if(isWrapClass(obj.getClass())){
            return null;
        }
        if(obj instanceof String){
            return null;
        }
        UndoVO vo = new UndoVO();
        vo.setArgName(param.argName())
                .setType(param.type())
                .setFields(param.fields())
                .setMode(param.mode())
                .setObj(obj);
        String[] fs = param.fields();
        if(param.mode() == HandleType.DEFAULT){
            fs = Arrays.stream(fs).map(m -> String.format("$..%s", m)).toArray(String[]::new);
        }
        List<String> regs = new ArrayList<>();
        for(String key : fs){
            List<String> path = JsonPath.using(CONF).parse(JSON.toJSONString(obj)).read(key);
            if(path == null || path.isEmpty()){
                continue;
            }
            regs.addAll(path);
        }
        vo.setRegFields(regs);
        return vo;
    }
}
