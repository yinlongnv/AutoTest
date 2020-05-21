package com.dadalong.autotest.bean.v1.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公告
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Notice extends Datetime{

    /**
     * 日志id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 执行人id
     */
    private Integer userId;

    /**
     * 用例id
     */
    private Integer caseId;

    /**
     * 测试报告html文件名
     */
    private String htmlUrl;

    /**
     * 是否已读，已读1，未读0
     */
    private Integer isRead;
}
