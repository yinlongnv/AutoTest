package com.dadalong.autotest;

import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        CreateOrEditUserDTO userDTO = new CreateOrEditUserDTO();
        userDTO.setUsername("aaa");
        userDTO.setIdNumber("bbb");
        userDTO.setPhoneNumber("12312312312");
        userDTO.setEmail("72032312@qq.com");
        userDTO.setRole(1);
        userDTO.setPassword("sakura");
        iUserService.createOrEditUser(userDTO);
    }

}
