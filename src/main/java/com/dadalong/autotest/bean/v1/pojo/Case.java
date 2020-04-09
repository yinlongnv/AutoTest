package com.dadalong.autotest.bean.v1.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by 78089 on 2020/4/9.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Case extends Datetime {

    /**
     * 用例id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 关联接口的id
     */
    private Integer apiId;

    /**
     * 执行人的id
     */
    private Integer userId;

    /**
     * 最后执行时间
     */
    private Date lastExecuteTime;

    /**
     * 执行状态
     */
    private Boolean executeStatus;

    /**
     * 用例内容
     */
    private String caseBody;

    /**
     * 执行次数
     */
    private Integer executeCount;

}
