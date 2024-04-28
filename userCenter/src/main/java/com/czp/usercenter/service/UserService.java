package com.czp.usercenter.service;

import com.czp.usercenter.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author czp
* @description 针对表【user(用户表格)】的数据库操作Service
* @createDate 2024-02-07 16:21:51
*/
public interface UserService extends IService<User> {
    public long userRegister(String userAccount,String userPassword,String checkPassword,String plantCode);
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request);
    public List<User> tagListUser(List<String> tagList);


    Integer minDistance(List<String> list, List<String> list1);

    List<User> matchUserList(Integer num, User user);
}
