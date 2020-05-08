package com.dadalong.autotest.model.user;

import lombok.Data;

import java.util.List;

@Data
public class BatchDTO {
    private List<Integer> userIds;
    private Integer userId;
}
