package com.github.doobo;

import com.github.doobo.service.AbstractSensitiveService;
import org.springframework.stereotype.Service;

/**
 * 可自定义实现脱敏函数
 */
@Service
public class SensitiveServiceImpl extends AbstractSensitiveService {

    @Override
    public String idCardNum(String idCardNum, int front, int end) {
        return super.idCardNum(idCardNum, front, end);
    }
}
