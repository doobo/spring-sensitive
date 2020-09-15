package com.github.doobo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.github.doobo.undo.HyposensitizationAop;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.doobo.config.ClassUtils.isBaseType;
import static com.github.doobo.config.ClassUtils.swapBaseType;

public class CommonTests {
    
    @Test
    public void testJSONPath(){
        List<UserSensitive> us = Arrays.asList(new UserSensitive(), new UserSensitive());
        Map<String,Object> map = Collections.singletonMap("us", us);
        boolean tag = JSONPath.contains(us, "$..phone[0]");
        System.out.println(tag);
        Object obj = JSONPath.eval(us, "$..phone[0]");
        System.out.println(obj);
        JSONPath.set(map, "$.us..phone", "123");
        System.out.println(JSON.toJSONString(us));
        
        JSONPath path = JSONPath.compile("$..phone");
        System.out.println(path.getPath());
        Map<String, Object> paths = JSONPath.paths(map);
        System.out.println(JSON.toJSONString(paths));
    }
    
    @Test
    public void testClass(){
        System.out.println(isBaseType(String.class));
        System.out.println(isBaseType(HyposensitizationAop.class));
        System.out.println(isBaseType(Integer.class));
        System.out.println(isBaseType(int.class));
    }
    
    @Test
    public void testMemory(){
        UserSensitive userSensitive = new UserSensitive();
        mm(userSensitive);
        System.out.println(userSensitive.address);
    }
    
    public void mm(UserSensitive userSensitive){
        UserSensitive userSensitive2 = new UserSensitive();
        userSensitive2.setAddress("........5");
        BeanUtils.copyProperties(userSensitive2, userSensitive);
    }
}
