package com.github.doobo.jackson;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.doobo.config.SensitiveType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 脱敏AOP注解
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveInfoSerialize.class)
public @interface SensitiveInfo {
	SensitiveType value() default SensitiveType.RGE_EXP;

	/**
	 * 身份证和手机号前面保留几位
	 * @return
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

	/**
	 * 自定义处理标识
	 */
	String tag() default "self";
}
