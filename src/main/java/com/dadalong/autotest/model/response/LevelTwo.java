package com.dadalong.autotest.model.response;

import lombok.Data;

import java.util.List;

/**
 * 三级筛选下拉框
 */
@Data
public class LevelTwo{
    private String value;
    private String label;
    private List<LevelThree> children;
}
