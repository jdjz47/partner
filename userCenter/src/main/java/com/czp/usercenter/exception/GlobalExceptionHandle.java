package com.czp.usercenter.exception;/*
    author 陈振平
    version 1.0
    全局异常处理器
*/

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandle {
    @ExceptionHandler(BusinessException.class)
    public String ExceptionHandle(BusinessException e){
        return e.message;
    }
}
