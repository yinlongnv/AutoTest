package com.dadalong.autotest.service;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.response.LogListResponse;

import java.util.List;

public interface IOperateLogService {

    /**
     * 获取操作日志列表，可同时筛选搜索条件
     * @param searchRequest
     * @return
     */
    IPage<LogListResponse> listWithSearch(SearchRequest searchRequest);

    /**
     * 导出全部操作日志数据
     * @param searchRequest
     * @return
     */
    List<LogListResponse> exportAllLogs(SearchRequest searchRequest);
}
