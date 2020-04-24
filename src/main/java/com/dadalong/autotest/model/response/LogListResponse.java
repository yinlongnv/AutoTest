package com.dadalong.autotest.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by 78089 on 2020/4/24.
 */
@Data
public class LogListResponse {

    private Integer id;

    private String username;
    private String userNumber;
    private Integer role;//账号角色：QA0，root1
    private String lastIp;
    private String logContent;
    private String operatePath;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;

}
