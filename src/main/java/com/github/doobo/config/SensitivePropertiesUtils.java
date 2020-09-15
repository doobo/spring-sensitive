package com.github.doobo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class SensitivePropertiesUtils {

    private static SensitiveProperties INSTANCE;
    
    public static SensitiveProperties getInstance(){
        if(INSTANCE == null) {
            throw new IllegalArgumentException("SensitiveProperties is Undefined");
        }
        return INSTANCE;
    }
    
    @Autowired
    public void setSensitiveProperties(SensitiveProperties sensitiveProperties) {
        INSTANCE = sensitiveProperties;
    }
}
