package com.dadalong.autotest.model.response;

import com.dadalong.autotest.bean.v1.pojo.TestCase;
import com.dadalong.autotest.model.api.CreateOrEditApiDTO;
import lombok.Data;

import java.util.List;

/**
 * Created by 78089 on 2020/4/24.
 */
@Data
public class ApiListResponse {

    private Integer id;

    private String projectName;
    private String apiGroup;
    private String baseUrl;
    private String reqMethod;
    private String reqHeaders;
    private String reqBody;
    private String apiResponse;
    private String apiPath;
    private String apiName;
    private String apiDescription;
    private String createdBy;//创建人用户名 + user_id

    private List<TestCaseListResponse> testCaseList;

}
