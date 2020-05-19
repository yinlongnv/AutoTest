package com.dadalong.autotest.model.user;

import lombok.Data;

import java.util.List;

/**
 * 批量操作请求参数
 */
@Data
public class BatchDTO {
    private List<Integer> userIds;
    private Integer userId;
}
