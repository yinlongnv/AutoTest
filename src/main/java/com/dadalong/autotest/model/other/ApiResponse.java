package com.dadalong.autotest.model.other;

import lombok.Data;

/**
 * 接口请求的响应
 */
@Data
public class ApiResponse {
    private String code;
    private String message;
    private String data;
}
