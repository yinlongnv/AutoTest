package com.dadalong.autotest.bean.v1.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.User;

import java.util.Date;

public class UserWrapper extends QueryWrapper<User> {
    public UserWrapper ofUserNumber(String userNumber){
        this.eq("user_number",userNumber);
        return this;
    }

    public UserWrapper filterOfRole(String role){
        this.eq("role",role);
        return this;
    }

    public UserWrapper ofLastLoginDate(Date lastLogin){
        this.eq("last_login",lastLogin);
        return this;
    }

    public UserWrapper oflikeName(String name){
        this.like("user_number",name).or().like("username",name);
        return this;
    }


}
