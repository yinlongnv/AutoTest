package com.dadalong.autotest.model.api;

import com.dadalong.autotest.model.other.CaseRules;
import lombok.Data;

import java.util.List;

/**
 * 接收参数规则
 */
@Data
public class CaseRulesDTO {
    private Integer apiId;
    private Integer userId;
    private List<CaseRules> caseRulesList;
}
