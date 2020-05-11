package com.dadalong.autotest.model.api;

import lombok.Data;

@Data
public class CreateOrEditApiDTO {

    private Integer id;

    private String projectGroup;
    private String baseUrl;
    private String reqMethod;
    private String reqHeaders;
    private String reqBody;
    private String apiResponse;
    private String apiPath;
    private String apiName;
    private String apiDescription;

    private Integer userId;//如果是创建用户，前端需要传回当前登录用户的id——即创建该账户的用户id

}
