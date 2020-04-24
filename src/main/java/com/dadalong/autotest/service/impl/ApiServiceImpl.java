package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.lang.LangUtil;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import cn.com.dbapp.slab.java.commons.utils.ConverterUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dadalong.autotest.bean.v1.mapper.ApiMapper;
import com.dadalong.autotest.bean.v1.mapper.TestCaseMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.model.ApiData;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.model.user.LoginDTO;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.service.ITestCaseService;
import com.dadalong.autotest.service.IUserService;
import com.dadalong.autotest.utils.CreateUserNumberUtils;
import com.dadalong.autotest.utils.UniqueJudgementUtils;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Date;
import java.util.*;

@Service
@Transactional
public class ApiServiceImpl extends ServiceImpl<ApiMapper,Api> implements IApiService {

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 设置每页10条记录
     */
    private static final Integer size = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiServiceImpl.class);

    /**
     * 获取用户列表，包含筛选查询
     * @param searchRequest
     * @return
     */
    public IPage<ApiListResponse> listWithSearch(SearchRequest searchRequest){
        try {
            ApiWrapper wrapper = new ApiWrapper();
//            UserWrapper userWrapper = new UserWrapper();
            wrapper.ofListWithSearch(searchRequest);
            List<ApiListResponse> apiListResponseList = new ArrayList<>();

            SlabPage<Api> apiSlabPage = new SlabPage<>(searchRequest);
            IPage<Api> apiResults = apiMapper.selectPage(apiSlabPage,wrapper);
            for (Api record : apiResults.getRecords()) {
                User user = userMapper.selectById(record.getUserId());
//                ApiListResponse apiListResponse = ConverterUtil.getTranslate(record, new ApiListResponse());
                ApiListResponse apiListResponse = new ApiListResponse();
//                apiListResponse.setId(record.getId());
//                apiListResponse.setApiPath(record.getApiPath());
//                apiListResponse.setApiName(record.getApiName());
//                apiListResponse.setReqMethod(record.getReqMethod());
//                apiListResponse.setProjectName(record.getProjectName());
//                apiListResponse.setApiGroup(record.getApiGroup());
                BeanUtils.copyProperties(record, apiListResponse);
                apiListResponse.setCreatedBy(user.getUsername());
                apiListResponseList.add(apiListResponse);
            }
            SlabPage<ApiListResponse> apiListResponseSlabPage = new SlabPage<>(searchRequest);
            apiListResponseSlabPage.setRecords(apiListResponseList);
            apiListResponseSlabPage.setTotal(apiResults.getTotal());
            return apiListResponseSlabPage;
        }catch (Exception e){
            LOGGER.error("listWithSearchError",e);
            throw new ConflictException("listWithSearchError");//暂时没啥用
        }
    }

    @Override
    public List<String> getProjectNameList() {
        ApiWrapper wrapper = new ApiWrapper();
        List<String> projectNameList = new ArrayList<>();
        apiMapper.selectList(wrapper.getProjectName()).forEach(api -> {
            projectNameList.add(api.getProjectName());
        });
        return projectNameList;
    }

    @Override
    public void createOrEditApi(CreateOrEditUserDTO createOrEditUserDTO) {

    }

    @Override
    public void deleteBatch(List<Integer> userIds) {

    }

    @Override
    public String handleUploadedFile(MultipartFile file) throws IOException {
        return null;
    }


}
