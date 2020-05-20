package com.dadalong.autotest.bean.v1.wrapper;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.Notice;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class NoticeWrapper extends QueryWrapper<Notice> {

    /**
     * 显示公告列表，包含筛选查询功能
     * @param request
     * @return
     */
    public NoticeWrapper ofListWithSearch(SearchRequest request){
        Map<String,Object> map = request.getSearch();
        Object userId = map.get("user");
        this.ofUserId(userId);
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
     * 根据操作起止时间段筛选操作日志
     * @param startTime
     * @param endTime
     * @return
     */
    public NoticeWrapper ofTime(Object startTime, Object endTime){
        if(startTime != null && endTime != null && StringUtils.isNotBlank(startTime.toString()) && StringUtils.isNotBlank(endTime.toString())){
            this.between("created_at",startTime,endTime);
            return this;
        }
        return this;
    }

    /**
     * 根据user_id精确筛选操作日志
     * @param userId
     * @return
     */
    public NoticeWrapper ofUserId(Object userId){
        if(userId != null && StringUtils.isNotBlank(userId.toString())){
            this.eq("user_id", userId);
            return this;
        }
        return this;
    }

}
