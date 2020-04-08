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
    public void deleteBatch(String[] lists) {
        Map<String,Object> map = new HashMap<String,Object>();
        for(String userNumber : lists){
            map.put("user_number",userNumber);
            userMapper.deleteByMap(map);
        }
    }

    //存在小问题，wrapper好像不能进行循环，待解决  运行正常
    @Override
    public void disableBatch(String[] lists) {
        UserWrapper userWrapper = new UserWrapper();
        Map<String,Object> map = new HashMap<>();
        User user;
        for(String userNumber : lists){
            map.put("user_number",userNumber);
            List<User> users = userMapper.selectByMap(map);
            users.get(0).setStatus(true);
            userMapper.updateById(users.get(0));
        }
    }

    @Override
    public void enableBatch(String[] lists) {
        UserWrapper userWrapper = new UserWrapper();
        Map<String,Object> map = new HashMap<>();
        User user;
        for(String userNumber : lists){
            map.put("user_number",userNumber);
            List<User> users = userMapper.selectByMap(map);
            users.get(0).setStatus(false);
            userMapper.updateById(users.get(0));
        }
    }

    @Override
    public List<User> filterRole(String role) {
        UserWrapper userWrapper = new UserWrapper();
        return userMapper.selectList(userWrapper.filterOfRole(role));
    }


    @Override
    public List<User> searchByDate(Date lastLoginTime) {
        UserWrapper userWrapper = new UserWrapper();
        return userMapper.selectList(userWrapper.ofLastLoginDate(lastLoginTime));
    }

    @Override
    public List<User> searchByName(String name) {
        UserWrapper userWrapper = new UserWrapper();
        return userMapper.selectList(userWrapper.oflikeName(name));
    }
}
