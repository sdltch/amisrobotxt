package com.example.action;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class myRequest {
    HashMap<String, String> myjsonmap = new HashMap<String, String>();
    public String Authorization = null;
    public String Authorizations = "Authorization";
    public myRequest() {
        //获取登陆URl
        String loginUrl = DataManipulation.myurlRpa + DataManipulation.loginUrl;
        System.out.println("登录urlLL："+loginUrl);
        //登录入参
        String postData = DataManipulation.loginData;
        String loginYype = DataManipulation.loginYype;
        String loginHead = DataManipulation.longinhead;
        String myresponse = DataManipulation.myresponse;
        String loginfile = DataManipulation.loginfile;
        System.out.println("登录dataLL："+ postData);
        System.out.println("登录dataid："+ DataManipulation.loginId);
        HttpResponse httpResponse = doPost(loginUrl, loginYype, postData, loginHead);
//        HttpResponse httpResponse = doPostsend(loginUrl,loginYype,postData,loginfile,loginHead);
        String body = httpResponse.getBody();
        System.out.println("登录响应1："+body);
        JSONObject jsonObjectone = JSONObject.parseObject(body);
        ImportResponse importResponse = new ImportResponse();
        //获取响应字段，并放入map集合
//        String[] split = DataManipulation.loginAssert.split("=");
//        if(split[0].toUpperCase().equals("STRING")){
//            System.out.println("登录系统成功");
//            this.myjsonmap =importResponse.myresponse(body,myresponse,this.myjsonmap);
//        }else {
//            System.out.println("登录系统失败");
//        }
        if (body.contains("操作成功")){
            String datas = jsonObjectone.getString("data");
            System.out.println("登录响应2："+datas);
            JSONObject jsonObjectones = JSONObject.parseObject(datas);
            String tokens = jsonObjectones.getString("token");
            String tokenHeads = jsonObjectones.getString("tokenHead");
            this.Authorization = tokenHeads+tokens;
            System.out.println("this.body++:"+body);
            System.out.println("this.Authorization++:"+this.Authorization);
        }else {
            System.out.println("登录失败：");
        }
   }

    /**
     *
     * @param myurl
     * @param myhead
     */
    public HttpResponse doget(String myurl,String myhead) {

        System.out.println("url:"+myurl);
        HttpResponse httpresponse = null;
        try {
            // 创建httpGet请求
            HttpGet httpGet = new HttpGet(myurl);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //设置超时
            RequestConfig requestConfig = RequestTime();
            httpGet.setConfig(requestConfig);
            // 准备请求头 （这一步看需要，若接口有添加请求头信息则直接添加，若不需要 可直接略过）
            HttpRequest httpRequest = httpGet;
            myxtgethead(myhead,httpRequest);
            //httpRequest.addHeader("Basic YW1paW50ZWxsZWN0OmFtaWludGVsbGVjdC0xMjM0NTY=", "xxxxx1");
            //httpRequest.addHeader("token2", "xxxxx2");

            // 发送请求
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            //收响应
            httpresponse = new HttpResponse(httpResponse);
            //httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpresponse;
    }

    /**
     * post请求
     * @param url
     * @param myhead
     * @param posttype
     * @param data
     */
    public HttpResponse doPost(String url,String posttype,String data,String myhead) {
        HttpResponse httpresponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            //判断请求头并给请求头附值
            myxtposthead(myhead,httpPost);
            if ("POSTFORM".equals(posttype)) {
                httpPost.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
                String params = jointBodyParam(data);
                System.out.println("转化后params:"+params);
                httpPost.setEntity(new StringEntity(params, "UTF-8"));
            } else if ("POSTJSON".equals(posttype)) {
                httpPost.addHeader(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
                httpPost.setEntity(new StringEntity(data, "UTF-8"));
            }else {
                System.out.println("当前请求方法错误：" + posttype);
            }
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //设置超时
            RequestConfig requestConfig = RequestTime();
            httpPost.setConfig(requestConfig);
            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            httpresponse = new HttpResponse(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpresponse;
    }

    /**
     * 表单及文件上传
     * @param url
     * @param methed
     * @param data
     * @param fileone
     * @param myhead
     * @return
     */
    public HttpResponse doPostsend(String url,String methed,String data,String fileone,String myhead){
        HttpResponse httpresponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //每个post参数之间的分隔。随意设定，只要不会和其他的字符串重复即可。
        String boundary ="------WebKitFormBoundaryerEzPLp0xMCUtCXe";
        try {
            HttpPost httpPost = new HttpPost(url);
            File file =null;
            //设置请求头
            //判断请求头并给请求头附值
            myxtposthead(myhead,httpPost);
            httpPost.setHeader("Content-Type","multipart/form-data; boundary="+boundary);
            //httpPost.setHeader("Authorization","Basic YW1paW50ZWxsZWN0OmFtaWludGVsbGVjdC0xMjM0NTY=");
            //HttpEntity builder
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            //字符编码
            builder.setCharset(Charset.forName("UTF-8"));
            //模拟浏览器
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            //boundary
            builder.setBoundary(boundary);
            String filepath= "file";
            //判断是否需要文件上传
            if(!(("").equals(fileone)||fileone==null)){
                System.out.println("文件上传："+fileone);
                //取文件上传名称
                if(fileone.contains("=")){
                    String[] split = fileone.split("=");
                    filepath = split[0];
                    fileone = split[1];
                }
                //多个文件进行分割文件名
                if (fileone.contains(",")){
                    String[] splitfile = fileone.split(",");
                    for(int i =1;i<splitfile.length;i++){
                        //文件file，将文件路径转化成可识别文件路径
                        String location = splitfile[i].replaceAll("\\\\", "//");
                        //System.out.println(i+"文件路径："+location);
                        file = new File(splitfile[i]);
                        //file = new File(location);
                        //文件上传addPart
                        builder.addPart(filepath,new FileBody(file));
                        //另外一种文件上传addBinaryBody
//                        String fileName =file.getName();
//                        builder.addBinaryBody("name=\"multipartFile\"; filename=\"test.docx\"",
//                        new FileInputStream(file), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
                    }
                }else {
                    //单个文件
                    String location = fileone.replaceAll("\\\\", "//");
                    //System.out.println("文件路径00："+location);
                    file = new File(location);
                    //文件上传addPart
                    builder.addPart(filepath,new FileBody(file));
                }
                //multipart/form-data  文件上传
                //builder.addPart("multipartFile",new FileBody(file));
                // binary
                //builder.addBinaryBody("name=\"multipartFile\"; filename=\"test.docx\"", new FileInputStream(file), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
            }
            //其他普通参数demo
            //builder.addTextBody("filename", fileName,  ContentType.create("text/plain", Consts.UTF_8));
            //其他普通参数传入
//            if(!(("").equals(data)||data==null)){
//                if(data.contains(",")){
//                    String[] splitone = data.split(",");
//                    for(int i=0;i<splitone.length;i++){
//                        if(splitone[i].contains("=")){
//                            String[] splittwo = data.split("=");
//                            builder.addTextBody(splittwo[0], splittwo[1],
//                                    ContentType.create("text/plain", Consts.UTF_8));
//                        }
//                    }
//                }else {
//                    if(data.contains("=")){
//                        String[] splittwo = data.split("=");
//                        builder.addTextBody(splittwo[0], splittwo[1],
//                                ContentType.create("text/plain", Consts.UTF_8));
//                    }
//                }
//
//            }
            JSONObject jsonObject = JSONObject.parseObject(data);
            int size = jsonObject.keySet().size();
            //System.out.println("普通参数size:"+size);
            for(String str:jsonObject.keySet()){
                System.out.println(str + ":" +jsonObject.get(str));
                builder.addTextBody(str, jsonObject.get(str).toString(),
                        ContentType.create("text/plain", Consts.UTF_8));
            }
            //HttpEntity
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            // 执行提交
            CloseableHttpResponse responses = httpClient.execute(httpPost);
            httpresponse = new HttpResponse(responses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpresponse;
    }

    /**
     * 暂未使用该方法表单：application/x-www-form-urlencoded
     * @param url
     * @param posttype
     * @param data
     * @param myhead
     * @return
     */
    public HttpResponse doPostfrom(String url,String posttype,String data,String myhead){
        //String url ="http://synergy.k8s.superlucy.net/api/auth/oauth/token";
        HttpResponse httpresponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            //判断请求头并给请求头附值
            myxtposthead(myhead,httpPost);
//            httpPost.addHeader(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
            httpPost.addHeader(new BasicHeader("Accept","application/json, text/plain, */*"));
//            httpPost.addHeader(new BasicHeader("Authorization","Basic c3VwZXJsdWN5X3N5bmVyZ3k6c3luZXJneS0xcWF6LTJ3c3g="));
            httpPost.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
            List<NameValuePair> list = new ArrayList<>();
            JSONObject jsonObject = JSONObject.parseObject(data);
            for(String str:jsonObject.keySet()){
                System.out.println(str + ":" +jsonObject.get(str));
                list.add(new BasicNameValuePair(str,jsonObject.get(str).toString()));
            }

//            String params = jointBodyParam(data);
//            System.out.println("转化后params:"+params);

            //将数设置到 NameValuePair
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list, Consts.UTF_8);
            httpPost.setEntity(formEntity);
            CloseableHttpResponse responses = null;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            responses = httpClient.execute(httpPost);
            //设置超时
            RequestConfig requestConfig = RequestTime();
            httpPost.setConfig(requestConfig);
            httpresponse = new HttpResponse(responses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpresponse;
    }
    public HttpResponse doPostfroms(String url){
        HttpResponse httpresponse = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            //判断请求头并给请求头附值
//            myxtposthead(myhead,httpPost);
            httpPost.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
//            String params = jointBodyParam(data);
//            System.out.println("转化后params:"+params);

            List<NameValuePair> list = new ArrayList<>();
            list.add(new BasicNameValuePair("username","0215测试001"));
            list.add(new BasicNameValuePair("username","0215测试001"));
            //将参数设置到 NameValuePair
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list, Consts.UTF_8);
            httpPost.setEntity(formEntity);

            CloseableHttpResponse responses = null;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            responses = httpClient.execute(httpPost);
            //设置超时
            RequestConfig requestConfig = RequestTime();
            httpPost.setConfig(requestConfig);
            httpresponse = new HttpResponse(responses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpresponse;
    }


    /**
     *
     * @param jsonStr post请求from入参时将json格式入参拼接
     * @return
     */
    public static String jointBodyParam(String jsonStr) {
        Map<String, Object> map = (Map<String, Object>) JSONObject.parse(jsonStr);
        Set<String> keySet = map.keySet();
        StringBuffer sBuffer = new StringBuffer();
        String[] keyArr = new String[keySet.size()];
        keySet.toArray(keyArr);
        for (int i = 0; i < keyArr.length; i++) {
            String paramName = keyArr[i];
            // 第一个参数前用  ？ 来拼接
            sBuffer.append(paramName).append("=").append(map.get(paramName)).append("&");
        }
        // 截取字符串， 从0开始，到（最后一个&出现的位置）； 是为了截掉最后一个&
        //System.out.println("from格式拼接入参："+sBuffer.substring(0, sBuffer.lastIndexOf("&")));
        return sBuffer.substring(0, sBuffer.lastIndexOf("&"));
    }

    /**
     *
     * @param myhead
     * @param httpRequest
     * @return
     * 处理get请求头
     */
    public void myxtgethead(String myhead, HttpRequest httpRequest) {
        //判断请求头并给请求头附值
        if(("").equals(myhead)||myhead==null){
            System.out.println("该请求头为空时，get默认："+myhead);
            System.out.println("该请求头为空时，get默认："+this.Authorization);
            httpRequest.addHeader(new BasicHeader(this.Authorizations, this.Authorization));
        }else if(myhead.equals("NO")){
            System.out.println("该请求头不处理："+myhead);
        }else{
            System.out.println("myhead不为空");
            if(myhead.contains("@")){
                System.out.println("该请求头有@符号："+myhead);
                String[] splitone = myhead.split("@");
                for(int i=0;i<splitone.length;i++){
                    if(splitone[i].contains(":")){
                        System.out.println("该请求头有：符号："+myhead);
                        String[] splittwo = splitone[i].split(":");
                        httpRequest.addHeader(splittwo[0], splittwo[1]);
                    }else {
                        System.out.println("该请求头格式不符合要求："+splitone[i]);
                    }
                }
            }else{
                System.out.println("该请求头没有@符号："+myhead);
                if(myhead.contains(":")){
                    System.out.println("该请求头有：符号："+myhead);
                    String[] splittwo = myhead.split(":");
                    httpRequest.addHeader(new BasicHeader(splittwo[0], splittwo[1]));
                }else{
                    System.out.println("该请求头格式不符合要求："+myhead);
                }
            }
        }
    }

    /**
     *
     * @param myhead
     * @param httpPost
     * @return
     * 处理post请求头
     */
    public void myxtposthead(String myhead,HttpPost httpPost) {
        //判断请求头并给请求头附值
        if(("").equals(myhead)||myhead==null){
            System.out.println("该请求头为空时，post默认："+myhead);
            System.out.println("登录后Authorization"+this.Authorization);
            httpPost.addHeader(new BasicHeader(this.Authorizations, this.Authorization));
        }else if(myhead.equals("NO")){
            System.out.println("该请求头不处理："+myhead);
        }else{
            System.out.println("myhead不为空");
            if(myhead.contains("@")){
                System.out.println("该请求头有@符号："+myhead);
                String[] splitone = myhead.split("@");
                for(int i=0;i<splitone.length;i++){
                    if(splitone[i].contains(":")){
                        System.out.println("该请求头有：符号："+myhead);
                        String[] splittwo = splitone[i].split(":");
                        httpPost.addHeader(splittwo[0], splittwo[1]);
                    }else {
                        System.out.println("该请求头格式不符合要求："+splitone[i]);
                    }
                }
            }else{
                System.out.println("该请求头没有@符号："+myhead);
                if(myhead.contains(":")){
                    System.out.println("该请求头有：符号："+myhead);
                    String[] splittwo = myhead.split(":");
                    httpPost.addHeader(new BasicHeader(splittwo[0], splittwo[1]));
                }else{
                    System.out.println("该请求头格式不符合要求："+myhead);
                }
            }
        }
        //判断请求头并给请求头附值
//        if (!(("").equals(myhead) || myhead == null) && myhead.contains(":")) {
//            System.out.println("myhead不为空且有：符号：" + myhead);
//            if (myhead.contains("@")) {
//                System.out.println("该请求头有@符号：" + myhead);
//                String[] splitone = myhead.split("@");
//                for (int i = 0; i < splitone.length; i++) {
//                    if (splitone[i].contains(":")) {
//                        System.out.println("该请求头有：符号：" + myhead);
//                        String[] splittwo = splitone[i].split(":");
//                        httpPost.addHeader(new BasicHeader(splittwo[0], splittwo[1]));
//                    } else {
//                        System.out.println("拆分后该请求头格式不符合要求：" + splitone[i]);
//                    }
//                }
//            } else {
//                System.out.println("该请求头没有@符号：" + myhead);
//                String[] splittwo = myhead.split(":");
//                httpPost.addHeader(new BasicHeader(splittwo[0], splittwo[1]));
//            }
//        } else {
//            System.out.println("该请求头格式不符合要求：" + myhead);
//        }
    }

    /**
     *设置超时
     * @return
     */
    public static RequestConfig RequestTime(){
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000).setConnectionRequestTimeout(50000)
                .setSocketTimeout(50000).build();
        return  requestConfig;
    }

    public static void main(String[] args) throws Exception {

        //请求的url
        String url ="https://xxx.xxx.com";
        String myurl ="http://permission.synergy.c.superlucy.net/api/auth/oauth/token";
        String myhead = "Authorization:Basic YW1paW50ZWxsZWN0OmFtaWludGVsbGVjdC0xMjM0NTY=";
        //接口请求的数据，拼接成json字符串
        String jsonStr ="{\"password\":\"sdl@123\",\"username\":\"sdltest\",\"grant_type\":\"password\"}";
        //接口请求的类型：json，form
        String submitType = "POSTFORM";
        myRequest myRequest = new myRequest();
    }


    public static String doPut(String url, String token, String jsonStr) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000).setConnectionRequestTimeout(35000).setSocketTimeout(60000).build();
        httpPut.setConfig(requestConfig);
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setHeader("DataEncoding", "UTF-8");
        httpPut.setHeader("token", token);

        CloseableHttpResponse httpResponse = null;
        try {
            httpPut.setEntity(new StringEntity(jsonStr));
            httpResponse = httpClient.execute(httpPut);
            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}

