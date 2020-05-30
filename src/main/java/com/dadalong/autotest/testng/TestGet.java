package com.dadalong.autotest.testng;

import com.alibaba.fastjson.JSONObject;
import com.dadalong.autotest.model.other.ApiResponse;
import com.dadalong.autotest.utils.HttpClientUtils;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Map;

public class TestGet {

    @Test
    @Parameters({"getUrl","headersString"})
    public void testGet(String getUrl, String headersString) {
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        System.out.println("headersString1：" + headersString);
        Map<String, String> headersMap = httpClientUtils.changeHeadersStringToMap(headersString);
        System.out.println("headersMap：" + headersMap);
        String result = httpClientUtils.get(getUrl, headersMap);
        System.out.println(result);
        ApiResponse apiResponse = JSONObject.parseObject(result , ApiResponse.class);
        Assert.assertEquals(apiResponse.getCode(), "00000");
    }

//    @Test
//    @Parameters({"getUrl"})
//    public void testGet(String getUrl) {
//        HttpClientUtils httpClientUtils = new HttpClientUtils();
//        Map<String, String> map = new HashMap<>();
//        String result = httpClientUtils.get(getUrl, map);
//        System.out.println(result);
//        ApiResponse apiResponse = JSONObject.parseObject(result , ApiResponse.class);
//        Assert.assertEquals(apiResponse.getCode(), "00000");
//    }

}
