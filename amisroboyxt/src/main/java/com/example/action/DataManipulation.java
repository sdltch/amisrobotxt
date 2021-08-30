package com.example.action;
import com.example.data.Excel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class DataManipulation {
    /**
     *  循环获取数据，并根据获取的get、post、sendpost调用不同的方法；
     * 对接口返回结果进行断言
     */

    HashMap<String, String> myjsonmap = new HashMap<String, String>();
    //static ArrayList<String[]>  readExcels = null;
    static List<String[]> readExcels = null;
    //域名/url/入参/响应
    static String loginId = "";
    static String myurlRpa = "";
    static String loginUrl = "";
    static String loginData = "";
    static String loginAssert = "";
    static String loginYype = "";
    static String longinhead = "";
    static String RSA_PUB_KEY = "";
    static String myrequests = "";
    static String myresponse = "";
    static String loginfile = "";
    public void start() throws Exception {
        System.out.println("sdds");

        HttpResponse response = null;
        String body = "";
        readExcels = Excel.judgeExcel();
        //ArrayList<String[]> readExcels = Excel.readExcelx();
        if(readExcels != null){
            //取第二行域名
            String[] readexcelone = readExcels.get(1);
            myurlRpa = readexcelone[1];
            System.out.println("域名："+myurlRpa);
            //取第五行登录数据
            String[] readexcelfive = readExcels.get(4);
            loginId = readexcelfive[0];
            loginYype = readexcelfive[2];
            loginUrl = readexcelfive[3];
            loginData = readexcelfive[4];
            loginAssert = readexcelfive[5];
            loginfile = readexcelfive[6];
            myrequests = readexcelfive[7];
            myresponse = readexcelfive[8];
            longinhead = readexcelfive[10];
            //删除前第几行
            int tag = 3;
            for(int i=0;i < tag; i++){
                readExcels.remove(0);
            }
            //实例化对象
            myRequest sss = new myRequest();

            //Request ss = new Request();
            //循环取值
            for (String[] line : readExcels) {
                //编号
                String caseID = line[0];
                System.out.println("开始----------------------------------------------------------");
                System.out.println("编号："+line[0]);
                //方法
                String method = line[2];
                //地址
                String interfaceUrl = line[3];
                String url = myurlRpa + interfaceUrl;
                //String url = line[3];
                System.out.println("：url路径："+url);
                //参数
                String data = line[4];
                //断言
                String expected = line[5];
                //文件路径
                String refile = line[6];
                System.out.println("：文件路径："+refile);
                //根据给定的值，更改请求参数
                String myrequest = line[7];
                System.out.println("myrequest------"+myrequest);
                //根据给定的值，取响应值
                String myresponse = line[8];
                System.out.println("myresponse------"+myresponse);
                //备注
                String noteCase = line[9];
                //head
                String xthead = line[10];
                //获取响应值
                //Map<String, File> requestFiles = new HashMap<String, File>();
                //JosnConversionMap josnConversionMap = new JosnConversionMap();
                ImportResponse importResponse = new ImportResponse();
                PutRequest putRequest = new PutRequest();
                //根据method判定调用方法
                if (method.contains("POST")) {
                    System.out.println("当前请求："+method);
                    //判断是否存在入参
                    System.out.println("转化前data：" + data);
                    if(!(("").equals(data)||data==null)){
                        data = putRequest.myrequest(data,this.myjsonmap);
                        System.out.println("转化后data：" + data);
//                        //myrequest不等于空
//                        if (!(("").equals(myrequest)||myrequest==null)) {
//                            System.out.println("request不为空："+method);
//                            //data = putRequest.myrequest(data, myrequest, this.myjsonmap);
//                            //替换
//                            data = putRequest.myrequest(data,this.myjsonmap);
//                            System.out.println("转化后data：" + data);
//                            System.out.println("转化后url：" + url);
////                            System.out.println("转化后方法：" + method);
////                            System.out.println("转化后xthead：" + xthead);
//                        }else{
//                            System.out.println("当前请求："+method+";替换值为空不需要替换");
//                        }
                    }else {
                        System.out.println("当前请求："+method+"没有入参");
                    }

//                    //对登录接口的密码进行加密
//                    if (line[1].equals("login")){
//                        data = rsaType(data, method);
//                        System.out.println("加密data："+data);
//                    }

                    if(method.equals("POSTJSON")){
                        response = sss.doPost(url,method,data,xthead);
                    }else if(method.equals("POSTFORM")){
                        response = sss.doPost(url,method,data,xthead);
                    }else if(method.equals("POSTSEND")){
                        response = sss.doPostsend(url,method,data,refile,xthead);
                    }
                    body = response.getBody();
                } else if (method.contains("GET")) {
                    System.out.println("当前请求sss："+method+"; url:"+url);
                    //判断是否存在入参   直接替换
                    if(!(("").equals(data)||data==null)){
                        data = putRequest.myrequest(data,this.myjsonmap);
                        url = url+"?"+data;
                        System.out.println("转化后geturl:"+url);
                    }
                    // 判断是否存在入参   通过request替换
//                    if(!(("").equals(data)||data==null)){
//                        //判断是否需要替换
//                        if(!(("").equals(myrequest)||myrequest==null)){
//                            System.out.println("当前get入参:"+data+" ;需要替换");
//                            //替换
////                            data = getquest(data,myrequest);
//                            data = putRequest.myrequests(data,this.myjsonmap);
//                            if(!(("").equals(data)||data==null)){
//                                url = url+"?"+data;
//                                System.out.println("转化后geturl:"+url);
//                            }
//                        }else {
//                            System.out.println("当前get入参不需要替换");
//                        }
//                    }
                    System.out.println("当前请求："+method+"; url:"+url);
                    response = sss.doget(url,xthead);
                    body = response.getBody();
                } else if(method.equals("PUT")){
                    System.out.println("当前请求："+method);
                    //判断是否存在入参   直接替换
                    if(!(("").equals(data)||data==null)){
                        data = putRequest.myrequest(data,this.myjsonmap);
                    }
                    System.out.println("转化后data：" + data);
//                    response = ss.doput(url, data);
//                    body = response.getBody();
                } else if(method.equals("DELETE")){
                    System.out.println("当前请求："+method);
                } else {
                    System.out.println("(系统暂时只支持get,post,sendpost,put,delete请求):当前请求：" + method);
                    break;
                }
//                System.out.println("response------"+response.toString());
                try {
                    System.out.println(line[0]+"++++响应："+body);
                    System.out.println(line[0]+"++++code："+response.getCode());
                    //调用断言
//                    body = new String(body.getBytes("gbk"),"utf-8");
//                    noteCase = new String(noteCase.getBytes("gbk"),"utf-8");
                    boolean assertIt = MyAssert.assertIt(caseID, body, expected, interfaceUrl, noteCase);
                    //Assert.assertEquals();
                    //获取响应字段，并放入map集合
                    if(assertIt == true){
                        if(!(("").equals(myresponse)||myresponse==null)){
                            System.out.println("接口调用成功后，响应："+myresponse+"开始赋值------------------");
                            this.myjsonmap =importResponse.myresponse(body,myresponse,this.myjsonmap);
                            System.out.println("接口调用成功后，响应："+myresponse+"结束赋值+++++++++++++++++++");
                        }else {
                            System.out.println("接口调用成功后，替换值response为空，不赋值------------------");
                        }
                    }else{
                        System.out.println("接口断言失败");
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
//    public void mytest(ArrayList<String[]> readExcels){
//        HttpResponse response = null;
//        String body = "";
//        //实例化对象
//        myRequest sss = new myRequest();
//        //String s = readExcels.get(0)[0];
//        //循环取值
//        for (String[] line : readExcels) {
//            //编号
//            String caseID = line[0];
//            System.out.println("开始----------------------------------------------------------");
//            System.out.println("编号："+line[0]);
//            //方法
//            String method = line[2];
//            //地址
//            String interfaceUrl = line[3];
//            String url = myurlRpa + interfaceUrl;
//            //String url = line[3];
//            System.out.println("：url路径："+url);
//            //参数
//            String data = line[4];
//            //断言
//            String expected = line[5];
//            //文件路径
//            String refile = line[6];
//            System.out.println("：文件路径："+refile);
//            //根据给定的值，更改请求参数
//            String myrequest = line[7];
//            System.out.println("myrequest------"+myrequest);
//            //根据给定的值，取响应值
//            String myresponse = line[8];
//            System.out.println("myresponse------"+myresponse);
//            //备注
//            String noteCase = line[9];
//            //head
//            String xthead = line[10];
//            //获取响应值
//            //Map<String, File> requestFiles = new HashMap<String, File>();
//            //JosnConversionMap josnConversionMap = new JosnConversionMap();
//            ImportResponse importResponse = new ImportResponse();
//            PutRequest putRequest = new PutRequest();
//            //根据method判定调用方法
//            if (method.contains("POST")) {
//                System.out.println("当前请求："+method);
//                //判断是否存在入参
//                if(!(("").equals(data)||data==null)){
//                    //myrequest不等于空
//                    if (!(("").equals(myrequest)||myrequest==null)) {
//                        System.out.println("request不为空："+method);
//                        data = putRequest.myrequest(data, myrequest, this.myjsonmap);
////                        putRequest.myrequests(data,this.myjsonmap);
//                        System.out.println("转化后data：" + data);
//                        System.out.println("转化后url：" + url);
////                        System.out.println("转化后方法：" + method);
////                        System.out.println("转化后xthead：" + xthead);
//                    }else{
//                        System.out.println("当前请求："+method+";替换值为空不需要替换");
//                    }
//                }else {
//                    System.out.println("当前请求："+method+"没有入参");
//                }
//
////                    //对登录接口的密码进行加密
////                    if (line[1].equals("login")){
////                        data = rsaType(data, method);
////                        System.out.println("加密data："+data);
////                    }
//
//                if(method.equals("POSTJSON")){
//                    response = sss.doPost(url,method,data,xthead);
//                }else if(method.equals("POSTFORM")){
//                    response = sss.doPost(url,method,data,xthead);
//                }else if(method.equals("POSTSEND")){
//                    response = sss.doPostsend(url,method,data,refile,xthead);
//                }
//                body = response.getBody();
//            } else if (method.contains("GET")) {
//                System.out.println("当前请求："+method+"; url:"+url);
//                //判断是否存在入参
//                if(!(("").equals(data)||data==null)){
//                    //判断是否需要替换
//                    System.out.println("当前get入参:"+data);
//                    if(!(("").equals(myrequest)||myrequest==null)){
//                        System.out.println("当前get入参:"+data+" ;需要替换");
//                        data = getquest(data,myrequest);
//                        if(!(("").equals(data)||data==null)){
//                            url = url+"?"+data;
//                            System.out.println("转化后geturl:"+url);
//                        }
//                    }else {
//                        System.out.println("当前get入参不需要替换");
//                    }
//                }
//                System.out.println("当前请求："+method+"; url:"+url);
//                response = sss.doget(url,xthead);
//                body = response.getBody();
//            } else if(method.equals("PUT")){
//                System.out.println("当前请求："+method);
//                //myrequest不等于空
//                if (!myrequest.equals("")) {
//                    data = putRequest.myrequest(data, myrequest, this.myjsonmap);
//                }
//                System.out.println("转化后data：" + data);
////                    response = ss.doput(url, data);
////                    body = response.getBody();
//            } else if(method.equals("DELETE")){
//                System.out.println("当前请求："+method);
//            } else {
//                System.out.println("(系统暂时只支持get,post,sendpost,put,delete请求):当前请求：" + method);
//                break;
//            }
//            System.out.println("response------"+response.toString());
//            try {
//                System.out.println(line[0]+"++++响应："+body);
//                System.out.println(line[0]+"++++code："+response.getCode());
//                //调用断言
////                    body = new String(body.getBytes("gbk"),"utf-8");
////                    noteCase = new String(noteCase.getBytes("gbk"),"utf-8");
//                boolean assertIt = MyAssert.assertIt(caseID, body, expected, interfaceUrl, noteCase);
//                //Assert.assertEquals();
//                //获取响应字段，并放入map集合
//                if(assertIt == true){
//                    if(!(("").equals(myresponse)||myresponse==null)){
//                        System.out.println("接口调用成功后，响应："+myresponse+"开始赋值------------------");
//                        this.myjsonmap =importResponse.myresponse(body,myresponse,this.myjsonmap);
//                        System.out.println("接口调用成功后，响应："+myresponse+"结束赋值------------------");
//                    }else {
//                        System.out.println("接口调用成功后，替换值response为空，不赋值------------------");
//                    }
//                }else{
//                    System.out.println("接口调用失败");
//                }
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        System.out.println("结束==========================================================");
//    }

    /**
     * 替换get入参
     * @param data  原始值
     * @param myrequest  替换值
     * @return
     */
    public String putgetRequest(String data,String myrequest,String myrequestone){
        //判断是否存在入参
        String ss=null;
        if(data.contains("&")){
            String[] split = data.split("&");
            for(int i=0;i<split.length;i++){
                if(split[i].contains("=")){
                    String[] splitone = split[i].split("=");
                        if(splitone.length==2){
                            String valueone = splitone[0];
                            String valuetwo = splitone[1];
                                if(valueone.equals(myrequest)){
                                    valuetwo = this.myjsonmap.get(myrequestone);
                                    System.out.println(myrequest+" :替换值："+valuetwo);
                                    if(split.length == (i+1)){
                                        ss+= valueone+"="+valuetwo;
                                    }else {
                                        ss+= valueone+"="+valuetwo+"&";
                                    }
                                }else {
                                    if(split.length == (i+1)){
                                        ss+= valueone+"="+valuetwo;
                                    }else {
                                        ss+= valueone+"="+valuetwo+"&";
                                    }
                                }
                        }else {
                            System.out.println("入参以=分割后长度不为2：" + data);
                        }
                }else {
                    System.out.println("入参以&分割后没有=分割：" + data);
                }
            }
        }
        System.out.println("处理后get入参：" + ss);
        return ss;
    }

    /**
     *
     * @param data
     * @param myrequest
     * @return
     */
    public String getquest(String data,String myrequest){
        String[] splittwo=null;
        String mydata = null;
        if(myrequest.contains(";")){
            String[] splitone = myrequest.split(";");
            for(int i =0;i<splitone.length;i++){
                if(splitone[i].contains("=")){
                    splittwo = splitone[i].split("=");
                    String valueone = splittwo[0];
                    String valuetwo = splittwo[1];
                    mydata = putgetRequest(data,valueone,valuetwo);
                }
            }
        }else {
            if(myrequest.contains("=")){
                splittwo = myrequest.split("=");
                String valueone = splittwo[0];
                String valuetwo = splittwo[1];
                mydata = putgetRequest(data,valueone,valuetwo);
            }
        }
      return mydata;
    }




    //判断请求方式
//    public String rsaType(String data ,String myType){
//        System.out.println("密码"+data);
//        if (myType.equals("POST")){
//            JSONObject jsonObject = JSONObject.parseObject(data);
//            String passwordone = (String)jsonObject.get("password");
//            System.out.println("密码"+passwordone);
//            //加密密码
//            String rsapasswords = rsapassword(passwordone);
//            System.out.println("加密密码"+rsapasswords);
//            //更改requesttwo的数据
//            jsonObject.put("password", rsapasswords);
//            //转化成String
//            data = JSONObject.toJSONString(jsonObject);
//        }else if (myType.equals("POSTFROM")){
//            /* 找出指定的2个字符在 该字符串里面的 位置 */
//            int strStartIndex = data.indexOf("&password=");
//            int strEndIndex = data.indexOf("&grant_type=");
//
//            /* index 为负数 即表示该字符串中 没有该字符 */
//            if (strStartIndex < 0) {
//                return "字符串 :---->" + data + "<---- 中不存在 " + "&password=" + ", 无法截取目标字符串";
//            }
//            if (strEndIndex < 0) {
//                return "字符串 :---->" + data + "<---- 中不存在 " + "&grant_type=" + ", 无法截取目标字符串";
//            }
//            /* 开始截取 */
//            String result = data.substring(strStartIndex, strEndIndex).substring("&password=".length());
//            //加密密码
//            String rsapasswords = rsapassword(result);
//            System.out.println("加密密码"+rsapasswords);
//            //替换加密后的密码
//            data=data.replace(result,rsapasswords);
//        }
//        return data;
//    }
//    public String setRequest(String data,String myrequest,HashMap<String, String> myjsonmap) {
//        //判断是否存在=
//        if (myrequest.contains("=")) {
//            //判断是否存在；
//            if (myrequest.contains(";")) {
//                //以；进行分割
//                String[] requestsp = myrequest.split(";");
//                for (int i = 0; i < requestsp.length; i++) {
//                    String[] requestspone = requestsp[i].split("=");
//                    System.out.println("request长度:" + requestspone.length);
//                    System.out.println("requestid:" + myjsonmap.get(requestspone[1]));
//                    if (data.contains(";")){
//                        String[] datas = data.split(";");
//                        for (int j = 0; j < datas.length; j++){
//                            data=data.replace(datas[j].split("=")[1],myjsonmap.get(requestspone[1]));
//                        }
//                    }
//                }
//            } else {
//                String[] requestspone = myrequest.split("=");
//                System.out.println("request长度:" + requestspone.length);
//                System.out.println("requestid:" + myjsonmap.get(requestspone[1]));
//                data=data.replace(data.split("=")[1],myjsonmap.get(requestspone[1]));
//            }
//        }
//        return data;
//    }


}
