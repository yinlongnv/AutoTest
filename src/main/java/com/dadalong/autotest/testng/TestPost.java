package com.dadalong.autotest.testng;

import com.alibaba.fastjson.JSONObject;
import com.dadalong.autotest.model.other.ApiResponse;
import com.dadalong.autotest.utils.HttpClientUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Map;

public class TestPost {

    @Test
    @Parameters({"postUrl","postData","headersString"})
    public void testPost(String postUrl, String postData, String headersString) {
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        System.out.println("headersString1：" + headersString);
        Map<String, String> headersMap = httpClientUtils.changeHeadersStringToMap(headersString);
        System.out.println("headersMap：" + headersMap);
        String result = httpClientUtils.post(postUrl, postData, headersMap);
        System.out.println(result);
        ApiResponse apiResponse = JSONObject.parseObject(result , ApiResponse.class);
        Assert.assertEquals(apiResponse.getCode(), "00000");
    }

//    @Test
//    @Parameters({"postUrl","postData"})
//    public void testPost(String postUrl, String postData) {
//        HttpClientUtils httpClientUtils = new HttpClientUtils();
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Content-Type", "application/json");
//        String result = httpClientUtils.post(postUrl, postData, headers);
//        System.out.println(result);
//        ApiResponse apiResponse = JSONObject.parseObject(result , ApiResponse.class);
//        Assert.assertEquals(apiResponse.getCode(), "00000");
//    }

}
