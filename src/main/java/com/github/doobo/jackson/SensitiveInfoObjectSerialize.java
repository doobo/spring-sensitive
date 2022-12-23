package com.github.doobo.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.github.doobo.config.SensitivePropertiesConfig;
import com.github.doobo.config.SensitiveType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;

import static com.github.doobo.config.ClassUtils.isBaseType;


/**
 * jackson的脱敏实现
 * @author doobo
 */
@Slf4j
public class SensitiveInfoObjectSerialize extends JsonSerializer<Object> implements ContextualSerializer {

	private SensitiveType type;

	public SensitiveInfoObjectSerialize() {
	}

	public SensitiveInfoObjectSerialize(final SensitiveInfo sensitiveInfo) {
		this.type = sensitiveInfo.value();
	}

	@Override
	public void serialize(final Object s, final JsonGenerator jsonGenerator,
						  final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
		if(!SensitivePropertiesConfig.getSensitiveProperties().isEnableJackFilter()){
			jsonGenerator.writeObject(s);
		}
		try {
			if(this.type == SensitiveType.NULL && !isBaseType(s.getClass())){
				jsonGenerator.writeObject(null);
				return;
			}
			jsonGenerator.writeObject(s);
		}catch (Exception e){
			log.error("脱敏数据处理异常", e);
			jsonGenerator.writeObject(s);
		}
	}

	@Override
	public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
											  final BeanProperty beanProperty) throws JsonMappingException {
		// 为空直接跳过
		if (beanProperty != null) {
			if(!SensitivePropertiesConfig.getSensitiveProperties().isEnableJackFilter()){
				return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
			}
			// 非 String 类直接跳过
			if (Objects.equals(beanProperty.getType().getRawClass(), Object.class)) {
				SensitiveInfo sensitiveInfo = beanProperty.getAnnotation(SensitiveInfo.class);
				if (sensitiveInfo == null) {
					sensitiveInfo = beanProperty.getContextAnnotation(SensitiveInfo.class);
				}
				if (sensitiveInfo != null && sensitiveInfo.value() == SensitiveType.NULL) {
					return new SensitiveInfoObjectSerialize(sensitiveInfo);
				}
			}
			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}
		return serializerProvider.findNullValueSerializer(null);
	}
}
