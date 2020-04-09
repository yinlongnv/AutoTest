package com.dadalong.autotest.bean.v1.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.User;

import java.util.Date;

/**
 * 具体内容还没填，这是复制的
 */
public class CaseWrapper extends QueryWrapper<User> {
    public CaseWrapper ofUserNumber(String userNumber){
        this.eq("user_number",userNumber);
        return this;
    }

    public CaseWrapper filterOfRole(String role){
        this.eq("role",role);
        return this;
    }

    public CaseWrapper ofLastLoginDate(Date lastLogin){
        this.eq("last_login",lastLogin);
        return this;
    }

    public CaseWrapper oflikeName(String name){
        this.like("user_number",name).or().like("username",name);
        return this;
    }


}
