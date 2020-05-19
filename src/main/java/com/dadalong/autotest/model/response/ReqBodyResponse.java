package com.dadalong.autotest.model.response;

import lombok.Data;

/**
 * 请求参数内部结构
 */
@Data
public class ReqBodyResponse {

    private String name;
    private String type;
    private String required;

}
