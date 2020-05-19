package com.dadalong.autotest.model.other;

import lombok.Data;

import java.util.List;

/**
 * 三级筛选下拉框
 */
@Data
public class LevelOne {
    private String value;
    private String label;
    private List<LevelTwo> children;
}
