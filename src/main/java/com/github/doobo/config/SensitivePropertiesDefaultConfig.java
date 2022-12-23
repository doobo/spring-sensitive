package com.github.doobo.config;


import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:spring-sensitive-default.yml"
        , encoding = "utf-8", factory = YamlPropertySourceFactory.class)
public class SensitivePropertiesDefaultConfig {

    @Bean
    @ConfigurationProperties("sensitive.default")
    @ConditionalOnMissingBean(name = "defaultSensitiveProperties")
    public SensitiveProperties defaultSensitiveProperties() {
        return new SensitiveProperties();
    }
}
