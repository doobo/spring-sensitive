package com.github.doobo;

import com.alibaba.fastjson.JSON;
import com.github.doobo.undo.HyposensitizationParam;
import com.github.doobo.undo.HyposensitizationParams;
import org.springframework.stereotype.Service;

@Service
public class UndoService {
    
    @HyposensitizationParams({
            @HyposensitizationParam(type = "card", fields = "address")
    })
    public UserSensitive testAop(UserSensitive userSensitive){
        System.out.println(JSON.toJSONString(userSensitive));
        return userSensitive;
    }
}
