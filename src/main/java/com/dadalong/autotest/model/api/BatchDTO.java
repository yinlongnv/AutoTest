package com.dadalong.autotest.model.api;

import lombok.Data;

import java.util.List;

@Data
public class BatchDTO {
    private List<Integer> apiIds;
    private Integer userId;
}
