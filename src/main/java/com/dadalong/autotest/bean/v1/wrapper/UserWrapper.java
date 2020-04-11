package com.dadalong.autotest.bean.v1.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.ListWithSearchDTO;
import org.apache.commons.lang3.StringUtils;

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

    public UserWrapper ofSearch(ListWithSearchDTO listWithSearchDTO){
            this.like(StringUtils.isNotBlank(listWithSearchDTO.getUserNumber()), "user_number", listWithSearchDTO.getUserNumber())
                    .like(StringUtils.isNotBlank(String.valueOf(listWithSearchDTO.getRole())), "role", listWithSearchDTO.getRole())
                    .like(StringUtils.isNotBlank(listWithSearchDTO.getStartTime()), "last_login", listWithSearchDTO.getStartTime())
                    .like(StringUtils.isNotBlank(listWithSearchDTO.getEndTime()), "last_login", listWithSearchDTO.getEndTime());
            return this;
    }

}
