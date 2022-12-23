package com.github.doobo.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class SensitivePropertiesConfig {

    @Bean
    @Primary
    @ConfigurationProperties("sensitive")
    @ConditionalOnProperty(name = "sensitive.startConfig", havingValue = "true")
    @ConditionalOnMissingBean(name = "sensitiveProperties")
    public SensitiveProperties sensitiveProperties() {
        return new SensitiveProperties();
    }

    /**
     * 获取okhttp配置
     */
    public static SensitiveProperties getSensitiveProperties() {
        return SensitiveSpringUtil.getBean(SensitiveProperties.class);
    }
}
