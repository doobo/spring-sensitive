package com.github.doobo.fastjson;

import com.github.doobo.config.HandleType;
import com.github.doobo.config.SensitiveType;

import java.lang.annotation.*;

/**
 * @author doobo
 */
@Inherited
@Documented
@Target({ElementType.FIELD , ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DesensitizationParam {
    
    /*脱敏类型,默认电话号码*/
    SensitiveType type() default SensitiveType.MOBILE_PHONE;
    
    /*脱敏字段,默认phone*/
    String[] fields() default "phone";
    
    /*处理方式,默认字段相等匹配*/
    HandleType mode() default HandleType.DEFAULT;
    /**
     * 身份证和手机号前面保留几位
     */
    int idFront() default 3;

    /**
     * 身份证和手机号后面保留几位
     */
    int idBack() default 3;

    /**
     * 地址默认保留前几位
     */
    int addSize() default 8;

    /**
     * 自定义正则匹配规则
     */
    String regExp() default "";

    /**
     * 正则替换字符
     */
    String regStr() default "*";
    
}
