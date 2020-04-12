package com.dadalong.autotest.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.model.ApiData;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.model.user.ListWithSearchDTO;
import com.dadalong.autotest.service.IUserService;
import com.dadalong.autotest.utils.CreateUserNumberUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
//import org.json.JSONObject;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 设置每页10条记录
     */
    private static final Integer size = 10;

    @Override
    public Page<User> listWithSearch(ListWithSearchDTO listWithSearchDTO) {
        UserWrapper userWrapper = new UserWrapper();
        Page<User> pages = new Page<>(listWithSearchDTO.getPage(), size);
        System.out.println(listWithSearchDTO.getStartTime()==null);
        System.out.println(listWithSearchDTO.getEndTime()==null);
        System.out.println(listWithSearchDTO.getUserNumber()==null);
        if((listWithSearchDTO.getUserNumber()==null)&&(listWithSearchDTO.getStartTime()==null)&&(listWithSearchDTO.getRole()==null)&&(listWithSearchDTO.getEndTime()==null)){
            userMapper.selectPage(pages,null);
        }else {
            userMapper.selectPage(pages, userWrapper.ofSearch(listWithSearchDTO));
        }
        return pages;
    }

    /**
     * 创建/编辑用户
     * @param createOrEditUserDTO 从前端传回来的json格式数据转换的对象
     */
    @Override
    public void createOrEditUser(CreateOrEditUserDTO createOrEditUserDTO) {
        User user = new User();
        //随机生成用户编号
        CreateUserNumberUtils createUserNumberUtils = new CreateUserNumberUtils();
        user.setUserNumber(createUserNumberUtils.createUserNumber());
        user.setUsername(createOrEditUserDTO.getUsername());
        user.setRole(createOrEditUserDTO.getRole());
        user.setIdNumber(createOrEditUserDTO.getIdNumber());
        user.setPhoneNumber(createOrEditUserDTO.getPhoneNumber());
        user.setEmail(createOrEditUserDTO.getEmail());
        user.setPassword(createOrEditUserDTO.getPassword());
        user.setLastIp(createOrEditUserDTO.getLastIp());
        if(createOrEditUserDTO.getId() != null && createOrEditUserDTO.getId() != 0){
            user.setId(createOrEditUserDTO.getId());
            userMapper.updateById(user);
        }else {
            userMapper.insert(user);
        }
    }

    @Override
    public void deleteBatch(Integer[] lists) {
        Map<String,Object> map = new HashMap<String, Object>();
        for(Integer userId : lists){
            map.put("id",userId);
            userMapper.deleteByMap(map);
        }
    }

    //存在小问题，wrapper好像不能进行循环，待解决  运行正常
    @Override
    public void disableBatch(Integer[] lists) {
        UserWrapper userWrapper = new UserWrapper();
        Map<String,Object> map = new HashMap<>();
        User user;
        for(Integer userId : lists){
            map.put("id",userId);
            List<User> users = userMapper.selectByMap(map);
            users.get(0).setStatus(true);
            userMapper.updateById(users.get(0));
        }
    }

    @Override
    public void enableBatch(Integer[] lists) {
        UserWrapper userWrapper = new UserWrapper();
        Map<String,Object> map = new HashMap<>();
        User user;
        for(Integer userId : lists){
            map.put("id",userId);
            List<User> users = userMapper.selectByMap(map);
            users.get(0).setStatus(false);
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
