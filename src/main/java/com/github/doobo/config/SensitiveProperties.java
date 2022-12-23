package com.github.doobo.config;

import lombok.Data;

@Data
public class SensitiveProperties {

    //是否启用fastJson脱敏
    private boolean enableFastFilter;
    
    //是否启用Jackson脱敏
    private boolean enableJackFilter;
    
    //是否启用数据填充
    private boolean enableUndoFilter;
}
