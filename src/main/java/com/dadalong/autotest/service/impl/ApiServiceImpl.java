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
import com.dadalong.autotest.model.api.CreateOrEditApiDTO;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.response.ApiNameListResponse;
import com.dadalong.autotest.model.response.FilterMapResponse;
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
     * 获取接口列表，包含筛选查询
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

    /**
     * 获取所有接口业务筛选项列表
     * @return
     */
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
    public List<String> getApiGroupList() {
        ApiWrapper wrapper = new ApiWrapper();
        List<String> apiGroupList = new ArrayList<>();
        apiMapper.selectList(wrapper.getApiGroup()).forEach(api -> {
            apiGroupList.add(api.getApiGroup());
        });
        return apiGroupList;
    }

    /**
     * 获取所有请求方法筛选项列表
     * @return
     */
    @Override
    public List<String> getReqMethodList() {
        ApiWrapper wrapper = new ApiWrapper();
        List<String> reqMethodList = new ArrayList<>();
        apiMapper.selectList(wrapper.getReqMethod()).forEach(api -> {
            reqMethodList.add(api.getReqMethod());
        });
        return reqMethodList;
    }

    @Override
    public List<ApiNameListResponse> getApiNameList() {
        ApiWrapper wrapper = new ApiWrapper();
        List<ApiNameListResponse> apiNameListResponses = new ArrayList<>();
        apiMapper.selectList(wrapper.getApiName()).forEach(api -> {
            ApiNameListResponse apiNameListResponse = new ApiNameListResponse();
            apiNameListResponse.setApiId(api.getId());
            apiNameListResponse.setApiName(api.getApiName());
            apiNameListResponses.add(apiNameListResponse);
        });
        return apiNameListResponses;
    }

    /**
     * 创建/编辑接口
     * @param createOrEditApiDTO 从前端传回来的json格式数据转换的对象
     */
    @Override
    public void createOrEditApi(CreateOrEditApiDTO createOrEditApiDTO) {
        UniqueJudgementUtils uniqueJudgementUtils = new UniqueJudgementUtils();
        Api api = new Api();
        BeanUtils.copyProperties(createOrEditApiDTO, api);
        if(createOrEditApiDTO.getId() == null) {
            if (!uniqueJudgementUtils.ifApiNameExist(api.getApiName())) {
                api.setUserId(createOrEditApiDTO.getUserId());
                apiMapper.insert(api);
            } else {
                throw new ConflictException("接口名称已存在");
            }
        } else if (!uniqueJudgementUtils.ifApiNameExist(api.getApiName())){
            api.setId(createOrEditApiDTO.getId());
            apiMapper.updateById(api);
        } else {
            throw new ConflictException("接口名称已存在");
        }
    }

    /**
     * (批量)删除接口
     * @param apiIds
     */
    @Override
    public void deleteBatch(List<Integer> apiIds) {
        try {
            removeByIds(apiIds);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取接口详情
     * @param id
     * @return
     */
    @Override
    public ApiListResponse detail(Integer id) {
        ApiListResponse apiListResponse = new ApiListResponse();
        Api api = apiMapper.selectById(id);
        BeanUtils.copyProperties(api, apiListResponse);
        String username = userMapper.selectById(api.getUserId()).getUsername();
        apiListResponse.setCreatedBy(username);
        return apiListResponse;
    }

    @Override
    public FilterMapResponse filterMap() {
        FilterMapResponse filterMapResponse = new FilterMapResponse();
        Map<Map<String,String>,Map<String,String>> result = new HashMap<>();
        List<Api> apiList = apiMapper.selectList(new ApiWrapper().groupBy("project_name").groupBy("api_group"));
        apiList.forEach(api -> {
            System.out.println(api);
        });
        String projectName = null;
        String baseUrl = null;
        Map<String,String> key = new HashMap<>();
        Map<String,String> value = new HashMap<>();
        String oldProjectName = apiList.get(0).getProjectName();
        String oldBaseUrl = apiList.get(0).getBaseUrl();
        for (Api api : apiList) {
            projectName = api.getProjectName();
            baseUrl = api.getBaseUrl();
            if(api.getProjectName().equals(oldProjectName)){
                String merge = api.getApiName() + " " + api.getApiPath();
                value.put(api.getApiGroup(),merge);
            }else{
                key.put(oldProjectName,oldBaseUrl);
                result.put(key,value);
                oldProjectName = api.getProjectName();
                oldBaseUrl = api.getBaseUrl();
                key = new HashMap<>();
                value = new HashMap<>();
                String merge = api.getApiName() + " " + api.getApiPath();
                value.put(api.getApiGroup(),merge);
            }
        }
        key.put(projectName,baseUrl);
        result.put(key,value);
        filterMapResponse.setFilterMap(result);
        return filterMapResponse;
    }

    @Override
    public String handleUploadedFile(MultipartFile file) throws IOException {
        String content = new String(file.getBytes());
//        JsonArray jsonArry = new JsonParser().parse(content).getAsJsonArray();
//        System.out.println(jsonArry.get(0).getAsJsonObject().get("name").toString());
        List<ApiData> apiDatas = toDatabaseFromJson(content);
        System.out.println(apiDatas.get(0).getList().get(0).getReq_body_type());
        System.out.println(apiDatas.get(0).getList().get(0).getReq_body_type());
        System.out.println(apiDatas.get(1).getName());
        return null;
    }

    private List<ApiData> toDatabaseFromJson(String content){
        JsonArray jsonArray = new JsonParser().parse(content).getAsJsonArray();
        List<ApiData> apis = new LinkedList<>();
        ApiData apiData;
        for(int i = 0 ; i < jsonArray.size();i++){
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            apiData = JSONObject.parseObject(jsonObject.toString(),ApiData.class);
            apis.add(apiData);
        }
        return apis;
    }

}
