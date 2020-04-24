package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import cn.com.dbapp.slab.java.commons.utils.ConverterUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dadalong.autotest.bean.v1.mapper.ApiMapper;
import com.dadalong.autotest.bean.v1.mapper.TestCaseMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.pojo.TestCase;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import com.dadalong.autotest.bean.v1.wrapper.TestCaseWrapper;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.response.TestCaseListResponse;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.service.ITestCaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 78089 on 2020/4/24.
 */
@Service
@Transactional
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCase> implements ITestCaseService {

    @Resource
    private TestCaseMapper testCaseMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ApiMapper apiMapper;

    /**
     * 设置每页10条记录
     */
    private static final Integer size = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiServiceImpl.class);

    /**
     * 获取用例列表，包含筛选查询
     * @param searchRequest
     * @return
     */
    @Override
    public IPage<TestCaseListResponse> listWithSearch(SearchRequest searchRequest) {
        try {
            TestCaseWrapper wrapper = new TestCaseWrapper();
//            UserWrapper userWrapper = new UserWrapper();
            wrapper.ofListWithSearch(searchRequest);
            List<TestCaseListResponse> testCaseListResponseList = new ArrayList<>();

            SlabPage<TestCase> testCaseSlabPage = new SlabPage<>(searchRequest);
            IPage<TestCase> testCaseResults = testCaseMapper.selectPage(testCaseSlabPage,wrapper);
            for (TestCase record : testCaseResults.getRecords()) {
                User createdBy = userMapper.selectById(record.getUserId());
                User username = userMapper.selectById(record.getExecuteByUserId());
                Api api = apiMapper.selectById(record.getApiId());
//                TestCaseListResponse testCaseListResponse = ConverterUtil.getTranslate(record, new TestCaseListResponse());
                TestCaseListResponse testCaseListResponse = new TestCaseListResponse();
                BeanUtils.copyProperties(record, testCaseListResponse);
                testCaseListResponse.setCreatedBy(createdBy.getUsername());
                testCaseListResponse.setUsername(username.getUsername());
                testCaseListResponse.setLastExecuteTime(record.getUpdatedAt());//时间未格式化
                testCaseListResponse.setApiName(api.getApiName());
                testCaseListResponseList.add(testCaseListResponse);
            }
            SlabPage<TestCaseListResponse> testCaseListResponseSlabPage = new SlabPage<>(searchRequest);
            testCaseListResponseSlabPage.setRecords(testCaseListResponseList);
            testCaseListResponseSlabPage.setTotal(testCaseResults.getTotal());
            return testCaseListResponseSlabPage;

        }catch (Exception e){
            LOGGER.error("listWithSearchError",e);
            throw new ConflictException("listWithSearchError");//暂时没啥用
        }
    }

    /**
     * (批量)删除用例
     * @param caseIds
     */
    @Override
    public void deleteBatch(List<Integer> caseIds) {
        try {
            removeByIds(caseIds);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public TestCaseListResponse detail(Integer id) {
        TestCaseListResponse testCaseListResponse = new TestCaseListResponse();
        TestCase testCase = testCaseMapper.selectById(id);
        BeanUtils.copyProperties(testCase, testCaseListResponse);

        return testCaseListResponse;
    }

}
