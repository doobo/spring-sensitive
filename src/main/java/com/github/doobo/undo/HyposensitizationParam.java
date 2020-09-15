package com.github.doobo.undo;

import com.github.doobo.config.HandleType;

import java.lang.annotation.*;

/**
 * @author doobo
 */
@Inherited
@Documented
@Target({ElementType.FIELD , ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HyposensitizationParam {

    /**
     * 待填充对象的名称,默认第一个参数
     */
    String argName() default "";

    /**
     * 脱敏标签,方便标识特定类型,进行相关处理
     */
    String type() default "";
    
    /*反脱敏字段,可正则,默认字段名匹配*/
    String[] fields() default "";

    /*处理方式,默认字段相等匹配*/
    HandleType mode() default HandleType.DEFAULT;
}
