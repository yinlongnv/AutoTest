package com.dadalong.autotest.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 通知公告返回数据
 */
@Data
public class NoticeListResponse {

    private Integer id;

    private String username;// 执行人用户名

    private String projectName;// 所属项目
    private String apiGroup;// 所属分组
    private String apiName;// 接口名称
    private String apiPath;// 接口路径

    private Integer caseId;// 用例id
    private String htmlUrl;// html测试报告文件名
    private String isRead;// 是否已读
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdAt;// 执行时间

}
