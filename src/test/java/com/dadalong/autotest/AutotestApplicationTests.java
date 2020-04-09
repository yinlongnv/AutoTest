package com.dadalong.autotest;

import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.CreateUserDTO;
import com.dadalong.autotest.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@SpringBootTest(classes = AutoTestApplication.class)
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class AutotestApplicationTests {
    @Autowired
    private IUserService iUserService;

    @Test
    void contextLoads() {
    }

    @Test
    public void addUser(){
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setUsername("aaa");
        userDTO.setIdNumber("bbb");
        userDTO.setPhoneNumber("12312312312");
        userDTO.setEmail("72032312@qq.com");
        userDTO.setRole("root");
        userDTO.setPassword("sakura");
        iUserService.addUser(userDTO);
    }

//    @Test
//    public void ofRole(){
//        List<User> users = iUserService.filterRole("qa");
//        users.forEach(System.out::println);
//    }
//
//    public void lastLogin(){
//
//    }

//    @Test
//    public void ofLike(){
//        List<User> users = iUserService.searchByName("c");
//        users.forEach(System.out::println);
//    }
}
