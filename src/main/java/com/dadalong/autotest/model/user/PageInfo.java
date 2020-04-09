package com.dadalong.autotest.model.user;

import lombok.Data;

@Data
public class PageInfo {
    private Integer page;
    private Long size;
    private Long total;
}
