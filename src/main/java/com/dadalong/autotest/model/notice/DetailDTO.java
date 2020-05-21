package com.dadalong.autotest.model.notice;

import lombok.Data;

/**
 * 查看详情请求参数
 */
@Data
public class DetailDTO {
    private Integer noticeId;
    private Integer userId;
}
