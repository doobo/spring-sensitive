package com.github.doobo.fastjson;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.github.doobo.config.SensitiveInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SimpleValueFilter implements ValueFilter {

    private final Map<String, DesensitizationAnnotation> map = new HashMap<>();

    /**
     * fastJson字符串匹配
     * @param desensitizationController
     */
    public SimpleValueFilter(DesensitizationController desensitizationController) {
        for (DesensitizationAnnotation desensitization : desensitizationController.value()) {
            if(desensitization == null || desensitization.type() != HandleType.DEFAULT){
                continue;
            }
            String[] key = desensitization.key();
            for (String k : key){
                map.put(k, desensitization);
            }
        }
    }

    @Override
    public Object process(Object object, String name, Object value) {
        if(!(value instanceof String)){
            return value;
        }
        String valueStr = (String) value;
        DesensitizationAnnotation sensitiveInfo = map.get(name);
        if(sensitiveInfo == null){
            return value;
        }
        try {
            //有正则优先使用正则
            if(StringUtils.isNotBlank(sensitiveInfo.regExp())){
                return valueStr.replaceAll(sensitiveInfo.regExp(), sensitiveInfo.regStr());
            }
            switch (sensitiveInfo.value()) {
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
                    return value;
                }
            }
        }catch (Exception e){
            log.error("脱敏数据处理异常", e);
        }
        return value;
    }
}