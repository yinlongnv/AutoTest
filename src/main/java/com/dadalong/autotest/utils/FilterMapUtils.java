package com.dadalong.autotest.utils;

import com.dadalong.autotest.bean.v1.mapper.ApiMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;
import com.dadalong.autotest.model.response.*;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class FilterMapUtils {

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 提取“所属业务+环境域名-所属分组-接口名称+接口路径”级联筛选
     * @return
     */
    public FilterMapResponse filterMap() {
        FilterMapResponse filterMapResponse = new FilterMapResponse();
        List<Api> apiList = apiMapper.selectList(new ApiWrapper().groupBy("project_name").groupBy("api_group"));
        String projectName = apiList.get(0).getProjectName();
        //预处理
        Map<String,List<Api>> map = new HashMap<>();
        List<Api> apis = new ArrayList<>();

        for (Api api : apiList) {
            if(api.getProjectName().equals(projectName)){
                apis.add(api);
            }else{
                map.put(projectName,apis);
                projectName = api.getProjectName();
                apis = new ArrayList<>();
                apis.add(api);
            }
        }
        map.put(projectName,apis);

        List<LevelOne> options = new ArrayList<>();
        for(String pn : map.keySet()){
            List<Api> record = map.get(pn);
            LevelOne levelOne = new LevelOne();
            levelOne.setLabel(pn);
            levelOne.setValue(pn);
            List<LevelTwo> listTwo = new ArrayList<>();
            for (Api api : record) {
                List<Api> groupApi = apiMapper.selectList(new ApiWrapper().eq("project_name",pn).eq("api_group",api.getApiGroup()));
                LevelTwo levelTwo = new LevelTwo();
                levelTwo.setLabel(api.getApiGroup());
                levelTwo.setValue(api.getApiGroup());
                List<LevelThree> listThree = new ArrayList<>();
                for (Api two : groupApi) {
                    Api last = apiMapper.selectOne(new ApiWrapper().eq("project_name",pn).eq("api_group",two.getApiGroup()).eq("api_path",two.getApiPath()));
                    String merge = last.getApiName() + " " + last.getApiPath();
                    LevelThree levelThree = new LevelThree();
                    levelThree.setValue(merge);
                    levelThree.setLabel(merge);
                    listThree.add(levelThree);
                }
                listTwo.add(levelTwo);
                levelTwo.setChildren(listThree);
            }
            levelOne.setChildren(listTwo);
            options.add(levelOne);
        }
        filterMapResponse.setOptions(options);
        return filterMapResponse;
    }

    /**
     * 提取用户名筛选项
     * @return
     */
    public FilterUserNameResponse filterUserName() {
        FilterUserNameResponse filterUserNameResponse = new FilterUserNameResponse();
        UserWrapper userWrapper = new UserWrapper();
        userWrapper.groupBy("username").orderByAsc("id");
        List<User> userList = userMapper.selectList(userWrapper);
        List<UserName> userNameList = new ArrayList<>();
        for (User user : userList) {
            UserName userName = new UserName();
            userName.setValue(user.getId().toString());
            userName.setLabel(user.getUsername());
            userNameList.add(userName);
        }
        filterUserNameResponse.setUserNameOptions(userNameList);
        return filterUserNameResponse;
    }
}
