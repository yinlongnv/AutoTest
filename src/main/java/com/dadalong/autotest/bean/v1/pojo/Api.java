package com.dadalong.autotest.bean.v1.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by 78089 on 2020/4/9.
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
     * 所属业务
     */
    private Integer projectId;

    /**
     * 所属分组
     */
    private String apiGroup;

    /**
     * 环境域名
     */
    private String baseUrl;

    /**
     * 请求头
     */
    private String reqHeaders;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 接口路径
     */
    private String apiPath;

    /**
     * 接口描述
     */
    private String apiDescription;

    /**
     * 请求方法
     */
    private String reqMethod;

    /**
     * 请求体
     */
    private String reqBody;

    /**
     * 相应信息
     */
    private String apiResponse;

}
