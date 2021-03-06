package com.dadalong.autotest.model.other;

import lombok.Data;

@Data
public class CaseRules {
    private String name;//参数名称
    private String required;//是否必需，是为1，否为0
    private String type;//参数数据类型：int、string、other
    private String min;//最小值
    private String max;//最大值
    private String options;//选项及内容
    private String isArray;//是否为数组，是为1，否为0
    private String model;//参数类型：phone、email、idNumber、other
}
