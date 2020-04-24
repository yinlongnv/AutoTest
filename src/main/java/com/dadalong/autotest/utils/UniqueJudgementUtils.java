package com.dadalong.autotest.utils;

import com.dadalong.autotest.bean.v1.mapper.ApiMapper;
import com.dadalong.autotest.bean.v1.mapper.TestCaseMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Created by 78089 on 2020/4/23.
 */
@Configuration
public class UniqueJudgementUtils {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private TestCaseMapper testCaseMapper;

    public Boolean ifUsernameExist(String username) {

        UserWrapper userWrapper = new UserWrapper();
        System.out.println("判断用户名" + username);
        if (userMapper.selectOne(userWrapper.ofUsername(username)) == null) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean ifApiNameExist(String apiName) {

        ApiWrapper apiWrapper = new ApiWrapper();
        if (apiMapper.selectOne(apiWrapper.ofApiName(apiName)) == null) {
            return false;
        } else {
            return true;
        }
    }


}