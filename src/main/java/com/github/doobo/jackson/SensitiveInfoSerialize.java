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
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

import static com.github.doobo.config.SensitiveServiceUtils.getSensitiveService;

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
		if(!SensitivePropertiesConfig.getSensitiveProperties().isEnableJackFilter()){
			jsonGenerator.writeString(s);
		}
		try {
			//有正则优先使用正则
			if(StringUtils.isNotBlank(sensitiveInfo.regExp())){
				jsonGenerator.writeString(s.replaceAll(sensitiveInfo.regExp(), sensitiveInfo.regStr()));
				return;
			}
			switch (this.type) {
				case CHINESE_NAME: {
					jsonGenerator.writeString(getSensitiveService().chineseName(s));
					break;
				}
				case ID_CARD:
				case MOBILE_PHONE: {
					jsonGenerator.writeString(getSensitiveService().idCardNum(s, sensitiveInfo.idFront(), sensitiveInfo.idBack()));
					break;
				}
				case FIXED_PHONE: {
					jsonGenerator.writeString(getSensitiveService().fixedPhone(s));
					break;
				}
				case PASSWORD: {
					jsonGenerator.writeString(getSensitiveService().password(s));
					break;
				}
				case ADDRESS: {
					jsonGenerator.writeString(getSensitiveService().address(s, sensitiveInfo.addSize()));
					break;
				}
				case EMAIL: {
					jsonGenerator.writeString(getSensitiveService().email(s));
					break;
				}
				case BANK_CARD: {
					jsonGenerator.writeString(getSensitiveService().bankCard(s));
					break;
				}
				case SHOPS_CODE: {
					jsonGenerator.writeString(getSensitiveService().shopsCode(s));
					break;
				}
				case NULL: {
					jsonGenerator.writeString((String) null);
					break;
				}
				case SELF: {
					jsonGenerator.writeString(getSensitiveService().selfJacksonHandler(s, sensitiveInfo));
					break;
				}
				default:{
					jsonGenerator.writeString(s);
				}
			}
		}catch (Exception e){
			log.error("脱敏数据处理异常", e);
			jsonGenerator.writeString(s);
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
			SensitiveInfo sensitiveInfo = beanProperty.getAnnotation(SensitiveInfo.class);
			// 非 String 类直接跳过
			if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
				if (sensitiveInfo != null) {
					// 如果能得到注解,就将注解的 value 传入 SensitiveInfoSerialize
					return new SensitiveInfoSerialize(sensitiveInfo);
				}
			}
			if(sensitiveInfo != null && sensitiveInfo.value() == SensitiveType.NULL){
				return new SensitiveInfoObjectSerialize(sensitiveInfo);
			}
			return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
		}
		return serializerProvider.findNullValueSerializer(null);
	}
}
