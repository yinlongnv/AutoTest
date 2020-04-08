package com.dadalong.httpclient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;

public class MyHttpClient {

    @Test
    public void test1() throws IOException {

        //用于存放结果
        String result;
        HttpGet get = new HttpGet("http://www.baidu.com");
        //用于执行get方法
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(get);
        //转为字符串格式
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(result);

    }

}
