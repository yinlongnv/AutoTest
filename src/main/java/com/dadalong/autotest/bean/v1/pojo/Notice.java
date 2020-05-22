package com.dadalong.autotest.bean.v1.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告表
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Notice extends Datetime{

    /**
     * 通知公告id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 执行人id
     */
    private Integer creatorId;

    /**
     * 接口id
     */
    private Integer apiId;

    /**
     * 用例id
     */
    private Integer caseId;

    /**
     * 测试报告html文件名
     */
    private String htmlUrl;
}
