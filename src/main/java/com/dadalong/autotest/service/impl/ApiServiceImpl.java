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
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.model.ApiData;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.model.user.LoginDTO;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.service.IUserService;
import com.dadalong.autotest.utils.CreateUserNumberUtils;
import com.dadalong.autotest.utils.UniqueJudgementUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public IPage<Api> listWithSearch(SearchRequest searchRequest){
        try {
            ApiWrapper wrapper = new ApiWrapper();
            wrapper.ofListWithSearch(searchRequest);
            SlabPage<Api> apiSlabPage = new SlabPage<>(searchRequest);
            return page(apiSlabPage, wrapper);
        }catch (Exception e){
            LOGGER.error("listWithSearchError",e);
            throw new ConflictException("listWithSearchError");//暂时没啥用
        }
    }

    @Override
    public void createOrEditApi(CreateOrEditUserDTO createOrEditUserDTO) {

    }

    /**
     * 创建/编辑用户，以userId区分是创建还是编辑
     * @param createOrEditUserDTO 从前端传回来的json格式数据转换的对象
     */
//    @Override
//    public void createOrEditUser(CreateOrEditUserDTO createOrEditUserDTO) {
//        UniqueJudgementUtils uniqueJudgementUtils = new UniqueJudgementUtils();
////        UserWrapper userWrapper = new UserWrapper();
//        Api api;
//        //随机生成用户编号
//        CreateUserNumberUtils createUserNumberUtils = new CreateUserNumberUtils();
////        user.setUsername(createOrEditUserDTO.getUsername());
////        user.setRole(createOrEditUserDTO.getRole());
////        user.setIdNumber(createOrEditUserDTO.getIdNumber());
////        user.setPhoneNumber(createOrEditUserDTO.getPhoneNumber());
////        user.setEmail(createOrEditUserDTO.getEmail());
////        user.setPassword(createOrEditUserDTO.getPassword());
//        api = ConverterUtil.getTranslate(createOrEditUserDTO, new Api());
////        if (!uniqueJudgementUtils.ifUsernameExist(createOrEditUserDTO.getUsername())) {
////            saveOrUpdate(user);
////        } else {
////            throw new ConflictException("用户名已存在");
////        }
//        if(createOrEditUserDTO.getId() == null) {
//            if (!uniqueJudgementUtils.ifUsernameExist(api.getApiName())) {
////                api.setUserNumber(createUserNumberUtils.createUserNumber());
//                api.setUserId(createOrEditUserDTO.getUserId());
//                apiMapper.insert(api);
//            } else {
//                throw new ConflictException("用户名已存在");
//            }
//        } else if (!uniqueJudgementUtils.ifUsernameExist(api.getApiName())){
//            api.setId(createOrEditUserDTO.getId());
//            apiMapper.updateById(api);
//        } else {
//            throw new ConflictException("用户名已存在");
//        }
//    }

    /**
     * (批量)删除用户
     * @param userIds
     */
    @Override
    public void deleteBatch(List<Integer> userIds) {
        try {
            removeByIds(userIds);
        }catch (Exception e){
            e.printStackTrace();
        }
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
