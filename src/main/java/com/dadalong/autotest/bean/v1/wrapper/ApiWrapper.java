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
        Object projectName = map.get("projectName");
        this.ofProjectName(projectName);
        Object apiGroup = map.get("apiGroup");
        this.ofApiGroup(apiGroup);
        Object reqMethod = map.get("reqMethod");
        this.ofReqMethod(reqMethod);
        return this;
    }

    /**
     * 获取所有接口业务筛选项列表
     * @param projectName
     * @return
     */
    public ApiWrapper ofProjectName(Object projectName){
        if(projectName != null && StringUtils.isNotBlank(projectName.toString())){
            System.out.println(projectName.toString());
            this.eq("project_name",projectName.toString());
        }
        return this;
    }

    /**
     * 获取所有所属分组筛选项列表
     * @param apiGroup
     * @return
     */
    public ApiWrapper ofApiGroup(Object apiGroup){
        if(apiGroup != null && StringUtils.isNotBlank(apiGroup.toString())){
            System.out.println(apiGroup.toString());
            this.eq("api_group",apiGroup.toString());
        }
        return this;
    }

    /**
     * 获取所有请求方法筛选项列表
     * @param reqMethod
     * @return
     */
    public ApiWrapper ofReqMethod(Object reqMethod){
        if(reqMethod != null && StringUtils.isNotBlank(reqMethod.toString())){
            System.out.println(reqMethod.toString());
            this.eq("req_method",reqMethod.toString());
        }
        return this;
    }

    /**
     * 获取所有project_name并去重
     * @return
     */
    public ApiWrapper getProjectName(){
        this.groupBy("project_name");
        return this;
    }

    /**
     * 获取所有api_group并去重
     * @return
     */
    public ApiWrapper getApiGroup(){
        this.groupBy("api_group");
        return this;
    }

    /**
     * 获取所有req_method并去重
     * @return
     */
    public ApiWrapper getReqMethod(){
        this.groupBy("req_method");
        return this;
    }

    /**
     * 获取所有api_name并去重
     * @return
     */
    public ApiWrapper getApiName(){
        this.groupBy("api_name");
        return this;
    }

}
