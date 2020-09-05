package com.github.doobo;

import com.github.doobo.config.SensitiveType;
import com.github.doobo.fastjson.DesensitizationAnnotation;
import com.github.doobo.fastjson.DesensitizationController;
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
     * @return
     */
    @DesensitizationController({
        @DesensitizationAnnotation(type = SensitiveType.MOBILE_PHONE, fields = {"phone", "idCard"}),
        @DesensitizationAnnotation(type = SensitiveType.BANK_CARD, fields = "$[%d].bankCard", mode = HandleType.RGE_EXP),
        @DesensitizationAnnotation(type = SensitiveType.BANK_CARD, fields = "$[0].idCard2", mode = HandleType.RGE_EXP)
    })
    @GetMapping("fast")
    public List<UserSensitive> sensitive(){
        return Arrays.asList(new UserSensitive(), new UserSensitive());
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
