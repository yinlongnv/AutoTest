package com.dadalong.autotest.testng;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 78089 on 2020/5/21.
 */
public class TestPOST {

    @Test
    private void testPost() {
        CloseableHttpClient httpClient = HttpClients.createDefault();

//如果发送是POST请求，创建HttpPost对象
        HttpPost httppost = new HttpPost("http://localhost:9001/user/login");

//post请求参数配置
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("username", "大大龙"));
        formparams.add(new BasicNameValuePair("password", "1"));
        UrlEncodedFormEntity uefEntity = null;   //设置编码格式为utf-8
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httppost.setEntity(uefEntity);  //设置POST请求参数

//使用httpclient的execute方法发送接口请求
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);
    }

}
