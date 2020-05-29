package com.dadalong.autotest.bean.v1.wrapper;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.Api;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class ApiWrapper extends QueryWrapper<Api> {

    /**
     * 显示接口列表，包含筛选查询功能
     * @param request
     * @return
     */
    public ApiWrapper ofListWithSearch(SearchRequest request){
        Map<String,Object> map = request.getSearch();
        Object search = map.get("apiName");
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
     * 筛选接口业务
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
     * 筛选所属分组
     * @param apiGroup
     * @return
     */
    public ApiWrapper ofApiGroup(Object apiGroup){
        if(apiGroup != null && StringUtils.isNotBlank(apiGroup.toString())){
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
            this.eq("req_method",reqMethod.toString());
        }
        return this;
    }

    /**
     * 根据这四项精确定位唯一的api
     * @param api
     * @return
     */
    public ApiWrapper ofApiId(Api api){
        this.eq("project_name", api.getProjectName())
                .eq("api_group", api.getApiGroup())
                .eq("api_name", api.getApiName())
                .eq("api_path", api.getApiPath());
        return this;
    }

}
