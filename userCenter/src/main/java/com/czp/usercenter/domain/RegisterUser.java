package com.czp.usercenter.domain;/*
    author 陈振平
    version 1.0
*/

import lombok.Data;

@Data
public class RegisterUser {
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String planetCode;
}
