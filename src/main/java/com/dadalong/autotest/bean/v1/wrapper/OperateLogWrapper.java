package com.dadalong.autotest.bean.v1.wrapper;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.OperateLog;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.response.LogListResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

public class OperateLogWrapper extends QueryWrapper<OperateLog> {
    /**
     * 显示操作日志列表，包含筛选查询功能
     * @param request
     * @return
     */
    public OperateLogWrapper ofListWithSearch(SearchRequest request){
        Map<String,Object> map = request.getSearch();
        Object search = map.get("logInfo");
        if (search != null && StringUtils.isNotBlank(search.toString())) {
            this.like("log_content", search.toString()).or().like("operate_path", search.toString());
        }
        Object userId = map.get("userId");
        this.ofUserId(userId);
//        Object apiGroup = map.get("apiGroup");
//        this.ofApiGroup(apiGroup);
//        Object reqMethod = map.get("reqMethod");
//        this.ofReqMethod(reqMethod);
        return this;
    }

    /**
     * 筛选用户id
     * @param userId
     * @return
     */
    public OperateLogWrapper ofUserId(Object userId){
        if(userId != null && StringUtils.isNotBlank(userId.toString())){
            System.out.println(userId.toString());
            this.eq("user_id",Integer.parseInt(userId.toString()));
        }
        return this;
    }

}
