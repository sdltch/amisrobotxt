package example.action;

import com.alibaba.fastjson.JSONObject;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.beans.binding.StringExpression;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import oracle.jrockit.jfr.VMJFR;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.log4j.Logger;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.*;

public class myRequest {
    Logger logger = Logger.getLogger(myRequest.class);
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
        String loginAssert = DataManipulation.loginAssert;
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
        if(loginAssert.contains("=")){
            String[] split = loginAssert.split("=");
            loginAssert = split[1];
        }
        if (body.contains(loginAssert)){
            String datas = jsonObjectone.getString("data");
            System.out.println("登录响应data："+datas);
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
            RequsetHeader requsetHeader = new RequsetHeader();
            requsetHeader.myxthead(myhead,httpRequest,Authorization,Authorizations);
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
            HttpRequest httpRequest = httpPost;
            RequsetHeader requsetHeader = new RequsetHeader();
            requsetHeader.myxthead(myhead,httpRequest,Authorization,Authorizations);
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
            HttpRequest httpRequest = httpPost;
            //设置请求头
            //判断请求头并给请求头附值
            RequsetHeader requsetHeader = new RequsetHeader();
            requsetHeader.myxthead(myhead,httpRequest,Authorization,Authorizations);
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
     * post请求from入参时将json格式入参拼接
     * @param jsonStr
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
     *  put请求
     * @param url
     * @param posttype
     * @param data
     * @param myhead
     * @return
     */
    public HttpResponse doPut(String url,String posttype,String data,String myhead) {
        HttpResponse httpresponse = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        //设置超时
        RequestConfig requestConfig = RequestTime();
        httpPut.setConfig(requestConfig);
        HttpRequest httpRequest = httpPut;
        //判断请求头并给请求头附值
        RequsetHeader requsetHeader = new RequsetHeader();
        requsetHeader.myxthead(myhead,httpRequest,Authorization,Authorizations);
        if ("PUTJSON".equals(posttype)) {
            httpPut.addHeader(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
            httpPut.setEntity(new StringEntity(data, "UTF-8"));
        }else if("PUTFROM".equals(posttype)) {
            httpPut.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
            String params = jointBodyParam(data);
            System.out.println("转化后params:"+params);
            httpPut.setEntity(new StringEntity(params, "UTF-8"));
        }else {
            System.out.println("当前不支持该请求方法：" + posttype);
        }
        CloseableHttpResponse httpResponse = null;
        try {
            httpPut.setEntity(new StringEntity(data));
            CloseableHttpResponse execute = httpClient.execute(httpPut);
            httpresponse = new HttpResponse(execute);
            return httpresponse;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * delete请求 json
     * @param myurl
     * @param data
     * @param xmyhead
     * @return
     */
    public HttpResponse doDeletejson(String myurl,String data,String xmyhead){
        HttpResponse httpresponse = null;
        PutRequest putRequest = new PutRequest();
        try {
            // 创建httpDelete请求
            if(!myurl.contains("$")){
                HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(myurl);
                //HttpDelete httpDelete = new HttpDelete(myurl);
                CloseableHttpClient httpClient = HttpClients.createDefault();
                //设置超时
                RequestConfig requestConfig = RequestTime();
                httpDelete.setConfig(requestConfig);
                // 准备请求头 （这一步看需要，若接口有添加请求头信息则直接添加，若不需要 可直接略过）
                HttpRequest httpRequest = httpDelete;
                RequsetHeader requsetHeader = new RequsetHeader();
                requsetHeader.myxthead(xmyhead,httpRequest,Authorization,Authorizations);
                httpDelete.addHeader(new BasicHeader("Content-Type", "application/json;charset=UTF-8"));
                httpDelete.setEntity(new StringEntity(data, "UTF-8"));
                //设置请求参数json
                StringEntity stringEntity = new StringEntity(data);
                httpDelete.setEntity(stringEntity);
                // 发送请求
                CloseableHttpResponse httpResponses = httpClient.execute(httpDelete);
                //收响应
                httpresponse = new HttpResponse(httpResponses);
                //httpClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpresponse;
    }

    /**
     *  Delete请求from
     * @param myurl
     * @param xmyhead
     * @return
     */
    public HttpResponse doDeletefrom(String myurl,String xmyhead){
        HttpResponse httpresponse = null;
        PutRequest putRequest = new PutRequest();
        try {
            // 创建httpDelete请求
            if(!myurl.contains("$")){
                HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(myurl);
                //HttpDelete httpDelete = new HttpDelete(myurl);
                CloseableHttpClient httpClient = HttpClients.createDefault();
                //设置超时
                RequestConfig requestConfig = RequestTime();
                httpDelete.setConfig(requestConfig);
                // 准备请求头 （这一步看需要，若接口有添加请求头信息则直接添加，若不需要 可直接略过）
                HttpRequest httpRequest = httpDelete;
                RequsetHeader requsetHeader = new RequsetHeader();
                requsetHeader.myxthead(xmyhead,httpRequest,Authorization,Authorizations);
                httpDelete.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8"));
                // 发送请求
                CloseableHttpResponse httpResponses = httpClient.execute(httpDelete);
                //收响应
                httpresponse = new HttpResponse(httpResponses);
                //httpClient.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpresponse;
    }

    public String jsonDeleteRequest(final String url, final Map<String, Object> param) throws Exception {

        //解决https请求证书的问题
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, (X509Certificate[] x509Certificates, String s) -> true);
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", socketFactory).build();
        HttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).build();

        String responseBody = null;
        // 创建默认的httpClient实例.
//		final CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            //以post方式请求网页
            final HttpDeleteWithBody delete = new HttpDeleteWithBody(url);
            //将参数转为JSON格式
            final Gson gson = new Gson();
            final String jsonParam = gson.toJson(param);

            delete.setHeader("Content-Type", "application/json;charset=UTF-8");
            delete.setHeader("accept","application/json");
            //将POST参数以UTF-8编码并包装成表单实体对象
            final StringEntity se = new StringEntity(jsonParam, "UTF-8");
            se.setContentType("text/json");
            delete.setEntity(se);
            final CloseableHttpResponse response = httpClient.execute(delete);
            try {
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    logger.info("准备获取返回结果");
                    responseBody = EntityUtils.toString(entity, "UTF-8");
                    logger.info("获取返回结果为：" + responseBody);
                }
            } finally {
                response.close();
            }
            logger.info(responseBody);
        }catch(Exception e){
            logger.error("接口请求失败：url=" + url, e);
        }finally {
            // 当不再需要HttpClient实例时,关闭连接管理器以确保释放所有占用的系统资源
            httpClient.getConnectionManager().shutdown();
        }
        return responseBody;
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

}

