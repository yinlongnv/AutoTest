package com.dadalong.autotest.model.testCase;

import lombok.Data;

@Data
public class CreateOrEditCaseDTO {

    private Integer id;
    private String projectName;
    private String apiGroup;
    private String apiNamePath;
    private String caseContent;
    private String caseDescription;
    private Integer userId;//如果是创建用户，前端需要传回当前登录用户的id——即创建该账户的用户id

}