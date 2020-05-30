package com.dadalong.autotest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.service.IUserService;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = AutoTestApplication.class)
//@SpringBootTest
//@RunWith(SpringRunner.class)
public class AutotestApplicationTests {
    @Autowired
    private IUserService iUserService;

    @Test
    void contextLoads() {
    }

    @Test
    public void handleDatas() {
        String h = "[{'name': 'Content-Type', 'value': 'application/json', 'required': '1'},{'name': 'Accept', 'value': 'application/json', 'required': '1'}]";
        JSONArray jsonArray = JSONArray.parseArray(h);
        Map<String, String> map = new HashMap<>();
        System.out.println("jsonArray" + jsonArray);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            System.out.println("jsonObject：" + jsonObject);
            System.out.println("jsonObject.name："+ jsonObject.get("name"));
            System.out.println("jsonObject.value："+ jsonObject.get("value"));
            map.put(jsonObject.get("name").toString(), jsonObject.get("value").toString());
            System.out.println("map：" + map);
            System.out.println("mapString：" + map.toString());
            HashMap hashMap = JSON.parseObject(map.toString(), HashMap.class);
            System.out.println("hashMap：" + hashMap);
        }
    }

}
