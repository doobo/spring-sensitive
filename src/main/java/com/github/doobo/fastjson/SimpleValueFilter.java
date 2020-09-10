package com.github.doobo.fastjson;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.github.doobo.config.HandleType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * fastJson序列化时进行脱敏示例
 */
@Slf4j
@Deprecated
public class SimpleValueFilter implements ValueFilter {

    private final Map<String, DesensitizationParam> map = new HashMap<>();

    /**
     * fastJson字符串匹配
     * @param desensitizationController
     */
    public SimpleValueFilter(DesensitizationParams desensitizationController) {
        for (DesensitizationParam desensitization : desensitizationController.value()) {
            if(desensitization == null || desensitization.mode() != HandleType.DEFAULT){
                continue;
            }
            String[] key = desensitization.fields();
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
        DesensitizationParam sensitiveInfo = map.get(name);
        return DesensitizationAop.handlerDesensitization(sensitiveInfo, valueStr);
    }
}