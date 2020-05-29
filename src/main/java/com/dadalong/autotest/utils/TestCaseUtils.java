package com.dadalong.autotest.utils;

import com.dadalong.autotest.bean.v1.mapper.ApiMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.pojo.TestCase;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理用例所属信息的工具
 */
@Configuration
public class TestCaseUtils {

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 处理下拉筛选框内容定位到唯一的apiId
     * @param projectName
     * @param apiGroup
     * @param apiMerge
     * @return
     */
    public Integer getApiId(String projectName, String apiGroup, String apiMerge) {
        Api api = new Api();
        api.setProjectName(projectName);
        api.setApiGroup(apiGroup);
        String apiInfo[] = apiMerge.split(" ");
        api.setApiName(apiInfo[0]);
        api.setApiPath(apiInfo[1]);
        ApiWrapper apiWrapper = new ApiWrapper();
        apiWrapper.ofApiId(api);
        Api getApi = apiMapper.selectOne(apiWrapper);
        if (getApi != null && StringUtils.isNotBlank(getApi.toString())) {
            return getApi.getId();
        } else {
            return 0;
        }
    }

    /**
     * 处理testcase相关的异常信息，包括创建人、执行人和执行时间
     * @param testCase
     * @return
     */
    public Map<String, String> setUserInfo(TestCase testCase) {
        Map<String, String> userMap = new HashMap<>();
        User createdBy = userMapper.selectById(testCase.getUserId());
        User username = userMapper.selectById(testCase.getExecuteByUserId());
        if(createdBy != null && StringUtils.isNotBlank(createdBy.toString())) {
            userMap.put("createdByUsername", createdBy.getUsername());
        } else {
            userMap.put("createdByUsername", "root");
        }
        if (testCase.getExecuteCount() == 0) {
            userMap.put("lastExecuteTime", null);
            userMap.put("executeByUsername", null);
        } else {
            if(username != null && StringUtils.isNotBlank(username.toString())) {
                userMap.put("executeByUsername", username.getUsername());
            } else {
                userMap.put("executeByUsername", "root");
            }
            //创建SimpleDateFormat对象实例并定义好转换格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userMap.put("lastExecuteTime", sdf.format(testCase.getUpdatedAt()));
        }
//        System.out.println("userMap：" + userMap);
        return userMap;
    }
}
