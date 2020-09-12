package com.github.doobo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
    
    @Resource
    UndoService undoService;
    
    @Test
    void testObserver() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for(int i = 0; i < 1000; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    UserSensitive userSensitive = undoService.testAop(new UserSensitive());
                    if(!"...1".equals(userSensitive.address)){
                        System.out.println("................");
                    }
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await(60, TimeUnit.SECONDS);
    }
    
}


