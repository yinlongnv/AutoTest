package com.dadalong.autotest.model.testCase;

import lombok.Data;

import java.util.List;

/**
 * 批量操作请求参数
 */
@Data
public class BatchDTO {
    List<Integer> caseIds;
    private Integer userId;
}
