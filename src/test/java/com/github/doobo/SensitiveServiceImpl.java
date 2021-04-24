package com.github.doobo;

import com.github.doobo.fastjson.DesensitizationParam;
import com.github.doobo.jackson.SensitiveInfo;
import com.github.doobo.service.AbstractSensitiveService;
import org.springframework.stereotype.Service;

/**
 * 可自定义实现脱敏函数
 */
@Service
public class SensitiveServiceImpl extends AbstractSensitiveService {

    @Override
    public String idCardNum(String idCardNum, int front, int end) {
        return super.idCardNum(idCardNum, front, end);
    }

    @Override
    public String selfFastJsonHandler(String input, DesensitizationParam param) {
        if("self".equals(param.tag())){
            return "fastJsonSelfHandler:" + input;
        }
        return input;
    }

    @Override
    public String selfJacksonHandler(String input, SensitiveInfo param) {
        return "JacksonHandler:" + input;
    }
}
