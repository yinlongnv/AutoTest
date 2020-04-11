package com.dadalong.autotest.bean.v1.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 *  实体——用户user
 * "@Data"：@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode这5个注解的合集。
 * "@EqualsAndHashCode"：会生成equals(Object other) 和 hashCode()方法。默认仅使用该类中定义的属性且不调用父类的方法——通过callSuper=true解决
 * "@TableId"：表示表的主键。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends Datetime{

    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户编号
     */
    private String userNumber;
    /**
     * 用户名
     */
    private String username;
    /**
     * 账号角色：QA0，root1
     */
    private Integer role;
    /**
     * 账号状态：启用0，禁用1
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
     * 最后登录时间
     */
    private Date lastLogin;
    /**
     * 总登录次数：默认为0
     */
    private Integer loginCount;
    /**
     * 创建该账号的用户id
     */
    private Integer userId;

}
