package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.lang.LangUtil;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.model.ApiData;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.model.user.LoginDTO;
import com.dadalong.autotest.service.IUserService;
import com.dadalong.autotest.utils.CreateUserNumberUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.alibaba.fastjson.JSONObject;
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
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 设置每页10条记录
     */
    private static final Integer size = 10;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final LangUtil langUtil;
    public UserServiceImpl() {
        this.langUtil = new LangUtil();
    }

    /**
     * 用户登录，更新上次登录IP信息，更新最后登录时间信息，更新登录次数
     * @param loginDTO
     * @return
     */
    @Override
    public User login(LoginDTO loginDTO) {
        UserWrapper userWrapper = new UserWrapper();
        User user = new User();
        user = userMapper.selectOne(userWrapper.ofUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword()));
        if (user != null) {
            user.setLastIp(loginDTO.getLastIp());
            user.setLastLogin(new Date());
            Integer loginCount = user.getLoginCount() + 1;
            user.setLoginCount(loginCount);
            userMapper.updateById(user);
            return user;
        } else {
            return null;
        }
    }

    /**
     * 获取用户列表，包含筛选查询
     * @param searchRequest
     * @return
     */
    public IPage<User> listWithSearch(SearchRequest searchRequest){
        try {
            UserWrapper wrapper = new UserWrapper();
            wrapper.ofListWithSearch(searchRequest);
            SlabPage<User> userSlabPage = new SlabPage<>(searchRequest);
            return page(userSlabPage, wrapper);
        }catch (Exception e){
            LOGGER.error("listWithSearchError",e);
            throw new ConflictException("listWithSearchError");//暂时没啥用
        }
    }

    /**
     * 创建/编辑用户，以userId区分是创建还是编辑
     * @param createOrEditUserDTO 从前端传回来的json格式数据转换的对象
     */
    @Override
    public void createOrEditUser(CreateOrEditUserDTO createOrEditUserDTO) {
        User user = new User();
        //随机生成用户编号
        CreateUserNumberUtils createUserNumberUtils = new CreateUserNumberUtils();
        user.setUsername(createOrEditUserDTO.getUsername());
        user.setRole(createOrEditUserDTO.getRole());
        user.setIdNumber(createOrEditUserDTO.getIdNumber());
        user.setPhoneNumber(createOrEditUserDTO.getPhoneNumber());
        user.setEmail(createOrEditUserDTO.getEmail());
        user.setPassword(createOrEditUserDTO.getPassword());
        if(createOrEditUserDTO.getUserId() != null && createOrEditUserDTO.getUserId() != 0){
            user.setUserNumber(createUserNumberUtils.createUserNumber());
            user.setUserId(createOrEditUserDTO.getUserId());
            userMapper.insert(user);
        }else {
            userMapper.updateById(user);
        }
    }

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

    /**
     * (批量)禁用用户
     * @param userIds
     */
    @Override
    public void disableBatch(List<Integer> userIds) {
        Map<String,Object> map = new HashMap<>();
        for(Integer userId : userIds){
            map.put("id",userId);
            List<User> users = userMapper.selectByMap(map);
            users.get(0).setStatus(1);
            userMapper.updateById(users.get(0));
        }
    }

    /**
     * (批量)启用用户
     * @param userIds
     */
    @Override
    public void enableBatch(List<Integer> userIds) {
        Map<String,Object> map = new HashMap<>();
        for(Integer userId : userIds){
            map.put("id",userId);
            List<User> users = userMapper.selectByMap(map);
            users.get(0).setStatus(0);
            userMapper.updateById(users.get(0));
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
