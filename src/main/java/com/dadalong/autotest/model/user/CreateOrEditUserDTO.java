package com.dadalong.autotest.model.user;

import lombok.Data;

/**
 * 创建/编辑请求参数
 */
@Data
public class CreateOrEditUserDTO {

    private Integer id;
    private String username;
    private String idNumber;
    private String phoneNumber;
    private String email;
    private Integer role;
    private String password;
    private Integer userId;//如果是创建用户，前端需要传回当前登录用户的id——即创建该账户的用户id

}
