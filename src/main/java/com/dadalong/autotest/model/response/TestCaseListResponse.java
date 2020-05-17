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
    private String baseUrl;
    private String projectName;
    private String apiGroup;
    private String apiName;//1 + api_id
    private String apiPath;
    private String reqMethod;
    private String apiDescription;
    private String reqHeaders;
    private String reqQuery;
    private String reqBody;
    private String apiResponse;

    private String caseRules;
    private String caseBody;
    private String caseDescription;//2
    private String caseResponse;
    private String createdBy;//3创建人用户名 + user_id
    private String username;//4执行人用户名 + execute_by_user_id
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastExecuteTime;//5最近一次执行时间 updated_at
    private Integer executeStatus;//6执行状态
    private Integer executeCount;//7执行次数

}
