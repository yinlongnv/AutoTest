package com.dadalong.autotest.model.user;

import lombok.Data;

@Data
public class CreateUserDTO {

    private Integer id;
    private String username;
    private String idNumber;
    private String phoneNumber;
    private String email;
    private String role;
    private String password;

}
