package com.github.doobo.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.github.doobo.config.SensitiveInfoUtils;
import com.github.doobo.config.SensitiveType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * jackson的脱敏实现
 * @author doobo
 */
@Slf4j
public class SensitiveInfoSerialize extends JsonSerializer<String> implements ContextualSerializer {

	private SensitiveType type;

	private SensitiveInfo sensitiveInfo;

	public SensitiveInfoSerialize() {
	}

	public SensitiveInfoSerialize(final SensitiveInfo sensitiveInfo) {
		this.type = sensitiveInfo.value();
		this.sensitiveInfo = sensitiveInfo;
	}

	@Override
	public void serialize(final String s, final JsonGenerator jsonGenerator,
						  final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
		try {
			//有正则优先使用正则
			if(StringUtils.isNotBlank(sensitiveInfo.regExp())){
				jsonGenerator.writeString(s.replaceAll(sensitiveInfo.regExp(), sensitiveInfo.regStr()));
				return;
			}
			switch (this.type) {
				case CHINESE_NAME: {
					jsonGenerator.writeString(SensitiveInfoUtils.chineseName(s));
					break;
				}
				case ID_CARD:
				case MOBILE_PHONE: {
					jsonGenerator.writeString(SensitiveInfoUtils.idCardNum(s, sensitiveInfo.idFront(), sensitiveInfo.idBack()));
					break;
				}
				case FIXED_PHONE: {
					jsonGenerator.writeString(SensitiveInfoUtils.fixedPhone(s));
					break;
				}
				case PASSWORD: {
					jsonGenerator.writeString(SensitiveInfoUtils.password(s));
					break;
				}
				case ADDRESS: {
					jsonGenerator.writeString(SensitiveInfoUtils.address(s, sensitiveInfo.addSize()));
					break;
				}
				case EMAIL: {
					jsonGenerator.writeString(SensitiveInfoUtils.email(s));
					break;
				}
				case BANK_CARD: {
					jsonGenerator.writeString(SensitiveInfoUtils.bankCard(s));
					break;
				}
				case SHOPS_CODE: {
					jsonGenerator.writeString(SensitiveInfoUtils.shopsCode(s));
					break;
				} default:{
					jsonGenerator.writeString(s);
				}
			}
		}catch (Exception e){
			log.error("脱敏数据处理异常", e);
		}
	}

	@Override
	public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
											  final BeanProperty beanProperty) throws JsonMappingException {
		if (beanProperty != null) {
			// 为空直接跳过
			if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
				// 非 String 类直接跳过
				SensitiveInfo sensitiveInfo = beanProperty.getAnnotation(SensitiveInfo.class);
				if (sensitiveInfo == null) {
					sensitiveInfo = beanProperty.getContextAnnotation(SensitiveInfo.class);
				}
				if (sensitiveInfo != null) {
					// 如果能得到注解,就将注解的 value 传入 SensitiveInfoSerialize
					return new SensitiveInfoSerialize(sensitiveInfo);
				}
			}
			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}
		return serializerProvider.findNullValueSerializer(null);
	}
}
