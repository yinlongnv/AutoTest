package com.dadalong.autotest.service;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.response.LogListResponse;

public interface IOperateLogService {

    /**
     * 获取操作日志列表，可同时筛选搜索条件
     * @param searchRequest
     * @return
     */
    public IPage<LogListResponse> listWithSearch(SearchRequest searchRequest);

}
