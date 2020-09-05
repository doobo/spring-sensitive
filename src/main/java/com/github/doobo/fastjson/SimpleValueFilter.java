package com.github.doobo.fastjson;

import com.alibaba.fastjson.serializer.ValueFilter;
import lombok.extern.slf4j.Slf4j;

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
        DesensitizationAnnotation sensitiveInfo = map.get(name);
        return DesensitizationAop.handlerDesensitization(sensitiveInfo, valueStr);
    }
}