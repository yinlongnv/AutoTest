package com.dadalong.autotest.service;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.api.BatchDTO;
import com.dadalong.autotest.model.api.CreateOrEditApiDTO;
import com.dadalong.autotest.model.api.DetailDTO;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.response.ApiNameListResponse;
import com.dadalong.autotest.model.response.FilterMapResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


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
     * 接收上传的json文件
     * @param file
     * @return
     * @throws IOException
     */
    public String handleUploadedFile(MultipartFile file) throws IOException;
}
