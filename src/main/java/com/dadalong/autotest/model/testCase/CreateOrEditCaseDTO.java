package com.dadalong.autotest.model.testCase;

import lombok.Data;

/**
 * 创建/编辑请求参数
 */
@Data
public class CreateOrEditCaseDTO {

    private Integer id;
    private String projectName;
    private String apiGroup;
    private String apiMerge;//apiName apiPath格式
    private String caseBody;
    private String caseDescription;
    private String caseResponse;

//    private Integer apiId;
    private Integer userId;//如果是创建，前端需要传回当前登录用户的id——即创建该账户的用户id
}
