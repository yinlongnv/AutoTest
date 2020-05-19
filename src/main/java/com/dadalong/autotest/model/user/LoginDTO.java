package com.dadalong.autotest.model.user;

import lombok.Data;

/**
 * 登录请求参数
 */
@Data
public class LoginDTO {

    private String username;
    private String password;
    private String lastIp;

}
