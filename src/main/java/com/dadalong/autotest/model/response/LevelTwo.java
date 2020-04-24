package com.dadalong.autotest.model.response;

import lombok.Data;

import java.util.List;

@Data
public class LevelTwo{
    private String value;
    private String label;
    private List<LevelThree> children;
}
