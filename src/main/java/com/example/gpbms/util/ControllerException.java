package com.example.gpbms.util;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //全局捕获异常
public class ControllerException {

    @ResponseBody
    @ExceptionHandler //统一处理某一类异常
    public RespBean exception(Exception e){
        String failureMsg = "操作失败";
        RespBean respBean = RespBean.failure(failureMsg);
        System.out.println(e);
        return respBean;
    }
}
