package com.dadalong.autotest.bean.v1.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * 公告-用户关联表
 */
@Data
public class NoticeUsers {

    /**
     * 公告-用户表id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 通知公告id
     */
    private Integer noticeId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 是否已读，已读时间戳，未读0
     */
    private String isRead;
}
