package com.github.doobo.service;

import com.github.doobo.config.SensitiveInfoUtils;
import com.github.doobo.fastjson.DesensitizationParam;
import com.github.doobo.jackson.SensitiveInfo;

/**
 * 默认脱敏工具抽象类
 * 使用时，可继承并重写(覆盖)相关方法
 */
public abstract class AbstractSensitiveService implements SensitiveService{
    @Override
    public String chineseName(String fullName) {
        return SensitiveInfoUtils.chineseName(fullName);
    }

    @Override
    public String chineseName(String familyName, String givenName) {
        return SensitiveInfoUtils.chineseName(familyName, givenName);
    }

    @Override
    public String idCardNum(String id) {
        return SensitiveInfoUtils.idCardNum(id);
    }

    @Override
    public String idCardNum(String idCardNum, int front, int end) {
        return SensitiveInfoUtils.idCardNum(idCardNum, front, end);
    }

    @Override
    public String fixedPhone(String num) {
        return SensitiveInfoUtils.fixedPhone(num);
    }

    @Override
    public String mobilePhone2(String num) {
        return SensitiveInfoUtils.mobilePhone2(num);
    }

    @Override
    public String mobilePhone(String num) {
        return SensitiveInfoUtils.mobilePhone(num);
    }

    @Override
    public String address(String address, int sensitiveSize) {
        return SensitiveInfoUtils.address(address, sensitiveSize);
    }

    @Override
    public String email(String email) {
        return SensitiveInfoUtils.email(email);
    }

    @Override
    public String bankCard(String cardNum) {
        return SensitiveInfoUtils.bankCard(cardNum);
    }

    @Override
    public String shopsCode(String code) {
        return SensitiveInfoUtils.shopsCode(code);
    }

    @Override
    public String password(String password) {
        return SensitiveInfoUtils.password(password);
    }

    @Override
    public String selfFastJsonHandler(String input, DesensitizationParam param) {
        return input;
    }

    @Override
    public String selfJacksonHandler(String input, SensitiveInfo param) {
        return input;
    }
}
