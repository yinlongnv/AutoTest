package com.dadalong.autotest.utils;

import com.dadalong.autotest.bean.v1.mapper.ApiMapper;
import com.dadalong.autotest.bean.v1.mapper.TestCaseMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;

import javax.annotation.Resource;

/**
 * Created by 78089 on 2020/4/23.
 */
public class UniqueJudgementUtils {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private TestCaseMapper testCaseMapper;

    public Boolean ifUsernameExist(String username) {

        UserWrapper userWrapper = new UserWrapper();
        if (userMapper.selectOne(userWrapper.ofUsername(username)) == null) {
            return false;
        } else {
            return true;
        }

    }


}
