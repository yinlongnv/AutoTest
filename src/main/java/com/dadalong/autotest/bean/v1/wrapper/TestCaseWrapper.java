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
        Object search = map.get("search");
        if (search != null && StringUtils.isNotBlank(search.toString())) {
            this.like("case_description", search.toString());
        }
        this.ofApiName(map.get("apiName"));
        Object executeStatus = map.get("executeStatus");
        this.ofExecuteStatus(executeStatus);
        return this;
    }

    public TestCaseWrapper ofApiName(Object ApiName){
        if(ApiName != null && StringUtils.isNotBlank(ApiName.toString())){
            System.out.println(ApiName.toString());
            this.eq("project_name",ApiName.toString());
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
