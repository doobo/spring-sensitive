# spring-sensitive

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

> 基于springboot的数据脱敏，实现了"模型类"和"AOP注解"两种方法,选择其中一种即可
* 选择任意一种即可,若同时使用，先执行controller层的脱敏，再执行模型类里面的脱敏(返回视图默认Jackson)
* 基于AOP实现的，也可用于其它spring方法上
## 如何使用
```
 <dependency>
   <groupId>com.github.doobo</groupId>
   <artifactId>spring-sensitive</artifactId>
   <version>1.0</version>
 </dependency>
```

## 使用fastJson脱敏
```
/**
 * 基于fastJson的数据脱敏
 */
@DesensitizationParams({
    @DesensitizationParam(type = SensitiveType.MOBILE_PHONE, fields = {"phone", "idCard"}),
    @DesensitizationParam(type = SensitiveType.BANK_CARD, fields = "$..bankCard", mode = HandleType.RGE_EXP),
    @DesensitizationParam(regExp = "(?<=\\w{2})\\w(?=\\w{1})", fields = "$[0].idCard2", mode = HandleType.RGE_EXP)
})
@GetMapping("fast")
public List<UserDesensitization> sensitive(){
    return Arrays.asList(new UserDesensitization(), new UserDesensitization());
}
```

## 使用jackson脱敏,基于jackson的JsonSerialize实现
```java
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
```

### 方法调用输出
```
@SpringBootTest
public class ApplicationTests {

    /**
     * jackson脱敏测试
     * @throws JsonProcessingException
     */
    @Test
    void testSensitive() throws JsonProcessingException {
        UserSensitive user = new UserSensitive();
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(user);
        System.out.println(str);
    }
    
}
```

## 脱敏结果
![脱敏结果](https://i.loli.net/2020/09/04/W2sUPFdeSBXpm87.png)