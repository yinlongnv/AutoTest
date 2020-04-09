package com.dadalong.autotest.model.user;

import lombok.Data;

@Data
public class ReqBodyForm {
    private String required;
    private String _id;
    private String name;
    private String type;
    private String desc;
    private String example;
}
