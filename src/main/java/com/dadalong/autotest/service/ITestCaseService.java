package com.dadalong.autotest.service;


import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.response.TestCaseListResponse;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;

import java.sql.Date;
import java.util.List;


public interface ITestCaseService {

    /**
     * 获取用例列表，可同时筛选搜索条件
     * @param searchRequest
     * @return
     */
    public IPage<TestCaseListResponse> listWithSearch(SearchRequest searchRequest);


}
