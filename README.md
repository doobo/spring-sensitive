# spring-sensitive

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

> 基于springboot的数据脱敏，实现了"模型类"和"AOP注解"两种方法,选择其中一种即可
* 选择任意一种即可,若同时使用，先执行controller层的脱敏，再执行模型类里面的脱敏(返回视图默认Jackson)
* 基于AOP实现的方法，也可用于其它spring方法上，如无效，记得引入spring的aop包
* 脱敏了的数据,前端传回来后，可进行数据回填，参考下面的"数据回写"部分，基于fastJson实现
## 如何使用
```
 <dependency>
   <groupId>com.github.doobo</groupId>
   <artifactId>spring-sensitive</artifactId>
   <version>1.2</version>
 </dependency>
```

## 使用fastJson脱敏
```
/**
 * 基于fastJson的数据脱敏
 */
@DesensitizationParams({
        @DesensitizationParam(type = SensitiveType.NULL, fields = {"id","address"}),
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

	@SensitiveInfo(value = SensitiveType.NULL)
	String address2 = "湖南省";

	@SensitiveInfo(value = SensitiveType.BANK_CARD)
	String bankCard = "622260000027736298837";
	
	@SensitiveInfo(value = SensitiveType.NULL)
	Integer id = 654321;
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

## 数据回写
有些数据脱敏给前端后,传回给后台时,需要回填到入参里面去,如一些用户ID,手机号等信息
```
/**
 * IndexController.java
 * 数据回填,不给argName默认取第一个参数
 * @param pt1
 * @param pt2
 */
@HyposensitizationParams({
        @HyposensitizationParam(type = "card", fields = "bankCard"),
        @HyposensitizationParam(argName = "a", type = "string"),
        @HyposensitizationParam(argName = "pt1", type = "phone", fields = {"idCard","phone"}),
        @HyposensitizationParam(argName = "pt2", type = "reg", fields = {"$..address", "$.bankCard"}, mode = HandleType.RGE_EXP)
})
@GetMapping("undo")
public String Hyposensitization(UserDesensitization pt1, UserSensitive pt2, String a){
    return JSON.toJSONString(Arrays.asList(pt1, pt2, a));
}

//PtoUndoObserver.java
import com.github.doobo.undo.UndoObserver;
import com.github.doobo.undo.UndoVO;
import org.springframework.stereotype.Component;

@Component
public class PtoUndoObserver extends UndoObserver {

    /**
     * 继承观察者,可填充到方法的入参里面
     * @param vo
     */
    @Override
    public void undoValue(UndoVO vo) {
        synchronized (this) {
            if (vo.getType().equals("card")) {
                vo.undo("...1");
            }
            if (vo.getType().equals("phone")) {
                vo.undo("......2");
            }
            if (vo.getType().equals("reg")) {
                vo.undo(".........3");
            }
            if(vo.getType().equals("string")){
                vo.undo(4);
            }
        }
    }
}
```

## 全局配置是否启动相关功能
```yaml
sensitive:
  enableFastFilter: true
  enableJackFilter: true
  enableUndoFilter: true
```

## 脱敏结果
![脱敏结果](https://i.loli.net/2020/09/04/W2sUPFdeSBXpm87.png)

## 数据回写结果
![数据回写](https://i.loli.net/2020/09/10/DOfTpeR917X8YQ4.png)