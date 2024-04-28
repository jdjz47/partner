package com.czp.usercenter.mapper;/*
    author 陈振平
    version 1.0
*/

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.czp.usercenter.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
