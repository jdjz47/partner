package com.czp.usercenter;

import com.czp.usercenter.domain.User;
import com.czp.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class UserCenterApplicationTests {
    @Resource
    UserService service;
    @Test
    void contextLoads() {
    }
    @Test
    public void searchTest(){
        List<String> tags= Arrays.asList("java","实习");
        List<User> users = service.tagListUser(tags);
        System.out.println(users);
    }
}
