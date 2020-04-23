package com.dadalong.autotest.bean.v1.wrapper;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.pojo.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

public class ApiWrapper extends QueryWrapper<Api> {

    /**
     * 显示接口列表，包含筛选查询功能
     * @param request
     * @return
     */
    public ApiWrapper ofListWithSearch(SearchRequest request){
        Map<String,Object> map = request.getSearch();
        Object search = map.get("search");
        if (search != null && StringUtils.isNotBlank(search.toString())) {
            this.like("api_name", search.toString());
        }
        this.ofProjectName(map.get("projectName"));
        Object reqMethod = map.get("reqMethod");
        this.ofReqMethod(reqMethod);
        return this;
    }

    public ApiWrapper ofProjectName(Object projectName){
        if(projectName != null && StringUtils.isNotBlank(projectName.toString())){
            System.out.println(projectName.toString());
            this.eq("project_name",projectName.toString());
        }
        return this;
    }

    public ApiWrapper ofReqMethod(Object reqMethod){
        if(reqMethod != null && StringUtils.isNotBlank(reqMethod.toString())){
            System.out.println(reqMethod.toString());
            this.eq("req_method",reqMethod.toString());
        }
        return this;
    }

    public ApiWrapper getProjectName(){
        this.select("project_name");
        return this;
    }

}
