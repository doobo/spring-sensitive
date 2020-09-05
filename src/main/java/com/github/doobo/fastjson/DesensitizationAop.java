package com.github.doobo.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.doobo.config.SensitiveInfoUtils;
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

@Slf4j
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
        //加了正则处理
        List<DesensitizationAnnotation> ds = getRexExpDes(desensitizationController);
        filterRegExpValue(ds, returnValue);
        String rs = JSON.toJSONString(returnValue, new SimpleValueFilter(desensitizationController), SerializerFeature.WriteMapNullValue);
        throw new FastJsonCustomException(JSON.parse(rs));
    }

    /**
     * 获取正则相关的注解
     * @param desensitizationController
     */
    private List<DesensitizationAnnotation> getRexExpDes(DesensitizationController desensitizationController){
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
     * 正则过滤匹配的值
     * @param ds
     * @param returnValue
     */
    private void filterRegExpValue(List<DesensitizationAnnotation> ds, Object returnValue){
        if(ds == null || ds.isEmpty()){
            return;
        }
        for(DesensitizationAnnotation item : ds){
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
     * 处理单个脱敏
     * @param sensitiveInfo
     * @param valueStr
     */
    public static String handlerDesensitization(DesensitizationAnnotation sensitiveInfo, String valueStr){
        if(sensitiveInfo == null){
            return valueStr;
        }
        try {
            //有正则优先使用正则
            if(StringUtils.isNotBlank(sensitiveInfo.regExp())){
                return valueStr.replaceAll(sensitiveInfo.regExp(), sensitiveInfo.regStr());
            }
            switch (sensitiveInfo.type()) {
                case CHINESE_NAME: {
                    return SensitiveInfoUtils.chineseName(valueStr);
                }
                case ID_CARD:
                case MOBILE_PHONE: {
                    return SensitiveInfoUtils.idCardNum(valueStr, sensitiveInfo.idFront(), sensitiveInfo.idBack());
                }
                case FIXED_PHONE: {
                    return SensitiveInfoUtils.fixedPhone(valueStr);
                }
                case PASSWORD: {
                    return SensitiveInfoUtils.password(valueStr);
                }
                case ADDRESS: {
                    return SensitiveInfoUtils.address(valueStr, sensitiveInfo.addSize());
                }
                case EMAIL: {
                    return SensitiveInfoUtils.email(valueStr);
                }
                case BANK_CARD: {
                    return SensitiveInfoUtils.bankCard(valueStr);
                }
                case SHOPS_CODE: {
                    return SensitiveInfoUtils.shopsCode(valueStr);
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