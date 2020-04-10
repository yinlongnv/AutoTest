package com.dadalong.autotest.model.user;

import lombok.Data;

import java.util.Date;

@Data
public class SearchDTO {
    private String userNumber;
    private String role;
    private String lastLoginTime;
    private Integer page;

}
