package com.dadalong.autotest.model.response;

import com.dadalong.autotest.model.other.LevelOne;
import lombok.Data;

import java.util.List;

/**
 * 三级筛选下拉框
 */
@Data
public class FilterMapResponse {

    private List<LevelOne> options;

}
