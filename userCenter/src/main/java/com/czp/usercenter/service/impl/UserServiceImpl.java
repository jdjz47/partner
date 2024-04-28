package com.czp.usercenter.service.impl;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czp.usercenter.mapper.UserMapper;
import com.czp.usercenter.service.UserService;
import com.czp.usercenter.domain.User;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.czp.usercenter.domain.UserConstant.USER_LOGIN_STATE;

/**
* @author czp
* @description 针对表【user(用户表格)】的数据库操作Service实现
* @createDate 2024-02-07 16:21:51
*/

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword,String planetCode) {
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword,planetCode)){
            return -1;
        }
        if(userAccount.length()<4){
            return -1;
        }
        if(userPassword.length()<8&&checkPassword.length()<8){
            return -1;
        }
        //确保密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            return -1;
        }
        if(planetCode.length()>5){
            return -1;
        }
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("planetCode",planetCode);
        Long count=this.count(wrapper);
        if(count>0){
            return -1;
        }
        System.out.println("数据测试中");
        //通过校验后开始注册
        User user=new User();
        user.setUserAccount(userAccount);
        user.setPlanetCode(planetCode);
        String s = DigestUtils.md5DigestAsHex(userPassword.getBytes());
        user.setUserPassword(s);
        boolean save = this.save(user);
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        if(userAccount.length()<4){
            return null;
        }
        if(userPassword.length()<8){
            return null;
        }
        String s = DigestUtils.md5DigestAsHex(userPassword.getBytes());
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("userAccount",userAccount);
        wrapper.eq("userPassword",s);
        User user = userMapper.selectOne(wrapper);
        if(user==null){
            return null;
        }
        //用户数据存在进行存储
        HttpSession session = request.getSession();
        session.setAttribute(USER_LOGIN_STATE,user);
        //对数据进行脱敏,然后返回前端
        User safetyUser=new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        return safetyUser;
    }

//    @Deprecated
//    private List<User> tagListUser_Deprecated(List<String> tagList) {
//        if(CollectionUtils.isEmpty(tagList)){
//            return null;
//        }
//        QueryWrapper<User> wrapper=new QueryWrapper<>();
//        for (String tag:tagList){
//            wrapper.like("tags",tag);
//        }
//        List<User> users = userMapper.selectList(wrapper);
//        return users;
//    }

    public List<User> tagListUser(List<String> tagList) {
        if(CollectionUtils.isEmpty(tagList)){
            return null;
        }
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        //先查询出所有的用户
        List<User> users = userMapper.selectList(wrapper);
        List<User> collect = users.stream().filter(user -> {
            if (user.getTags() == null) {
                return false;
            }
            String tags = user.getTags();
            for (String tag : tagList) {
                if (tags.contains(tag)) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public Integer minDistance(List<String> list, List<String> list1) {
        int m = list.size(), n = list1.size();
        int[][] dp = new int[m+1][n+1];  //创建一个二维数组
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (list.get(i-1).equals(list1.get(j-1))) {
                    dp[i][j] = dp[i-1][j-1]+60;
                } else {
                    dp[i][j] = Math.max(dp[i-1][j-1], Math.max(dp[i-1][j], dp[i][j-1])) + 1;
                }
            }
        }
        return dp[m][n];
    }

    @Override
    public List<User> matchUserList(Integer num, User user) {
        String tags = user.getTags();
        String[] userTagList = tags.split(",");
        QueryWrapper<User> wrapper=new QueryWrapper();
        wrapper.select("id","username","planetCode","avatarUrl","profile","tags");
        wrapper.notIn("id",user.getId());
        wrapper.and(wrappers->{
            for (int i = 0; i < userTagList.length; i++) {
                wrappers.like("tags",userTagList[i]).or();
            }
        });
        //根据用户标签进行来确定大致目标
        List<User> userAll = this.list(wrapper);
        List<String> list = Arrays.asList(userTagList);
        SortedMap<Integer,List<User>> map=new TreeMap();
            for(User index:userAll) {
                if (!StringUtils.isBlank(index.getTags())) {
                    String[] userTagList2 = index.getTags().split(",");
                    List<String> list2 = Arrays.asList(userTagList2);
                    Integer score = this.minDistance(list, list2);
                    List<User> list1 = map.get(score);
                    if(list1==null){
                        list1=new ArrayList<>();
                    }
                    list1.add(index);
                    map.put(score,list1);
                }
            }
        List<User> userList=new ArrayList<>();
            int i=map.size()-1;
            while(userList.size()<5&&i>=1){
                Set<Integer> scores = map.keySet();
                Object[] scores_key  =scores.toArray();
                List<User> userList1 = map.get(scores_key[i]);
                for (User user1 : userList1) {
                    if(userList.size()<5) {
                        userList.add(user1);
                    }else{
                        break;
                    }
                    }
                i--;
                }
            return userList;
    }
}