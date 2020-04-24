package com.dadalong.autotest.model.response;

import lombok.Data;

/**
 * Created by 78089 on 2020/4/24.
 */
@Data
public class ApiListResponse {

    private Integer id;

    private String apiPath;//1
    private String apiName;//2
    private String reqMethod;//3
    private String projectName;//4
    private String apiGroup;//5
    private String createdBy;//6创建人用户名 + user_id

}
