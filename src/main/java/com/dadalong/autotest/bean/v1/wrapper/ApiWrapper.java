package com.dadalong.autotest.bean.v1.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.User;

import java.util.Date;

/**
 * 具体内容还没填，这是复制的
 */
public class ApiWrapper extends QueryWrapper<User> {
    public ApiWrapper ofUserNumber(String userNumber){
        this.eq("user_number",userNumber);
        return this;
    }

    public ApiWrapper filterOfRole(String role){
        this.eq("role",role);
        return this;
    }

    public ApiWrapper ofLastLoginDate(Date lastLogin){
        this.eq("last_login",lastLogin);
        return this;
    }

    public ApiWrapper oflikeName(String name){
        this.like("user_number",name).or().like("username",name);
        return this;
    }


}
