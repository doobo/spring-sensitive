package com.github.doobo.undo;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HyposensitizationParams {
    /**
     * 反脱敏参数
     */
    HyposensitizationParam[] value();
}
