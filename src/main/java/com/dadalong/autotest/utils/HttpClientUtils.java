package com.dadalong.autotest.utils;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private static RequestConfig requestConfig;

    static
    {
        // 设置请求和传输超时时间
        requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();
    }

    /**
     * 用HttpClient发送有参get请求
     * @param get_uri
     * @param params
     * @return
     */
    public static String doGet(String get_uri, Map<String, String> params, Map<String, String> headers) {

        //用于存放结果
        String strResult = "";
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建接受响应的response
        CloseableHttpResponse response = null;
        //最终发送的uri
        URI uri;

        try {
            // 组装最终需要发送请求的uri
            URIBuilder builder = new URIBuilder(get_uri);
            if (params != null) {
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key));
                }
            }
            uri = builder.build();

            //创建HttpGet请求指定uri
            HttpGet httpGet = new HttpGet(uri);
            //向get请求中注入头信息
            if (headers != null) {
                for (String key : headers.keySet()) {
                    httpGet.setHeader(key, headers.get(key));
                }
            }

            httpGet.setConfig(requestConfig);

            //用response接收向指定uri发送get请求得到的响应
            response = httpClient.execute(httpGet);

            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                strResult = EntityUtils.toString(response.getEntity(), "utf-8");
                System.out.println("********************&*(%$E%^&*()*&" + strResult);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }

    /**
     * 用HttpClient发送无参get请求
     * @param get_uri
     * @return
     */
    public static String doGet(String get_uri) {
        return doGet(get_uri, null, null);
    }

    /**
     * 用HttpClient发送有参、请求头信息的post请求
     * @param post_uri
     * @param params
     * @return
     */
    public static String doPost(String post_uri, Map<String, String> params, Map<String, String> headers) {
        //用于存放结果
        String strResult = "";
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建接受响应的response
        CloseableHttpResponse response = null;

        try {
            //创建HttpPost请求指定uri
            HttpPost httpPost = new HttpPost(post_uri);


            //向uri中注入参数
            if (params != null) {
                List<NameValuePair> paramsList = new ArrayList<>();
                for (String key : params.keySet()) {
                    paramsList.add(new BasicNameValuePair(key, params.get(key)));
                }
                StringEntity entity = new StringEntity(paramsList.toString(), "utf-8");
                httpPost.setEntity(entity);
            }
            //向post请求中注入头信息
            if (headers != null) {
                for (String key : headers.keySet()) {
                    httpPost.setHeader(key, headers.get(key));
                }
            }
            //用response接收向指定uri发送post请求得到的响应
            response = httpClient.execute(httpPost);

            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                strResult = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
    /**
     * 用HttpClient发送有参、无请求头信息的post请求
     * @param post_uri
     * @param params
     * @return
     */
    public static String doPost(String post_uri, Map<String, String> params) {
        //用于存放结果
        String strResult = "";
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建接受响应的response
        CloseableHttpResponse response = null;

        try {
            //创建HttpPost请求指定uri
            HttpPost httpPost = new HttpPost(post_uri);


            //向uri中注入参数
            if (params != null) {
                //定义了一个list，该list的数据类型是NameValuePair（简单名称值对节点类型），
                //这个代码用于Java像url发送Post请求。在发送post请求时用该list来存放参数。
                List<NameValuePair> paramsList = new ArrayList<>();
                for (String key : params.keySet()) {
                    paramsList.add(new BasicNameValuePair(key, params.get(key)));
                }

                StringEntity entity = new StringEntity(paramsList.toString(), "utf-8");
                httpPost.setEntity(entity);
            }
            //用response接收向指定uri发送post请求得到的响应
            response = httpClient.execute(httpPost);

            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                strResult = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
    /**
     * 用HttpClient发送无参post请求
     * @param post_url
     * @return
     */
    public static String doPost(String post_url) {
        return doPost(post_url, null, null);
    }

    /**
     * 用HttpClient发送json参数post请求
     * @param url
     * @param json
     * @return
     */
    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建接受响应的response
        CloseableHttpResponse response = null;
        //用于存放结果
        String strResult = "";
        try {
            // 创建HttpPost请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            strResult = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return strResult;
    }

}
