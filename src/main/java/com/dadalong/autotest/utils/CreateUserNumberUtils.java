package com.dadalong.autotest.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by 78089 on 2020/4/11.
 * 随机生成用户编号
 */
public class CreateUserNumberUtils {

    public String createUserNumber() {

        //用户编号前缀——AH
        String header = "AH";

        //获取当前年月日——yyyyMMdd
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = simpleDateFormat.format(new Date());
        System.out.println("+++++++++++++++++++++++++新增用户编号时间：" + date);

        //随时生成两位数
        Random random = new Random();
        int num = random.nextInt(99);
        System.out.println("+++++++++++++++++++++++++新增随机用户编号：" + num);
        String number = "" + num;

        String userNumber = header + date + number;

        return  userNumber;
    }
}
