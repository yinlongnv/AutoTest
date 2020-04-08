package com.dadalong.autotest.bean.v1.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.User;

public class UserWrapper extends QueryWrapper<User> {
    public UserWrapper ofUserNumber(String userNumber){
        this.eq("user_number",userNumber).getSqlSet();
        return this;
    }

    public UserWrapper filterOfRole(String role){
        this.eq("role",role);
        return this;
    }


}
