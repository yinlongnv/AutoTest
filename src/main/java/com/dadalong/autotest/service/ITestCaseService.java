package com.dadalong.autotest.service;


import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.response.TestCaseListResponse;
import com.dadalong.autotest.model.testCase.CreateOrEditCaseDTO;
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

    /**
     * 创建/编辑用例，以Id区分是创建还是编辑
     * @param createOrEditCaseDTO 从前端传回来的json格式数据转换的对象
     */
    public void createOrEditCase(CreateOrEditCaseDTO createOrEditCaseDTO);

    /**
     * (批量)删除用例
     * @param caseIds
     */
    public void deleteBatch(List<Integer> caseIds);

    /**
     * 查看用例详情
     * @param id
     */
    public TestCaseListResponse detail(Integer id);

}
