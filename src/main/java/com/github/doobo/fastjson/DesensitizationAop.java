package com.github.doobo.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.github.doobo.config.*;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.doobo.config.SensitiveServiceUtils.getSensitiveService;
import static com.github.doobo.config.SensitiveInfoUtils.CONF;

@Slf4j
@Component
@Aspect
public class DesensitizationAop {

    @AfterReturning(value = "@annotation(com.github.doobo.fastjson.DesensitizationParams)", returning = "returnValue")
    public Object before(JoinPoint joinPoint, Object returnValue) throws Throwable {
        if(!SensitivePropertiesUtils.getInstance().isEnableFastFilter()){
            return returnValue;
        }
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DesensitizationParams desensitizationController = AnnotationUtils.getAnnotation(method , DesensitizationParams.class);
        if(desensitizationController == null){
            return returnValue;
        }
        List<DesensitizationParam> ds = getDes(desensitizationController);
        try {
            return filterValue(ds, returnValue);
        }catch (Exception e){
            log.error("JSONPathError",e);
            throw new FastJsonCustomException(returnValue);
        }
    }

    /**
     * 获取所有注解
     * @param desensitizationController
     */
    private List<DesensitizationParam> getDes(DesensitizationParams desensitizationController){
        if(desensitizationController == null){
            return Collections.emptyList();
        }
        return Arrays.stream(desensitizationController.value()).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 获取正则相关的注解
     * @param desensitizationController
     */
    @Deprecated
    private List<DesensitizationParam> getRexExpDes(DesensitizationParams desensitizationController){
        if(desensitizationController == null){
            return Collections.emptyList();
        }
        return Arrays.stream(desensitizationController.value()).map(m->{
            if(m.mode() == HandleType.RGE_EXP){
                return m;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * fastJson正则过滤匹配的值
     * @param ds
     * @param returnValue
     */
    @Deprecated
    private void filterRegExpValue(List<DesensitizationParam> ds, Object returnValue){
        if(ds == null || ds.isEmpty()){
            return;
        }
        for(DesensitizationParam item : ds){
           if(item.mode() != HandleType.RGE_EXP){
               continue;
           }
           for(String reg : item.fields()){
               if(reg == null){
                   continue;
               }
               String exp = reg;
               if(reg.contains("[%d]")){
                   exp = reg.replace("[%d]",".");
               }
               if(!JSONPath.contains(returnValue, exp)){
                   continue;
               }
               Object os = JSONPath.eval(returnValue, exp);
               if(os instanceof ArrayList){
                   ArrayList<Object> ns = (ArrayList<Object>) os;
                   if(ns.isEmpty()){
                       continue;
                   }
                   for(int i = 0; i < ns.size(); i++){
                       Object tmp = ns.get(i);
                       if(!(tmp instanceof String)){
                           continue;
                       }
                       String key = String.format(reg, i);
                       String value = (String) tmp;
                       if(JSONPath.contains(returnValue, key)){
                           JSONPath.set(returnValue, key, handlerDesensitization(item, value));
                       }
                   }
                   continue;
               }
               //非字符串类型,不处理
               if(!(os instanceof String)){
                   continue;
               }
               String valueStr = (String) os;
               valueStr = handlerDesensitization(item, valueStr);
               JSONPath.set(returnValue, exp, valueStr);
           }
        }
    }

    /**
     *  JsonPath和FastJson过滤字段
     * @param ds
     * @param t
     * @param <T>
     */
    private <T> T filterValue(List<DesensitizationParam> ds, T t){
        if(ds == null || ds.isEmpty()){
            return t;
        }
        for(DesensitizationParam item : ds){
            String[] fs = item.fields();
            if(fs.length == 0){
                continue;
            }
            if(item.mode() == HandleType.DEFAULT){
                fs = Arrays.stream(fs).map(m -> String.format("$..%s", m)).toArray(String[]::new);
            }
            for(String key : fs){
                List<String> path;
                try {
                    path = JsonPath.using(CONF).parse(JSON.toJSONString(t)).read(key);
                }catch (PathNotFoundException err){
                    log.warn("PathNotFoundExceptionError,{}", key);
                    continue;
                }
                if(path == null || path.isEmpty()){
                    continue;
                }
                path.forEach(p->{
                    if(JSONPath.contains(t, p)){
                        Object value = JSONPath.eval(t, p);
                        //如果是NULL匹配,并且是封装类型,设置为空
                        if(item.type() == SensitiveType.NULL){
                            JSONPath.set(t, p,null);
                        }else if(value instanceof String){
                            JSONPath.set(t, p, handlerDesensitization(item, (String) value));
                        }
                    }
                });
            }
        }
        return t;
    }

    /**
     * 处理单个脱敏
     * @param sensitiveInfo
     * @param valueStr
     */
    public static String handlerDesensitization(DesensitizationParam sensitiveInfo, String valueStr){
        if(sensitiveInfo == null){
            return valueStr;
        }
        try {
            //是NULL类型,直接返回null
            if(sensitiveInfo.type() == SensitiveType.NULL){
                return null;
            }
            //有正则优先使用正则
            if(StringUtils.isNotBlank(sensitiveInfo.regExp())){
                return valueStr.replaceAll(sensitiveInfo.regExp(), sensitiveInfo.regStr());
            }
            switch (sensitiveInfo.type()) {
                case CHINESE_NAME: {
                    return getSensitiveService().chineseName(valueStr);
                }
                case ID_CARD:
                case MOBILE_PHONE: {
                    return getSensitiveService().idCardNum(valueStr, sensitiveInfo.idFront(), sensitiveInfo.idBack());
                }
                case FIXED_PHONE: {
                    return getSensitiveService().fixedPhone(valueStr);
                }
                case PASSWORD: {
                    return getSensitiveService().password(valueStr);
                }
                case ADDRESS: {
                    return getSensitiveService().address(valueStr, sensitiveInfo.addSize());
                }
                case EMAIL: {
                    return getSensitiveService().email(valueStr);
                }
                case BANK_CARD: {
                    return getSensitiveService().bankCard(valueStr);
                }
                case SHOPS_CODE: {
                    return getSensitiveService().shopsCode(valueStr);
                } default:{
                    return valueStr;
                }
            }
        }catch (Exception e){
            log.error("脱敏数据处理异常", e);
        }
        return valueStr;
    }
}