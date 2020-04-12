package com.dadalong.autotest.bean.v1.model;

import lombok.Data;

import java.util.List;

@Data
public class QueryList {
    private String path;
    private String method;
    private String title;
    private String markdown;
    private List<ReqHeader> req_headers;
    private String req_body_type; //如果是form类型，则是列表，如果是row,json类型 则是other 是字符串
    private List<ReqBodyForm> req_body_form;
    private String req_body_other;
}
