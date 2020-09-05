package com.github.doobo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    }
}
