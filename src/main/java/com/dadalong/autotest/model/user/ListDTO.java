package com.dadalong.autotest.model.user;

import lombok.Data;

import java.util.List;

@Data
public class ListDTO {
    private String title;
    private String path;
    private String method;
    private String markdown;
    private List<ReqHeadersDTO> req_headers;
    private String req_body_type;
    private List<ReqBodyForm> req_body_form;

}
