package com.dadalong.autotest.model.response;

import lombok.Data;

import java.util.List;

@Data
public class LevelOne {
    private String value;
    private String label;
    private List<LevelTwo> children;
}
