package com.github.doobo;

import com.github.doobo.config.SensitiveType;
import com.github.doobo.fastjson.DesensitizationParam;
import com.github.doobo.fastjson.DesensitizationParams;
import com.github.doobo.fastjson.HandleType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class IndexController {

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

    /**
     * 基于jackson的数据脱敏
     * @return
     */
    @GetMapping
    public List<UserSensitive> jackson(){
        return Collections.singletonList(new UserSensitive());
    }
}
