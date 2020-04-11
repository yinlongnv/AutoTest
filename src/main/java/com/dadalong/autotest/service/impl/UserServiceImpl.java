package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;
import com.dadalong.autotest.model.user.CreateUserDTO;
import com.dadalong.autotest.model.user.SearchDTO;
import com.dadalong.autotest.service.IUserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    private static final Integer size = 5;

    @Override
    public void addUser(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setUserNumber("需要自定义一个生成函数");
        user.setIdNumber(createUserDTO.getIdNumber());
        user.setPhoneNumber(createUserDTO.getPhoneNumber());
        user.setEmail(createUserDTO.getEmail());
        user.setRole(createUserDTO.getRole());
        user.setPassword(createUserDTO.getPassword());
        if(createUserDTO.getId() != null && createUserDTO.getId() != 0){
            user.setId(createUserDTO.getId());
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
    public Page<User> filterRole(String role,Integer page) {
        UserWrapper userWrapper = new UserWrapper();
        Page<User> pages = new Page<>(page,size);
        userMapper.selectPage(pages,userWrapper.filterOfRole(role));
        return pages;
    }


    @Override
    public Page<User> searchByDate(Date lastLoginTime,Integer page) {
        UserWrapper userWrapper = new UserWrapper();
        Page<User> pages = new Page<>(page,size);
        userMapper.selectPage(pages,userWrapper.ofLastLoginDate(lastLoginTime));
        return pages;
    }

//    @Override
//    public Page<User> searchByName(String name,Integer page) {
//
//    }

    @Override
    public Page<User> list(String name,Integer page) {
        if(name != null && !name.equals("")){
            UserWrapper userWrapper = new UserWrapper();
            Page<User> pages = new Page<>(page,size);
            userMapper.selectPage(pages,userWrapper.oflikeName(name));
            return pages;
        }else {
            UserWrapper userWrapper = new UserWrapper();
            Page<User> pages = new Page<>(page, size);
            userMapper.selectPage(pages, null);
            return pages;
        }
    }

    @Override
    public Page<User> list(SearchDTO searchDTO) {
        if (searchDTO == null)
            return null;
        UserWrapper userWrapper = new UserWrapper();
        Page<User> pages = new Page<>(searchDTO.getPage(),size);
        userMapper.selectPage(pages,userWrapper.ofSearch(searchDTO));
        return pages;
    }

    @Override
    public String handleUploadedFile(MultipartFile file) throws IOException {
        String content = new String(file.getBytes());
        JsonArray jsonArry = new JsonParser().parse(content).getAsJsonArray();
        System.out.println(jsonArry);
        return null;
    }


}
