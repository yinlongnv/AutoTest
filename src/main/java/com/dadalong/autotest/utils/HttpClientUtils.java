package com.dadalong.autotest.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 使用HttpClient发送请求、接收响应很简单，一般需要如下几步即可。
 * 1. 创建HttpClient对象。
 * 2. 创建请求方法的实例，并指定请求URL。如果需要发送GET请求，创建HttpGet对象；如果需要发送POST请求，创建HttpPost对象。
 * 3. 如果需要发送请求参数，可调用HttpGet、HttpPost共同的setParams(HttpParams params)方法来添加请求参数；对于HttpPost对象而言，也可调用setEntity(HttpEntity entity)方法来设置请求参数。
 * 4. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
 * 5. 调用HttpResponse的getAllHeaders()、getHeaders(String name)等方法可获取服务器的响应头；调用HttpResponse的getEntity()方法可获取HttpEntity对象，该对象包装了服务器的响应内容。程序可通过该对象获取服务器的响应内容。
 * 6. 释放连接。无论执行方法是否成功，都必须释放连接
 */
public class HttpClientUtils {
    /**
     * http get
     * @param url 可带参数的 url 链接
     * @param heads http 头信息
     * */
    public String get(String url, Map<String, String> heads){
        org.apache.http.client.HttpClient httpClient= HttpClients.createDefault();
        HttpResponse httpResponse = null;
        String result = "";
        HttpGet httpGet = new HttpGet(url);
        if(heads != null){
            Set<String> keySet = heads.keySet();
            for(String s : keySet){
                httpGet.addHeader(s, heads.get(s));
            }
        }
        try{
            httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if(httpEntity != null){
                result = EntityUtils.toString(httpEntity,"utf-8");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return  result;
    }

    /**
     * http post
     * */
    public String post(String url, String data, Map<String, String> heads){
        org.apache.http.client.HttpClient httpClient = HttpClients.createDefault();
        HttpResponse httpResponse;
        String result = "";
        HttpPost httpPost = new HttpPost(url);
        if(heads != null){
            Set<String> keySet = heads.keySet();
            for(String s : keySet){
                httpPost.addHeader(s, heads.get(s));
            }
        }
        try{
            StringEntity s = new StringEntity(data,"utf-8");
            httpPost.setEntity(s);
            httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if(httpEntity != null){
                result = EntityUtils.toString(httpEntity,"utf-8");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return  result;
    }

    public Map<String, String> changeHeadersStringToMap(String headersString) {
        Map<String, String> headersMap = new HashMap<>();
        if (StringUtils.isNotBlank(headersString)) {
            System.out.println("headersString2：" + headersString);
            JSONArray jsonArray = JSONArray.parseArray(headersString);
            System.out.println("jsonArray" + jsonArray);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                headersMap.put(jsonObject.get("name").toString(), jsonObject.get("value").toString());
            }
            System.out.println("map：" + headersMap);
        } else {
            headersMap.put("Content-Type", "application/json");
        }
        return headersMap;
    }
}