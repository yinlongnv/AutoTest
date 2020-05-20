package com.dadalong.autotest.service;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.api.*;
import com.dadalong.autotest.model.response.ApiListResponse;

public interface IApiService {

    /**
     * 获取接口列表，可同时筛选搜索条件
     * @param searchRequest
     * @return
     */
    IPage<ApiListResponse> listWithSearch(SearchRequest searchRequest);

    /**
     * 创建/编辑接口，以Id区分是创建还是编辑
     * @param createOrEditApiDTO 从前端传回来的json格式数据转换的对象
     */
    String createOrEditApi(CreateOrEditApiDTO createOrEditApiDTO);

    /**
     * (批量)删除接口
     * @param batchDTO
     */
    void deleteBatch(BatchDTO batchDTO);

    /**
     * 查看接口详情
     * @param detailDTO
     * @return
     */
    ApiListResponse detail(DetailDTO detailDTO);

    /**
     * 批量导入的html或xlsx
     */
    String upload(UploadDTO uploadDTO);

    /**
     * 填入参数规则
     * @param caseRulesDTO
     * @return
     */
    Boolean putCaseRules(CaseRulesDTO caseRulesDTO);

    /**
     * 为指定api生成测试用例
     * @param createCasesDTO
     * @return
     */
    Boolean createCases(CreateCasesDTO createCasesDTO);
}
