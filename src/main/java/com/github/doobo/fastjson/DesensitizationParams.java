package com.github.doobo.fastjson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DesensitizationParams {

    /**
     * 脱敏自定义字段
     */
    DesensitizationParam[] value();

}
