package com.github.doobo;

import com.alibaba.fastjson.JSON;
import com.github.doobo.config.SensitiveType;
import com.github.doobo.fastjson.DesensitizationParam;
import com.github.doobo.fastjson.DesensitizationParams;
import com.github.doobo.config.HandleType;
import com.github.doobo.undo.HyposensitizationParam;
import com.github.doobo.undo.HyposensitizationParams;
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
            @DesensitizationParam(type = SensitiveType.NULL, fields = {"id"}),
            @DesensitizationParam(type = SensitiveType.SELF, fields = {"address"}),
            @DesensitizationParam(type = SensitiveType.MOBILE_PHONE, fields = {"phone", "idCard"}),
            @DesensitizationParam(type = SensitiveType.BANK_CARD, fields = "$..bankCard", mode = HandleType.RGE_EXP),
            @DesensitizationParam(regExp = "(?<=\\w{2})\\w(?=\\w{1})", fields = "$[0].idCard2", mode = HandleType.RGE_EXP)
    })
    @GetMapping("fast")
    public List<UserDesensitization> sensitive(){
        return Arrays.asList(new UserDesensitization(), new UserDesensitization());
    }


    /**
     * 数据回填,不给argName默认取第一个参数
     * @param pt1
     * @param pt2
     */
    @HyposensitizationParams({
            @HyposensitizationParam(type = "card", fields = "bankCard"),
            @HyposensitizationParam(argName = "a", type = "string"),
            @HyposensitizationParam(argName = "pt0", type = "obj"),
            @HyposensitizationParam(argName = "pt1", type = "phone", fields = {"idCard","phone"}),
            @HyposensitizationParam(argName = "pt2", type = "reg", fields = {"$..id"}, mode = HandleType.RGE_EXP)
    })
    @GetMapping("undo")
    public String Hyposensitization(UserDesensitization pt1, UserSensitive pt2
            , String a, SingleObj pt0){
        return JSON.toJSONString(Arrays.asList(pt1, pt2, a, pt0));
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
