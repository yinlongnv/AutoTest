package com.dadalong.autotest.testng;

import com.alibaba.fastjson.JSONObject;
import com.dadalong.autotest.model.other.ApiResponse;
import com.dadalong.autotest.utils.HttpClientUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestCases {

//    @Test
//    @Parameters({"getUrl"})
//    public void testGet(String getUrl) throws IOException {
//        HttpClientUtils httpClientUtils = new HttpClientUtils();
//        Map<String, String> map = new HashMap<>();
////        String url = "http://localhost:9001/case/filterMap";
//        String result = httpClientUtils.get(getUrl, map);
//        System.out.println(result);
//        ApiResponse apiResponse = JSONObject.parseObject(result , ApiResponse.class);
//        Assert.assertEquals(apiResponse.getCode(), "00000");
//    }

    @Test
    @Parameters({"postUrl","postData"})
    public void testPost(String postUrl, String postData) throws IOException {
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String result = httpClientUtils.post(postUrl, postData, headers);
        System.out.println(result);
        ApiResponse apiResponse = JSONObject.parseObject(result , ApiResponse.class);
        Assert.assertEquals(apiResponse.getCode(), "00000");
    }


//    @Test(dataProvider = "myDataProvider")
//    public void testDataProvider(String fruitName, Integer fruitNum){
//        System.out.println("我从myDataProvider仓库拿到了" + fruitNum + "个" + fruitName);
//    }

}
