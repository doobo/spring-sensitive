package com.github.doobo.fastjson;

import java.lang.annotation.*;


@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DesensitizationParams {

    /**
     * 脱敏自定义字段
     */
    DesensitizationParam[] value();

}
