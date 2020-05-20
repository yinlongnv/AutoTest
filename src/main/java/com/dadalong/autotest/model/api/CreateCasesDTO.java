package com.dadalong.autotest.model.api;

import lombok.Data;

/**
 * 生成测试用例请求字段
 */
@Data
public class CreateCasesDTO {
    private Integer userId;
    private Integer apiId;
}
