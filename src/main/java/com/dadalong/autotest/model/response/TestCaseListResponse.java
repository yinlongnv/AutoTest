package com.dadalong.autotest.model.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by 78089 on 2020/4/24.
 */
@Data
public class TestCaseListResponse {

    private Integer id;
    private String apiName;//1 + api_id
    private String caseDescription;//2
    private String createdBy;//3创建人用户名 + user_id
    private String username;//4执行人用户名 + execute_by_user_id
    private Date lastExecuteTime;//5最近一次执行时间 updated_at
    private String executeStatus;//6执行状态
    private String executeCount;//7执行次数

}
