package com.dadalong.autotest.bean.v1.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  实体——操作日志operate_log
 * "@Data"：@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode这5个注解的合集。
 * "@EqualsAndHashCode"：会生成equals(Object other) 和 hashCode()方法。默认仅使用该类中定义的属性且不调用父类的方法——通过callSuper=true解决
 * "@TableId"：表示表的主键。
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
     * 产生该操作日志之的用户id
     */
    private Integer userId;

    /**
     * 账号操作内容
     */
    private String logContent;

    /**
     * 操作界面路径
     */
    private String operatePath;

}
