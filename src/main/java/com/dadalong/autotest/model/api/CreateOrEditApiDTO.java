package com.dadalong.autotest.model.api;

import lombok.Data;

@Data
public class CreateOrEditApiDTO {

    private Integer id;

    private String baseUrl;
    private String projectName;
    private String apiGroup;
    private String apiName;
    private String apiPath;
    private String reqMethod;
    private String apiDescription;
    private String reqHeaders;
    private String reqQuery;
    private String reqBody;
    private String caseRules;
    private String apiResponse;

    private Integer userId;//如果是创建，前端需要传回当前登录用户的id——即创建该账户的用户id

}
