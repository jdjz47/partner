package com.czp.usercenter.exception;/*
    author 陈振平
    version 1.0
*/

import lombok.Data;

@Data
public class BusinessException extends Exception{
    String message;
    public BusinessException(){

    }
    public BusinessException(String message){
        this.message=message;
    }

}
