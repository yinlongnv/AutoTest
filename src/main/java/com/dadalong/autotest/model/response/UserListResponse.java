package com.dadalong.autotest.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by 78089 on 2020/4/24.
 */
@Data
public class UserListResponse {

    private Integer id;

    private String username;
    private String userNumber;
    private Integer status;//账号状态：启用0，禁用1
    private Integer role;//账号角色：QA0，root1

    private String createdBy;//创建人用户名 + user_id

    private String lastIp;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastLogin;
    private Integer loginCount;

    //以下目前多余
    private String idNumber;
    private String phoneNumber;
    private String email;
    private String password;

}
