package com.dadalong.autotest.model.user;

import lombok.Data;

import java.util.Date;

@Data
public class SearchDTO {
    private String userNumber;
    private Integer role;
    private String lastLogin;
    private Integer page;

}
