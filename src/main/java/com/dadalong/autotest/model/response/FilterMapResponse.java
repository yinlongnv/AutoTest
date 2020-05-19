package com.dadalong.autotest.model.response;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 三级筛选下拉框
 */
@Data
public class FilterMapResponse {

    private List<LevelOne> options;

}
