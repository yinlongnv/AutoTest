package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;
import com.dadalong.autotest.model.response.UserListResponse;
import com.dadalong.autotest.model.user.BatchDTO;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.model.user.DetailDTO;
import com.dadalong.autotest.model.user.LoginDTO;
import com.dadalong.autotest.service.IUserService;
import com.dadalong.autotest.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    InsertOperateLogUtils insertOperateLogUtils;

    @Resource
    CreateUserNumberUtils createUserNumberUtils;

    @Override
    public User login(LoginDTO loginDTO) {
        UserWrapper userWrapper = new UserWrapper();
        User user = userMapper.selectOne(userWrapper.ofUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword()));
        if (user != null) {
            user.setLastIp(loginDTO.getLastIp());
            user.setLastLogin(new Date());
            Integer loginCount = user.getLoginCount() + 1;
            user.setLoginCount(loginCount);
            userMapper.updateById(user);
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(user.getId(), LogContentEnumUtils.USERLOGIN, OperatePathEnumUtils.USERLOGIN);
            return user;
        } else {
            return null;
        }
    }

    public IPage<UserListResponse> listWithSearch(SearchRequest searchRequest){
        try {
            UserWrapper userWrapper = new UserWrapper();
            userWrapper.ofListWithSearch(searchRequest).orderByDesc("created_at");
            List<UserListResponse> userListResponseList = new ArrayList<>();
            Map<String,Object> map = searchRequest.getSearch();

            SlabPage<User> userSlabPage = new SlabPage<>(searchRequest);
            IPage<User> userResults = userMapper.selectPage(userSlabPage, userWrapper);
            for (User record : userResults.getRecords()) {
                User user = userMapper.selectById(record.getUserId());
                UserListResponse userListResponse = new UserListResponse();
                BeanUtils.copyProperties(record, userListResponse);
                if (user != null && StringUtils.isNotBlank(user.toString())) {
                    userListResponse.setCreatedBy(user.getUsername());
                    userListResponseList.add(userListResponse);
                } else {
                    userListResponse.setCreatedBy("root");
                    userListResponseList.add(userListResponse);
                }
            }
            SlabPage<UserListResponse> userListResponseSlabPage = new SlabPage<>(searchRequest);
            userListResponseSlabPage.setRecords(userListResponseList);
            userListResponseSlabPage.setTotal(userResults.getTotal());

            Object userId = map.get("userId");
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(Integer.valueOf(String.valueOf(userId)), LogContentEnumUtils.USERLIST, OperatePathEnumUtils.USERLIST);
            return userListResponseSlabPage;
        }catch (Exception e){
            throw new ConflictException("listWithSearchError");
        }
    }

    @Override
    public String createOrEditUser(CreateOrEditUserDTO createOrEditUserDTO) {
        User user = new User();
        UserWrapper userWrapper = new UserWrapper();
        BeanUtils.copyProperties(createOrEditUserDTO, user, "userId");
        if(createOrEditUserDTO.getId() == null) {
            if (userMapper.selectOne(userWrapper.eq("username", user.getUsername())) == null) {
                //随机生成用户编号
                user.setUserNumber(createUserNumberUtils.createUserNumber());
                user.setUserId(createOrEditUserDTO.getUserId());
                //插入操作日志
                insertOperateLogUtils.insertOperateLog(createOrEditUserDTO.getUserId(), LogContentEnumUtils.USERCREATE, OperatePathEnumUtils.USERCREATE);
                userMapper.insert(user);
                return "创建成功";
            } else {
                return "用户名已存在";
            }
        } else {
            if (userMapper.selectOne(new UserWrapper().eq("id", createOrEditUserDTO.getId())) == null) {
                return "该用户不存在";
            } else {
                if (userMapper.selectCount(userWrapper.ne("id", createOrEditUserDTO.getId()).eq("username", createOrEditUserDTO.getUsername())) == 0){
                    //插入操作日志
                    insertOperateLogUtils.insertOperateLog(createOrEditUserDTO.getUserId(), LogContentEnumUtils.USEREDIT, OperatePathEnumUtils.USEREDIT);
                    userMapper.updateById(user);
                    return "编辑成功";
                } else {
                    return "用户名已存在";
                }
            }
        }
    }

    @Override
    public Boolean deleteBatch(BatchDTO batchDTO) {
        if (removeByIds(batchDTO.getUserIds())) {
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(batchDTO.getUserId(), LogContentEnumUtils.USERDELETE, OperatePathEnumUtils.USERDELETE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void disableBatch(BatchDTO batchDTO) {
        Map<String,Object> map = new HashMap<>();
        for(Integer userId : batchDTO.getUserIds()){
            map.put("id",userId);
            List<User> users = userMapper.selectByMap(map);
            users.get(0).setStatus(1);
            userMapper.updateById(users.get(0));
        }
        //插入操作日志
        insertOperateLogUtils.insertOperateLog(batchDTO.getUserId(), LogContentEnumUtils.USERDISABLE, OperatePathEnumUtils.USERDISABLE);
    }

    @Override
    public void enableBatch(BatchDTO batchDTO) {
        Map<String,Object> map = new HashMap<>();
        for(Integer userId : batchDTO.getUserIds()){
            map.put("id",userId);
            List<User> users = userMapper.selectByMap(map);
            users.get(0).setStatus(0);
            userMapper.updateById(users.get(0));
        }
        //插入操作日志
        insertOperateLogUtils.insertOperateLog(batchDTO.getUserId(), LogContentEnumUtils.USERENABLE, OperatePathEnumUtils.USERENABLE);
    }

    @Override
    public User detail(DetailDTO detailDTO) {
        //插入操作日志
        insertOperateLogUtils.insertOperateLog(detailDTO.getUserId(), LogContentEnumUtils.USERDETAIL, OperatePathEnumUtils.USERDETAIL);
        return userMapper.selectById(detailDTO.getId());
    }
}
