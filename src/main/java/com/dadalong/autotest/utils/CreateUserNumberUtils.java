package com.dadalong.autotest.utils;

import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建用户时随机生成用户编号
 */
@Configuration
public class CreateUserNumberUtils {

    public String createUserNumber() {
        //用户编号前缀——AH
        String header = "AH";
        //获取当前年月日——yyyyMMddHHmmss
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = simpleDateFormat.format(new Date());
//        System.out.println("+++++++++++++++++++++++++新增用户编号时间：" + date);
        String userNumber = header + date;
        return userNumber;
    }
}
