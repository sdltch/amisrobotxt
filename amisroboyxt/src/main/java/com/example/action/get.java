//package com.example.action;
//
//import org.apache.http.Header;
//import org.apache.http.HttpRequest;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicHeader;
//import org.apache.http.util.EntityUtils;
//
//import java.util.Arrays;
//
//public class get {
//    public static void main(String[] args){
//        get gets = new get();
//        String myurl = "http://synergy.k8s.superlucy.net";
//        String interfaceUrl = "/api/auth/oauth/pubKey";
//        //String myhead = "Authorization:Basic YW1paW50ZWxsZWN0OmFtaWludGVsbGVjdC0xMjM0NTY=";
//        String myhead = "";
//        String myxturl = myurl+interfaceUrl;
//        gets.doget(myxturl,myhead);
//    }
//
//    /**
//     *
//     * @param myurl
//     * @param myhead
//     */
//    public void doget(String myurl,String myhead) {
//        System.out.println("url:"+myurl);
//        try {
//            // 创建httpGet请求
//            HttpGet httpGet = new HttpGet(myurl);
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//
//            // 准备请求头 （这一步看需要，若接口有添加请求头信息则直接添加，若不需要 可直接略过）
//            HttpRequest httpRequest = httpGet;
//            myxtgethead(myhead,httpRequest);
//            //httpRequest.addHeader("Authorization", "Basic YW1paW50ZWxsZWN0OmFtaWludGVsbGVjdC0xMjM0NTY=");
//            //httpRequest.addHeader("token2", "xxxxx2");
//
//            // 发送请求
//            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
//            int httpCode = httpResponse.getStatusLine().getStatusCode();
//            Header[] header = httpResponse.getAllHeaders();
//            String result = EntityUtils.toString(httpResponse.getEntity());
//
//            System.out.println("接口的响应状态码" + httpCode);
//            System.out.println("接口的响应头" + Arrays.toString(header));
//            System.out.println("接口的响应报文" + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     *
//     * @param myhead
//     * @param httpRequest
//     * @return
//     * 处理get请求头
//     */
//    public static void myxtgethead(String myhead, HttpRequest httpRequest) {
//        //判断请求头并给请求头附值
//        if(!(("").equals(myhead)||myhead==null)) {
//            System.out.println("myhead不为空");
//            if(myhead.contains("@")){
//                System.out.println("该请求头有@符号："+myhead);
//                String[] splitone = myhead.split("@");
//                for(int i=0;i<splitone.length;i++){
//                    if(splitone[i].contains(":")){
//                        System.out.println("该请求头有：符号："+myhead);
//                        String[] splittwo = splitone[i].split(":");
//                        httpRequest.addHeader(splittwo[0], splittwo[1]);
//                    }else {
//                        System.out.println("该请求头格式不符合要求："+splitone[i]);
//                    }
//                }
//            }else{
//                System.out.println("该请求头没有@符号："+myhead);
//                if(myhead.contains(":")){
//                    System.out.println("该请求头没有：符号："+myhead);
//                    String[] splittwo = myhead.split(":");
//                    httpRequest.addHeader(new BasicHeader(splittwo[0], splittwo[1]));
//                }else{
//                    System.out.println("该请求头格式不符合要求："+myhead);
//                }
//            }
//        }
//    }
//}
