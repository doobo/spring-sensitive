package com.github.doobo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Data
@Component
@ConfigurationProperties(prefix = "sensitive")
public class SensitiveProperties {
    
    private static SensitiveProperties INSTANCE;
    
    @Resource
    private SensitiveProperties sensitiveProperties;

    //是否启用fastJson脱敏
    private boolean enableFastFilter;
    
    //是否启用Jackson脱敏
    private boolean enableJackFilter;
    
    //是否启用数据填充
    private boolean enableUndoFilter;
    
    @PostConstruct
    public void init(){
        INSTANCE = sensitiveProperties;
    }
    
    public static SensitiveProperties getInstance(){
        return INSTANCE;
    }
}
