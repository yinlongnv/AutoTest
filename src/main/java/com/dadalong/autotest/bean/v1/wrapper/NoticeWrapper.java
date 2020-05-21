package com.dadalong.autotest.bean.v1.wrapper;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.Notice;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class NoticeWrapper extends QueryWrapper<Notice> {

    /**
     * 显示通知公告列表，包含筛选查询功能
     * @param request
     * @return
     */
    public NoticeWrapper ofListWithSearch(SearchRequest request){
        Map<String,Object> map = request.getSearch();
        Object startTime = map.get("startTime");
        Object endTime = map.get("endTime");
        this.ofTime(startTime, endTime);
        List<Integer> noticeIds = (List<Integer>) map.get("noticeIds");
        for (Object noticeId : noticeIds) {
            if (noticeId != null && StringUtils.isNotBlank(noticeId.toString())) {
                this.eq("notice_id", Integer.parseInt(noticeId.toString())).orderByDesc("created_at");
            }
        }
        return this;
    }

    /**
     * 根据操作起止时间段筛选通知公告
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

}
