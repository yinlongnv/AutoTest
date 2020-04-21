package com.dadalong.autotest.bean.v1.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.ListWithSearchDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class UserWrapper extends QueryWrapper<User> {

    public UserWrapper ofUsernameAndPassword(String username, String password) {
        this.eq("username", username).eq("password", password);
        return this;
    }

    public UserWrapper ofUsername(String username) {
        this.eq("username", username);
        return this;
    }

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

    public UserWrapper ofListWithSearch(ListWithSearchDTO listWithSearchDTO){
//            this.like(StringUtils.isNotBlank(listWithSearchDTO.getUserNumber()), "user_number", listWithSearchDTO.getUserNumber())
//                    .like(StringUtils.isNotBlank(String.valueOf(listWithSearchDTO.getRole())), "role", listWithSearchDTO.getRole())
//                    .like(StringUtils.isNotBlank(listWithSearchDTO.getUsername()), "username", listWithSearchDTO.getUsername())
//                    .between("last_login", listWithSearchDTO.getStartTime(), listWithSearchDTO.getEndTime());
        System.out.println("role：" + StringUtils.isNotBlank(listWithSearchDTO.getUserNumber()));
        this.like(StringUtils.isNotBlank(listWithSearchDTO.getUserNumber()), "user_number", listWithSearchDTO.getUserNumber())
                .like(StringUtils.isNotBlank(String.valueOf(listWithSearchDTO.getRole())), "role", listWithSearchDTO.getRole())
                .like(StringUtils.isNotBlank(listWithSearchDTO.getUsername()), "username", listWithSearchDTO.getUsername())
                .between(StringUtils.isNotBlank(listWithSearchDTO.getStartTime()) || StringUtils.isNotBlank(listWithSearchDTO.getEndTime()), "last_login", listWithSearchDTO.getStartTime(), listWithSearchDTO.getEndTime());
        return this;
    }

}
