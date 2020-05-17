package com.dadalong.autotest.bean.v1.model;

import lombok.Data;

@Data
public class ApiExcelExport {

    /**
     * 环境域名
     */
//    @ExcelField("环境域名")
    private String baseUrl;
    /**
     * 所属业务
     */
//    @ExcelField("所属业务")
    private String projectName;
    /**
     * 所属分组
     */
//    @ExcelField("所属分组")
    private String apiGroup;
    /**
     * 接口名称
     */
//    @ExcelField("接口名称")
    private String apiName;
    /**
     * 接口路径
     */
//    @ExcelField("接口路径")
    private String apiPath;
    /**
     * 请求方法
     */
//    @ExcelField("请求方法")
    private String reqMethod;
    /**
     * 接口描述
     */
//    @ExcelField("接口描述")
    private String apiDescription;
    /**
     * 请求头
     */
//    @ExcelField("请求头")
    private String reqHeaders;
    /**
     * 请求参数
     */
//    @ExcelField("请求参数")
    private String reqQuery;
    /**
     * 请求体
     */
//    @ExcelField("请求体")
    private String reqBody;
    /**
     * 响应信息
     */
//    @ExcelField("响应信息")
    private String apiResponse;
}
