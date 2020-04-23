package com.dadalong.autotest.service;


import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.model.user.LoginDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.List;


public interface IApiService {


    /**
     * 获取接口列表，可同时筛选搜索条件
     * @param searchRequest
     * @return
     */
    public IPage<Api> listWithSearch(SearchRequest searchRequest);
//    public Page<User> listWithSearch(ListWithSearchDTO listWithSearchDTO);

    /**
     * 创建/编辑用户，以userId区分是创建还是编辑
     * @param createOrEditUserDTO 从前端传回来的json格式数据转换的对象
     */
    public void createOrEditUser(CreateOrEditUserDTO createOrEditUserDTO);

    /**
     * (批量)删除用户
     * @param userIds
     */
    public void deleteBatch(List<Integer> userIds);

    /**
     * 接收上传的json文件
     * @param file
     * @return
     * @throws IOException
     */
    public String handleUploadedFile(MultipartFile file) throws IOException;
}
