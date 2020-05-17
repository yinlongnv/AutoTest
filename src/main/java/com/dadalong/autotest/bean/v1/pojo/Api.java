package com.dadalong.autotest.bean.v1.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  实体——接口api
 * "@Data"：@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode这5个注解的合集。
 * "@EqualsAndHashCode"：会生成equals(Object other) 和 hashCode()方法。默认仅使用该类中定义的属性且不调用父类的方法——通过callSuper=true解决
 * "@TableId"：表示表的主键。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Api extends Datetime{

    /**
     * 接口id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 环境域名
     */
    private String baseUrl;
    /**
     * 所属业务
     */
    private String projectName;
    /**
     * 所属分组
     */
    private String apiGroup;
    /**
     * 接口名称
     */
    private String apiName;
    /**
     * 接口路径
     */
    private String apiPath;
    /**
     * 请求方法
     */
    private String reqMethod;
    /**
     * 接口描述
     */
    private String apiDescription;
    /**
     * 请求头
     */
    private String reqHeaders;
    /**
     * 请求参数
     */
    private String reqQuery;
    /**
     * 请求体
     */
    private String reqBody;
    /**
     * 参数规则
     */
    private String caseRules;
    /**
     * 响应信息
     */
    private String apiResponse;
    /**
     * 创建该接口的用户id
     */
    private Integer userId;

}
