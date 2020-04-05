package com.dadalong.testng.suite;

import org.testng.annotations.Test;

//写登录@Test注解的测试方法
public class LoginTest {

    @Test
    public void loginSuccess() {
        System.out.println("登录成功");
    }

    @Test
    public void loginFail() {
        System.out.println("登录失败");
    }

}
