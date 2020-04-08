package com.dadalong.autotest.service.impl;

import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;
import com.dadalong.autotest.model.user.CreateUserDTO;
import com.dadalong.autotest.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

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
        userMapper.insert(user);
    }

    @Override
    public void deleteBatch(List<String> lists) {
        Map<String,Object> map = new HashMap<String,Object>();
        for(String userNumber : lists){
            map.put("user_number",userNumber);
            userMapper.deleteByMap(map);
        }
    }

    @Override
    public void disableBatch(List<String> lists) {
        UserWrapper userWrapper = new UserWrapper();
        User user;
        for(String userNumber : lists){
            user = userMapper.selectOne(userWrapper.ofUserNumber(userNumber));
            user.setStatus(true);
            userMapper.updateById(user);
        }
    }

    @Override
    public void enableBatch(List<String> lists) {
        UserWrapper userWrapper = new UserWrapper();
        User user;
        for(String userNumber : lists){
            user = userMapper.selectOne(userWrapper.ofUserNumber(userNumber));
            user.setStatus(false);
            userMapper.updateById(user);
        }
    }

    @Override
    public List<User> filterRole(String role) {
        UserWrapper userWrapper = new UserWrapper();
        return userMapper.selectList(userWrapper.filterOfRole(role));
    }


    @Override
    public List<User> searchByDate(Date lastLoginTime) {
        return null;
    }

    @Override
    public List<User> searchByName(String name) {
        return null;
    }
}
