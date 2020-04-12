package com.dadalong.autotest.bean.v1.model;

import lombok.Data;

@Data
public class ReqBodyForm {
    private String required;
    private String id;
    private String name;
    private String type;
    private String desc;
    private String example;
}
