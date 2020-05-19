package com.dadalong.autotest.utils;

import com.dadalong.autotest.bean.v1.mapper.ApiMapper;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 处理用例所属信息的工具
 */
@Configuration
public class TestCaseUtils {

    @Resource
    private ApiMapper apiMapper;

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
}
