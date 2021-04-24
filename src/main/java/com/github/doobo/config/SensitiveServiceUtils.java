package com.github.doobo.config;

import com.github.doobo.service.AbstractSensitiveService;
import com.github.doobo.service.SensitiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 脱敏服务工具类
 */
@Component
public class SensitiveServiceUtils {

    /**
     * 脱敏工具
     */
    private static SensitiveService sensitiveService;

    @Autowired(required = false)
    public void setSensitiveService(SensitiveService sensitiveService) {
        SensitiveServiceUtils.sensitiveService = sensitiveService;
    }

    /**
     * 获取脱敏工具类
     */
    public static SensitiveService getSensitiveService() {
        if(SensitiveServiceUtils.sensitiveService == null){
            SensitiveServiceUtils.sensitiveService = new AbstractSensitiveService() {};
        }
        return SensitiveServiceUtils.sensitiveService;
    }
}
