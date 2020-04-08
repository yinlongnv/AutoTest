package com.dadalong.autotest.bean.v1.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Datetime{

    /**
     * 用户编号
     */
    private String userNumber;
    /**
     * 用户名
     */
    private String username;
    /**
     * 账号角色
     */
    private String role;
    /**
     * 账号状态
     */
    private Boolean status;
    /**
     * 身份证号
     */
    private String idNumber;
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 最近登陆ip
     */
    private String lastIp;
    /**
     * 最后登陆时间
     */
    private Date lastLogin;
    /**
     * 总登陆次数
     */
    private Integer loginCount;
    /**
     * 创建者
     */
    private String createdBy;
}
