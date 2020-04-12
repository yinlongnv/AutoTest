package com.dadalong.autotest.bean.v1.model;

import lombok.Data;

import java.util.List;

@Data
public class ApiData {
    private String name;
    private List<QueryList> list;
}
