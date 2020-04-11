package com.dadalong.autotest.model.user;

import lombok.Data;

@Data
public class CreateOrEditUserDTO {

    private Integer id;
    private String username;
    private String idNumber;
    private String phoneNumber;
    private String email;
    private Integer role;
    private String password;
    private String lastIp;
    private Integer userId;

}
