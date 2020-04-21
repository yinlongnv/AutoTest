package com.dadalong.autotest.model.user;

import lombok.Data;

/**
 * Created by 78089 on 2020/4/19.
 */
@Data
public class LoginDTO {

    private String username;
    private String password;
    private String lastIp;

}
