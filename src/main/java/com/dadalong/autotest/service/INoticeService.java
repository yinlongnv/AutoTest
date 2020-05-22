package com.dadalong.autotest.service;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.notice.DetailDTO;
import com.dadalong.autotest.model.response.NoticeListResponse;

public interface INoticeService {

    /**
     * 获取通知公告列表，可同时筛选搜索条件
     * @param searchRequest
     * @return
     */
    IPage<NoticeListResponse> listWithSearch(SearchRequest searchRequest);

    /**
     * 标记为全部已读
     * @param userId
     * @return
     */
    Boolean markReadAll(Integer userId);

    /**
     * 查看通知公告详情
     * @param detailDTO
     * @return
     */
    NoticeListResponse detail(DetailDTO detailDTO);
}
