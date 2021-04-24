package com.github.doobo.service;

/**
 * 脱敏服务
 */
public interface SensitiveService {

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     */
    String chineseName(final String fullName);

    /**
     * [中文姓名] 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     */
    String chineseName(final String familyName, final String givenName);

    /**
     * [身份证号] 显示最后四位，其他隐藏。共计18位或者15位。<例子：*************5762>
     */
    String idCardNum(final String id);

    /**
     * 【身份证号】前三位 和后三位
     */
    String idCardNum(String idCardNum, int front, int end);

    /**
     * 【固定电话 前四位，后两位
     */
    String fixedPhone(String num);

    /**
     * [手机号码] 前三位，后四位，其他隐藏<例子:138******1234>
     */
    String mobilePhone2(final String num);

    /**
     * 【手机号码】前三位，后两位，其他隐藏，比如135******10
     */
    String mobilePhone(String num);

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     * @param sensitiveSize 敏感信息长度
     */
    String address(final String address, final int sensitiveSize);

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     */
    String email(final String email);

    /**
     * [银行卡号] 前六位，后四位，其他用星号隐藏每位1个星号<例子:6222600**********1234>
     */
    String bankCard(final String cardNum);

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位，其他用星号隐藏，每位1个星号<例子:12********>
     */
    String shopsCode(final String code);

    /**
     * 【密码】密码的全部字符都用*代替，比如：******
     */
    String password(String password);
}
