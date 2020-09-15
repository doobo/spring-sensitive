package com.github.doobo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * 基本配置类
 */
@Configuration
@ComponentScans({@ComponentScan("com.github.doobo.fastjson")
        , @ComponentScan("com.github.doobo.undo")
        , @ComponentScan("com.github.doobo.config")})
public class SensitiveAutoConfiguration {
}
