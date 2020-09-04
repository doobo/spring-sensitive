package com.github.doobo;

import com.github.doobo.jackson.SensitiveInfo;
import com.github.doobo.config.SensitiveType;
import lombok.Data;

@Data
public class UserSensitive {

	@SensitiveInfo(value = SensitiveType.CHINESE_NAME)
	String name = "张三";

	@SensitiveInfo(value = SensitiveType.ID_CARD)
	String idCard = "430524202012120832";

	@SensitiveInfo(regExp = "(?<=\\w{3})\\w(?=\\w{4})")
	String idCard2 = "430524202012120832";

	@SensitiveInfo(value = SensitiveType.MOBILE_PHONE)
	String phone = "1234567890";

	@SensitiveInfo(value = SensitiveType.FIXED_PHONE)
	String ext = "0739-8888888";

	@SensitiveInfo(value = SensitiveType.ADDRESS)
	String address = "湖南省长沙市高新区岳麓大道芯城科技园";

	@SensitiveInfo(value = SensitiveType.ADDRESS)
	String address2 = "湖南省";

	@SensitiveInfo(value = SensitiveType.BANK_CARD)
	String bankCard = "6222600000";
}
