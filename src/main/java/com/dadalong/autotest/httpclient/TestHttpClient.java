package com.dadalong.autotest.httpclient;

import com.dadalong.autotest.utils.HttpClientUtils;
import org.apache.http.client.CookieStore;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

public class TestHttpClient {

    //加载配置文件内容
    private ResourceBundle bundle;

    @BeforeTest
    public void beforeTest() {
        bundle = ResourceBundle.getBundle("application", Locale.CHINA);
    }

    /**
     * 测试Get请求
     */
    @Test
    public void testGetHttpClient() {

        String u = "http://csr.adl.io/range-user/api/admin/user/list";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("page", "1");
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC91c2VyXC9yYW5nZS11c2VyXC9hcGlcL2lubmVyXC9hdXRoIiwiaWF0IjoxNTg2MzE1NDMxLCJleHAiOjE1ODY1NjAwOTAsIm5iZiI6MTU4NjM0NDA5MCwianRpIjoiUHdNaXRieFRMc3dKSWNwNCIsInN1YiI6NzIsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjciLCJsb2dpbl90b2tlbiI6IjllYmUzODdhZjY0MjFhNjFmMzI1ZTgzNjdkMjg1ZTEyIn0.WoLFvyoOr9KXbijj30G8BAyaCIdc9hhnZ1fYOaGbVVc");
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        String result = httpClientUtils.doGet(u, map, headers);
        System.out.println(result);
        //将返回的响应结果字符串转化成json对象
        JSONObject resultJSON = new JSONObject(result);
        //获取到结果值
        String code = (String) resultJSON.get("code");
        String message = (String) resultJSON.get("message");
        //具体的判断返回结果的值
        Assert.assertEquals("00000",code);
        Assert.assertEquals("",message);

    }



    /**
     * 测试Post请求
     */
    @Test
    public void testPostHttpClient() {

        String u = "http://csr.adl.io/range-edu/api/teacher/course/private/destroy";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("ids", "[33]");
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC91c2VyXC9yYW5nZS11c2VyXC9hcGlcL2lubmVyXC9hdXRoIiwiaWF0IjoxNTg2MzE1NDMxLCJleHAiOjE1ODY1NjE4MjAsIm5iZiI6MTU4NjM0NTgyMCwianRpIjoiaUx1N3R4aU9pM1hacXNpWCIsInN1YiI6NzIsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjciLCJsb2dpbl90b2tlbiI6IjllYmUzODdhZjY0MjFhNjFmMzI1ZTgzNjdkMjg1ZTEyIn0.ZFYDfNGg3cx0SCNVw5-rJa7_pk9oU9NSYtkoCA_CUxM");
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        String result = httpClientUtils.doPost(u, map, headers);
        System.out.println(result);
        //将返回的响应结果字符串转化成json对象
        JSONObject resultJSON = new JSONObject(result);
        //获取到结果值
        String code = (String) resultJSON.get("code");
        String message = (String) resultJSON.get("message");
        //具体的判断返回结果的值
        Assert.assertEquals("00000",code);
        Assert.assertEquals("删除课程成功",message);

    }
}
