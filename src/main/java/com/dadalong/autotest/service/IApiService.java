package com.dadalong.autotest.service;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.api.CreateOrEditApiDTO;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.response.ApiNameListResponse;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.model.user.LoginDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


public interface IApiService {

    /**
     * 获取接口列表，可同时筛选搜索条件
     * @param searchRequest
     * @return
     */
    public IPage<ApiListResponse> listWithSearch(SearchRequest searchRequest);

    /**
     * 获取所有接口业务筛选项列表
     * @return
     */
    public List<String> getProjectNameList();

    /**
     * 获取所有所属分组筛选项列表
     * @return
     */
    public List<String> getApiGroupList();

    /**
     * 获取所有请求方法筛选项列表
     * @return
     */
    public List<String> getReqMethodList();

    /**
     * 获取所有接口名称筛选项列表
     * @return
     */
    public List<ApiNameListResponse> getApiNameList();

    /**
     * 创建/编辑接口，以Id区分是创建还是编辑
     * @param createOrEditApiDTO 从前端传回来的json格式数据转换的对象
     */
    public void createOrEditApi(CreateOrEditApiDTO createOrEditApiDTO);

    /**
     * (批量)删除接口
     * @param apiIds
     */
    public void deleteBatch(List<Integer> apiIds);

    /**
     * 接收上传的json文件
     * @param file
     * @return
     * @throws IOException
     */
    public String handleUploadedFile(MultipartFile file) throws IOException;
}
