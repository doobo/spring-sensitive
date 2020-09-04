package com.github.doobo.fastjson;

import lombok.Getter;

/**
 * 脱敏后数据返回
 */
public class FastJsonCustomException extends RuntimeException {

    @Getter
    private final Object rt;

    public FastJsonCustomException(Object rt) {
        this.rt = rt;
    }
}