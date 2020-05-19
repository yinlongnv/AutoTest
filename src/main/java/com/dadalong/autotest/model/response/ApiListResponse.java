package com.dadalong.autotest.model.response;

import com.dadalong.autotest.bean.v1.pojo.TestCase;
import com.dadalong.autotest.model.api.CreateOrEditApiDTO;
import lombok.Data;

import java.util.List;

/**
 * api列表返回数据
 */
@Data
public class ApiListResponse {

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

    private String createdBy;//创建人用户名 + user_id

//    private List<TestCaseListResponse> testCaseList;

}
