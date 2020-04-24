package com.dadalong.autotest.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by 78089 on 2020/4/24.
 */
@Data
public class TestCaseListResponse {

    private Integer id;

    private String projectName;
    private String apiGroup;
    private String baseUrl;
    private String reqMethod;
    private String reqHeaders;
    private String reqBody;
    private String apiResponse;
    private String apiPath;
    private String apiName;//1 + api_id
    private String apiDescription;

    private String caseDescription;//2
    private String createdBy;//3创建人用户名 + user_id
    private String username;//4执行人用户名 + execute_by_user_id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastExecuteTime;//5最近一次执行时间 updated_at
    private Integer executeStatus;//6执行状态
    private Integer executeCount;//7执行次数

}
