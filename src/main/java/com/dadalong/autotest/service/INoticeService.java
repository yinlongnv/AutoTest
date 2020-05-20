package com.dadalong.autotest.service;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.response.LogListResponse;

public interface INoticeService {

    /**
     * 获取通知公告列表，可同时筛选搜索条件
     * @param searchRequest
     * @return
     */
    IPage<LogListResponse> listWithSearch(SearchRequest searchRequest);

}
