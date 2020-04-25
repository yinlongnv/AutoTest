package com.dadalong.autotest.utils;

import com.dadalong.autotest.bean.v1.mapper.ApiMapper;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import com.dadalong.autotest.model.response.FilterMapResponse;
import com.dadalong.autotest.model.response.LevelOne;
import com.dadalong.autotest.model.response.LevelThree;
import com.dadalong.autotest.model.response.LevelTwo;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 78089 on 2020/4/25.
 */
@Configuration
public class FilterMapUtils {

    @Resource
    private ApiMapper apiMapper;

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
}
