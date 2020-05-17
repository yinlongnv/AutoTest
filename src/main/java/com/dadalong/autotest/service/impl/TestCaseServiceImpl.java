package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
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
import com.dadalong.autotest.model.response.TestCaseListResponse;
import com.dadalong.autotest.model.testCase.BatchDTO;
import com.dadalong.autotest.model.testCase.CreateOrEditCaseDTO;
import com.dadalong.autotest.model.testCase.DetailDTO;
import com.dadalong.autotest.service.ITestCaseService;
import com.dadalong.autotest.utils.InsertOperateLogUtils;
import com.dadalong.autotest.utils.LogContentEnumUtils;
import com.dadalong.autotest.utils.OperatePathEnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Resource
    InsertOperateLogUtils insertOperateLogUtils;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiServiceImpl.class);

    /**
     * 获取用例列表，包含筛选查询
     * @param searchRequest
     * @return
     */
    @Override
    public IPage<TestCaseListResponse> listWithSearch(SearchRequest searchRequest) {
        try {
            TestCaseWrapper testCaseWrapper = new TestCaseWrapper();
            ApiWrapper apiWrapper = new ApiWrapper();

            Map<String,Object> map = searchRequest.getSearch();
            if (StringUtils.isNotBlank(map.get("projectName").toString())) {
                Api a = new Api();
                a.setProjectName(map.get("projectName").toString());
                a.setApiGroup(map.get("apiGroup").toString());
                String apiInfo[] = map.get("apiMerge").toString().split(" ");
                a.setApiName(apiInfo[0]);
                a.setApiPath(apiInfo[1]);
                apiWrapper.ofApiId(a);
                Integer apiId = apiMapper.selectOne(apiWrapper).getId();
                searchRequest.setSearch("apiId", apiId);
            }
            testCaseWrapper.ofListWithSearch(searchRequest).orderByDesc("created_at");
            List<TestCaseListResponse> testCaseListResponseList = new ArrayList<>();

            SlabPage<TestCase> testCaseSlabPage = new SlabPage<>(searchRequest);
            IPage<TestCase> testCaseResults = testCaseMapper.selectPage(testCaseSlabPage,testCaseWrapper);
            for (TestCase record : testCaseResults.getRecords()) {
                Api api = apiMapper.selectById(record.getApiId());
                TestCaseListResponse testCaseListResponse = new TestCaseListResponse();
                BeanUtils.copyProperties(api, testCaseListResponse, "id");
                String merge = api.getApiName() + " " + api.getApiPath();
                testCaseListResponse.setApiMerge(merge);
                BeanUtils.copyProperties(record, testCaseListResponse);
                User createdBy = userMapper.selectById(record.getUserId());
                User username = userMapper.selectById(record.getExecuteByUserId());
                if(createdBy != null && StringUtils.isNotBlank(createdBy.toString())) {
                    testCaseListResponse.setCreatedBy(createdBy.getUsername());
                } else {
                    testCaseListResponse.setCreatedBy("root");
                }
                if(username != null && StringUtils.isNotBlank(username.toString())) {
                    testCaseListResponse.setUsername(username.getUsername());
                } else {
                    testCaseListResponse.setUsername("root");
                }
                testCaseListResponse.setLastExecuteTime(record.getUpdatedAt());
                testCaseListResponseList.add(testCaseListResponse);
            }
            SlabPage<TestCaseListResponse> testCaseListResponseSlabPage = new SlabPage<>(searchRequest);
            testCaseListResponseSlabPage.setRecords(testCaseListResponseList);
            testCaseListResponseSlabPage.setTotal(testCaseResults.getTotal());
            Object userId = map.get("userId");
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(Integer.parseInt(String.valueOf(userId)), LogContentEnumUtils.CASELIST, OperatePathEnumUtils.CASELIST);
            return testCaseListResponseSlabPage;
        }catch (Exception e){
            LOGGER.error("listWithSearchError",e);
            throw new ConflictException("listWithSearchError");
        }
    }

    @Override
    public void createOrEditCase(CreateOrEditCaseDTO createOrEditCaseDTO) {
        ApiWrapper apiWrapper = new ApiWrapper();
        TestCase testCase = new TestCase();
        BeanUtils.copyProperties(createOrEditCaseDTO, testCase, "userId");
        Api api = new Api();
        api.setProjectName(createOrEditCaseDTO.getProjectName());
        api.setApiGroup(createOrEditCaseDTO.getApiGroup());
        String apiInfo[] = createOrEditCaseDTO.getApiMerge().split(" ");
        api.setApiName(apiInfo[0]);
        api.setApiPath(apiInfo[1]);
        apiWrapper.ofApiId(api);
        Integer apiId = apiMapper.selectOne(apiWrapper).getId();//这里可能存在脏数据，暂未定位
        testCase.setApiId(apiId);
        if(createOrEditCaseDTO.getId() == null) {
            testCase.setExecuteStatus(1);
            testCase.setUserId(createOrEditCaseDTO.getUserId());
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(createOrEditCaseDTO.getUserId(), LogContentEnumUtils.CASECREATE, OperatePathEnumUtils.CASECREATE);
            testCaseMapper.insert(testCase);
        } else {
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(createOrEditCaseDTO.getUserId(), LogContentEnumUtils.CASEEDIT, OperatePathEnumUtils.CASEEDIT);
            testCaseMapper.updateById(testCase);
        }
    }

    /**
     * (批量)删除用例
     * @param batchDTO
     */
    @Override
    public void deleteBatch(BatchDTO batchDTO) {
        try {
            removeByIds(batchDTO.getCaseIds());
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(batchDTO.getUserId(), LogContentEnumUtils.CASEDELETE, OperatePathEnumUtils.CASEDELETE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 查看用例详情
     * @param detailDTO
     * @return
     */
    @Override
    public TestCaseListResponse detail(DetailDTO detailDTO) {
        TestCaseListResponse testCaseListResponse = new TestCaseListResponse();
        TestCase testCase = testCaseMapper.selectById(detailDTO.getId());
        BeanUtils.copyProperties(testCase, testCaseListResponse);
        Api api = apiMapper.selectById(testCase.getApiId());
        BeanUtils.copyProperties(api, testCaseListResponse, "id");
        String merge = api.getApiName() + " " + api.getApiPath();
        testCaseListResponse.setApiMerge(merge);
        User createdBy = userMapper.selectById(testCase.getUserId());
        User username = userMapper.selectById(testCase.getExecuteByUserId());
        if(createdBy != null && StringUtils.isNotBlank(createdBy.toString())) {
            testCaseListResponse.setCreatedBy(createdBy.getUsername());
        } else {
            testCaseListResponse.setCreatedBy("root");
        }
        if(username != null && StringUtils.isNotBlank(username.toString())) {
            testCaseListResponse.setUsername(username.getUsername());
        } else {
            testCaseListResponse.setUsername("root");
        }
        testCaseListResponse.setLastExecuteTime(testCase.getUpdatedAt());
        //插入操作日志
        insertOperateLogUtils.insertOperateLog(detailDTO.getUserId(), LogContentEnumUtils.CASEDETAIL, OperatePathEnumUtils.CASEDETAIL);
        return testCaseListResponse;
    }

}
