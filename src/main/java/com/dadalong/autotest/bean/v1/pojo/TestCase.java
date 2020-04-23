package com.dadalong.autotest.bean.v1.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  实体——用例case
 * "@Data"：@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode这5个注解的合集。
 * "@EqualsAndHashCode"：会生成equals(Object other) 和 hashCode()方法。默认仅使用该类中定义的属性且不调用父类的方法——通过callSuper=true解决
 * "@TableId"：表示表的主键。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TestCase extends Datetime {

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
     * 创建该条用例的用户id
     */
    private Integer userId;

    /**
     * 执行状态
     */
    private Boolean executeStatus;

    /**
     * 用例内容
     */
    private String caseBody;

    /**
     * 执行次数：默认为0
     */
    private Integer executeCount;

    /**
     * 用例描述
     */
    private String caseDescription;

    /**
     * 执行人id
     */
    private Integer executeByUserId;

}
