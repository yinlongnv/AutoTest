package com.dadalong.autotest.bean.v1.wrapper;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.OperateLog;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class OperateLogWrapper extends QueryWrapper<OperateLog> {

    /**
     * 显示操作日志列表，包含筛选查询功能
     * @param request
     * @return
     */
    public OperateLogWrapper ofListWithSearch(SearchRequest request){
        Map<String,Object> map = request.getSearch();
        Object startTime = map.get("startTime");
        Object endTime = map.get("endTime");
        this.ofTime(startTime, endTime);
        Object search = map.get("logInfo");
        if (search != null && StringUtils.isNotBlank(search.toString())) {
            this.like("log_content", search.toString()).or().like("operate_path", search.toString());
        }
        return this;
    }

    /**
     * 筛选操作起止时间段
     * @param startTime
     * @param endTime
     * @return
     */
    public OperateLogWrapper ofTime(Object startTime,Object endTime){
        if(startTime != null && endTime != null && StringUtils.isNotBlank(startTime.toString()) && StringUtils.isNotBlank(endTime.toString())){
            this.between("created_at",startTime,endTime);
            return this;
        }
        return this;
    }

}
