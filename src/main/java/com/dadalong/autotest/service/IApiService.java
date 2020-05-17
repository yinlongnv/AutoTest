package com.dadalong.autotest.service;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.model.api.BatchDTO;
import com.dadalong.autotest.model.api.CreateOrEditApiDTO;
import com.dadalong.autotest.model.api.DetailDTO;
import com.dadalong.autotest.model.api.UploadDTO;
import com.dadalong.autotest.model.response.ApiListResponse;

public interface IApiService {

    /**
     * 获取接口列表，可同时筛选搜索条件
     * @param searchRequest
     * @return
     */
    public IPage<ApiListResponse> listWithSearch(SearchRequest searchRequest);

    /**
     * 创建/编辑接口，以Id区分是创建还是编辑
     * @param createOrEditApiDTO 从前端传回来的json格式数据转换的对象
     */
    public void createOrEditApi(CreateOrEditApiDTO createOrEditApiDTO);

    /**
     * (批量)删除接口
     * @param batchDTO
     */
    public void deleteBatch(BatchDTO batchDTO);

    /**
     * 查看接口详情
     * @param detailDTO
     * @return
     */
    public ApiListResponse detail(DetailDTO detailDTO);

    /**
     * 接收批量导入的html
     */
    public String upload(UploadDTO uploadDTO);

    /**
     * 下载模板的模板数据，取数据库第一条
     */
    public Api exportDemo();

}
