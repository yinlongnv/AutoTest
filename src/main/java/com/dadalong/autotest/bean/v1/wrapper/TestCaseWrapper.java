package com.dadalong.autotest.bean.v1.wrapper;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.TestCase;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class TestCaseWrapper extends QueryWrapper<TestCase> {

    /**
     * 显示用例列表，包含筛选查询功能
     * @param request
     * @return
     */
    public TestCaseWrapper ofListWithSearch(SearchRequest request){
        Map<String,Object> map = request.getSearch();
        Object executeStatus = map.get("executeStatus");
        this.ofExecuteStatus(executeStatus);
        Object caseDescription = map.get("caseDescription");
        if (caseDescription != null && StringUtils.isNotBlank(caseDescription.toString())) {
            this.like("case_description", caseDescription.toString());
        }
        Object apiId = map.get("apiId");
        if (apiId != null && StringUtils.isNotBlank(apiId.toString())) {
            this.eq("api_id", Integer.parseInt(apiId.toString()));
        }
        return this;
    }

    public TestCaseWrapper ofExecuteStatus(Object executeStatus){
        if(executeStatus != null && StringUtils.isNotBlank(executeStatus.toString())){
            System.out.println(Integer.parseInt(executeStatus.toString()));
            this.eq("execute_status",Integer.parseInt(executeStatus.toString()));
        }
        return this;
    }

}
