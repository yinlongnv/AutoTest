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
public class OperateLog extends Datetime{

    /**
     * 日志id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 操作致生成该条日志的用户id
     */
    private Integer userId;

    /**
     * 账号操作
     */
    private String logContent;

    /**
     * 操作时间
     */
    private Date operateTime;

    /**
     * 操作界面
     */
    private String operatePath;

}
