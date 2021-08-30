package com.example.action;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.Arrays;
public class HttpResponse {
        private String cookie = "";
        private String body = "";
        private String myhead = "";
        private int code = 0;
        private CloseableHttpResponse connection = null;
        public HttpResponse(CloseableHttpResponse httpUrlConnection) {
            this.connection = httpUrlConnection;
        }

        public String getCookie() {
            //获取response中的cookie值
            String value = null;
            Header[] headers = this.connection.getHeaders("Set-Cookie");
            String headerstr = headers.toString();
            for (int i = 0; i < headers.length; i++) {//取出所有的cookie值
                value = headers[i].getValue();
                System.out.println("第"+i+"次的值为："+value);
                this.cookie+=value;
            }
            return  this.cookie;
        }

        public String getBody() {
            //收发数据
            body = "123";
            try {
                if (this.connection.getEntity() != null) {
                    // 将响应内容转换为字符串
                    HttpEntity entity = this.connection.getEntity();
                    // 将响应内容转换为字符串
                    this.body = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                    //this.body = EntityUtils.toString(this.connection.getEntity(), Charset.forName("UTF-8"));
                }else{
                    System.out.println("响应为空：");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.body;
        }
    /**
     *
     * @return
     */
    public int getCode() {
            try {
                this.code = this.connection.getStatusLine().getStatusCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.code;
        }

    /**
     *
      * @return
     */
    public String getHead() {
        try {
            Header[] allHeaders = this.connection.getAllHeaders();
            this.myhead = Arrays.toString(allHeaders);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.myhead;
    }

}


