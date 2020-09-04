package com.github.doobo.fastjson;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  脱敏方法异常处理
 */
@ControllerAdvice
@Slf4j
public class FastJsonExceptionHandler {
    
    @ResponseBody
    @ExceptionHandler(value = {FastJsonCustomException.class})
    public ResponseEntity<Object> exception(FastJsonCustomException e) {
        if(e != null) {
            return new ResponseEntity<>(e.getRt(), HttpStatus.OK);
        }
        return new ResponseEntity<>(Boolean.FALSE, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
