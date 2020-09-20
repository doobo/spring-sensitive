package com.github.doobo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDesensitization {

	String name = "张三";

	String idCard = "430524202012120832";

	String idCard2 = "430524202012120832";

	String phone = "1234567890";

	String ext = "0739-8888888";

	String address = "湖南省长沙市高新区岳麓大道芯城科技园";

	String address2 = "湖南省";

	String bankCard = "622260000027736298837";
	
	//fastJson过滤不支持基本类型int,long等
	Long id = 123456L;
}
