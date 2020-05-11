package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dadalong.autotest.bean.v1.mapper.ApiMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.model.ApiData;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import com.dadalong.autotest.model.api.BatchDTO;
import com.dadalong.autotest.model.api.CreateOrEditApiDTO;
import com.dadalong.autotest.model.api.DetailDTO;
import com.dadalong.autotest.model.response.*;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.utils.InsertOperateLogUtils;
import com.dadalong.autotest.utils.LogContentEnumUtils;
import com.dadalong.autotest.utils.OperatePathEnumUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class ApiServiceImpl extends ServiceImpl<ApiMapper,Api> implements IApiService {

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    InsertOperateLogUtils insertOperateLogUtils;

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
            wrapper.ofListWithSearch(searchRequest).orderByDesc("created_at");
            List<ApiListResponse> apiListResponseList = new ArrayList<>();
            Map<String,Object> map = searchRequest.getSearch();

            SlabPage<Api> apiSlabPage = new SlabPage<>(searchRequest);
            IPage<Api> apiResults = apiMapper.selectPage(apiSlabPage,wrapper);
            for (Api record : apiResults.getRecords()) {
                User user = userMapper.selectById(record.getUserId());
                ApiListResponse apiListResponse = new ApiListResponse();
                BeanUtils.copyProperties(record, apiListResponse);
                if(user != null && StringUtils.isNotBlank(user.toString())) {
                    apiListResponse.setCreatedBy(user.getUsername());
                } else {
                    apiListResponse.setCreatedBy("root");
                }
                apiListResponseList.add(apiListResponse);
            }
            SlabPage<ApiListResponse> apiListResponseSlabPage = new SlabPage<>(searchRequest);
            apiListResponseSlabPage.setRecords(apiListResponseList);
            apiListResponseSlabPage.setTotal(apiResults.getTotal());

            Object userId = map.get("userId");
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(Integer.parseInt(String.valueOf(userId)), LogContentEnumUtils.APILIST, OperatePathEnumUtils.APILIST);
            return apiListResponseSlabPage;
        }catch (Exception e){
            LOGGER.error("listWithSearchError",e);
            throw new ConflictException("listWithSearchError");
        }
    }

    /**
     * 创建/编辑接口
     * @param createOrEditApiDTO 从前端传回来的json格式数据转换的对象
     */
    @Override
    public void createOrEditApi(CreateOrEditApiDTO createOrEditApiDTO) {
        Api api = new Api();
        BeanUtils.copyProperties(createOrEditApiDTO, api, "userId");

//        System.out.println("++++++++++复制接口属性不传userID:"+ api.getUserId());
        if(createOrEditApiDTO.getId() == null) {
            api.setUserId(createOrEditApiDTO.getUserId());
//            System.out.println("++++++++++创建接口时给予userID:"+ api.getUserId());
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(createOrEditApiDTO.getUserId(), LogContentEnumUtils.APICREATE, OperatePathEnumUtils.APICREATE);
            apiMapper.insert(api);
        } else {
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(createOrEditApiDTO.getUserId(), LogContentEnumUtils.APIEDIT, OperatePathEnumUtils.APIEDIT);
            apiMapper.updateById(api);
        }
    }

    /**
     * (批量)删除接口
     * @param batchDTO
     */
    @Override
    public void deleteBatch(BatchDTO batchDTO) {
        try {
            removeByIds(batchDTO.getApiIds());
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(batchDTO.getUserId(), LogContentEnumUtils.APIDELETE, OperatePathEnumUtils.APIDELETE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取接口详情
     * @param detailDTO
     * @return
     */
    @Override
    public ApiListResponse detail(DetailDTO detailDTO) {
        ApiListResponse apiListResponse = new ApiListResponse();
        Api api = apiMapper.selectById(detailDTO.getId());
        BeanUtils.copyProperties(api, apiListResponse);
        User user = userMapper.selectById(api.getUserId());
        if (user != null && StringUtils.isNotBlank(user.toString())) {
            String username = user.getUsername();
            apiListResponse.setCreatedBy(username);
        } else {
            apiListResponse.setCreatedBy("root");
        }
        //插入操作日志
        insertOperateLogUtils.insertOperateLog(detailDTO.getUserId(), LogContentEnumUtils.APIDETAIL, OperatePathEnumUtils.APIDETAIL);
        return apiListResponse;
    }

    @Override
    public String handleUploadedFile(MultipartFile file) throws IOException {
        String content = new String(file.getBytes());
        System.out.println("++++++++++++json文件字符串"+ content);
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
            apiData = JSONObject.parseObject(jsonObject.toString(), ApiData.class);
            apis.add(apiData);
        }
        return apis;
    }

}
