package com.example.test;

import com.alibaba.fastjson.JSONObject;
import com.example.action.HttpResponse;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.MulticastChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class test {
//    public HttpResponse dosPost(String url, String posttype, String data, String myhead){
    public HttpResponse dosPost(){
        String url ="http://synergy.k8s.superlucy.net/api/auth/oauth/token";
        String data="{\"password\":\"sdl@123\",\"username\":\"sdltest\",\"grant_type\":\"password\"}";
        HttpResponse httpresponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            //判断请求头并给请求头附值
//            myxtposthead(myhead,httpPost);
//            httpPost.addHeader(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
            List<NameValuePair> list = new ArrayList<>();
            JSONObject jsonObject = JSONObject.parseObject(data);
            for(String str:jsonObject.keySet()){
                System.out.println(str + ":" +jsonObject.get(str));
                list.add(new BasicNameValuePair(str,jsonObject.get(str).toString()));
            }
            //打印出"keySet()"里的内容看一下
            System.out.println("json:"+jsonObject.keySet());

            httpPost.addHeader(new BasicHeader("Accept","application/json, text/plain, */*"));
            httpPost.addHeader(new BasicHeader("Authorization","Basic c3VwZXJsdWN5X3N5bmVyZ3k6c3luZXJneS0xcWF6LTJ3c3g="));
            httpPost.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
//            String params = jointBodyParam(data);
//            System.out.println("转化后params:"+params);
            //将参数设置到 NameValuePair
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list, Consts.UTF_8);
            httpPost.setEntity(formEntity);

            CloseableHttpResponse responses = null;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            responses = httpClient.execute(httpPost);
            //设置超时
//            RequestConfig requestConfig = RequestTime();
//            httpPost.setConfig(requestConfig);
            httpresponse = new HttpResponse(responses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpresponse;
    }
    //data/from 可用demo
    public HttpResponse doPostsend(){
        HttpResponse httpresponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //String url ="http://production.edge.customs.k8s.superlucy.net/api/auth/oauth/token";
        String fileone = "D:\\testdata\\github\\amisroboyxt\\amisrobot\\excel\\amirobotclound.xls";
//        String data = "{\"username\":\"0215测试001\",\"password\":\" 123456\",\"returnUrl\":\"/sas/invt/import\",\"captchaId\":\"\",\"captchaCode\":\"undefined\",\"grant_type\":\"password\"}";
        String url = "http://v30.edge.customs.k8s.amiintellect.com/api/auth/oauth/token";
        String data = "{\"username\":\"0215测试001\",\"password\":\"123456\",\"returnUrl\":\"/home\",\"captchaId\":\"\",\"captchaCode\":\"undefined\",\"grant_type\":\"password\"}";
        HttpResponse response = null;
        String result = "1111";
        //每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
        String boundary ="------WebKitFormBoundaryerEzPLp0xMCUtCXe";
        try {
            //文件名
            File file = new File(fileone);
            String fileName = file.getName();
            HttpPost httpPost = new HttpPost(url);
            //设置请求头
            httpPost.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
            httpPost.setHeader("Authorization","Basic YW1paW50ZWxsZWN0OmFtaWludGVsbGVjdC0xMjM0NTY=");
            //HttpEntity builder
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //字符编码
            builder.setCharset(Charset.forName("UTF-8"));
            //模拟浏览器
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            //boundary
            builder.setBoundary(boundary);
            //判断是否文件上传
            if(null==null){
                //multipart/form-data  文件上传
                //builder.addPart("multipartFile",new FileBody(file));
                // binary
                //builder.addBinaryBody("name=\"multipartFile\"; filename=\"test.docx\"", new FileInputStream(file), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
            }
            //multipart/form-data  文件上传
            //builder.addPart("multipartFile",new FileBody(file));
            // binary
            //builder.addBinaryBody("name=\"multipartFile\"; filename=\"test.docx\"", new FileInputStream(file), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
            //其他普通参数demo1
            //builder.addTextBody("filename", fileName,  ContentType.create("text/plain", Consts.UTF_8));
            //普通参数demo2：对于普通字段含有中文，不能用addTextBody，否则乱码,要先转成StringBody，再用addPart
            //StringBody stringBody = new StringBody("名称",
                    //ContentType.create("text/plain",Consts.UTF_8));
            //builder.addPart("username",stringBody);
            JSONObject jsonObject = JSONObject.parseObject(data);
            int size = jsonObject.keySet().size();
            System.out.println("size:"+size);
            for(String str:jsonObject.keySet()){
                System.out.println(str + ":" +jsonObject.get(str));
//                builder.addPart(str,new StringBody(jsonObject.get(str).toString(),
//                        ContentType.create("text/plain",Consts.UTF_8)));
                builder.addTextBody(str, jsonObject.get(str).toString(),  ContentType.create("text/plain", Consts.UTF_8));
            }
            //HttpEntity
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            // 执行提交
            CloseableHttpResponse responses = httpClient.execute(httpPost);
            httpresponse = new HttpResponse(responses);
            //响应
//            HttpEntity responseEntity = responses.getEntity();
//            if (responseEntity != null) {
//                // 将响应内容转换为字符串
//                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.err.println("result响应："+result);
        return httpresponse;
    }

    public static void main(String[] args) {
        test test = new test();
        HttpResponse httpResponse = test.doPostsend();
        String body = httpResponse.getBody();
        System.out.println("get:"+body+"++++"+httpResponse.getCode());
    }
}

