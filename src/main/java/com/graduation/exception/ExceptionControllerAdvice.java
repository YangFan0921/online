package com.graduation.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    //一个异常处理的方法
    @ExceptionHandler
    public String handleServiceException(ServiceException e){
        log.error("业务发生了异常",e);
        return e.getMessage();
    }

    @ExceptionHandler
    public String handleException(Exception e){
        log.error("其它异常",e);
        return e.getMessage();
    }
}
