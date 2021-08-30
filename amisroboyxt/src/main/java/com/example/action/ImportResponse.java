package com.example.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.Collections;
import java.util.HashMap;

/*
*将响应字段放入myjsonmap
* */
public class ImportResponse {
    //判断excel中response中是否有值，并对response中的值用“=”分割成数组
    public HashMap<String, String> myresponse(String body,String myresponse,HashMap<String, String> myjsonmap) {
        //将body转化成JSONObject
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject jsonObjectone = jsonObject;
        //System.out.println("进入以&分割:"+myresponse);
        String[] semicolon =null;
        //以分号分割
        if(myresponse.contains(";")){
            semicolon = semicolon(myresponse);
            System.out.println("有分号分割:");
        }else {
            semicolon= new String[]{myresponse};
            System.out.println("无分号分割:"+semicolon[0]);
        }
        for(int i=0;i<semicolon.length;i++){
            //System.out.println("semicolon以&分割1:"+semicolon.length);
            if(semicolon[i].contains("&")){
                //以&分割数组
                //System.out.println("myresponse以&分割2:"+i+":"+semicolon[i]);
                String[] special = special(semicolon[i]);
                if(special.length == 3){
                    String resone = special[0];
                    String restwo = special[1];
                    String resthree = special[2];
//                    System.out.println("resone: "+resone);
//                    System.out.println("adfaftwo: "+restwo);
//                    System.out.println("adfafthree: "+resthree);
                    //以=分割成数组
                    if(resone.contains("=")){
                        String[] strings = equalSign(resone);
                        for(int j=0;j<strings.length;j++){
                            System.out.println("strings=分割:"+j+"="+strings.length+"="+strings[j]);
                            if(strings[j].contains("]")){
                                jsonObjectone = myRes(strings[j], jsonObjectone);
                            }else {
                                //将value转化成JSONObject
                                System.out.println("adfaf1: "+j+"=fff="+strings[j]);
                                String stringone = jsonObjectone.getString(strings[j]);
                                System.out.println(j+"取值："+stringone);
                                jsonObjectone = JSONObject.parseObject(stringone);
                            }
                        }
                        //通过Key：s，获取value：值
                        // System.out.println("adfafone: "+jsonObject.toString());

                        String valueone = jsonObjectone.getString(restwo);
                        System.out.println("adfafthree1: "+valueone);
                        //赋值
                        myjsonmap.put(resthree, valueone);
                    }else {
                        if(resone.contains("[")){
                            jsonObjectone = myRes(resone, jsonObjectone);
                            String valueone = jsonObjectone.getString(restwo);
                            //赋值
                            myjsonmap.put(resthree, valueone);
                        }else {
                            String reone = jsonObjectone.getString(resone);
                            System.out.println("取值："+reone);
                            jsonObjectone = JSONObject.parseObject(reone);
                            String valueone = jsonObjectone.getString(restwo);
                            //赋值
                            myjsonmap.put(resthree, valueone);
                        }
                    }
                }else if(special.length == 2){
                    String stringone = jsonObjectone.getString(special[0]);
                    //赋值
                    myjsonmap.put(special[1], stringone);
                }else{
                    System.out.println("myresponse以&分割后不符合要求：" + special.toString());
                }

            }else {
                System.out.println("myresponse以&分割后不符合要求第："+i+" 个：" + semicolon[i]);
            }
            jsonObjectone =jsonObject;
        }
        return myjsonmap;
    }

    /**
     * 将
     * @param mysemicolon 将 mysemicolon以；分割成一个数组，并返回
     */
    public static String[] semicolon(String mysemicolon){
        String[] splitone = null;
        if(mysemicolon.contains(";")) {
            splitone = mysemicolon.split(";");
            System.out.println("myresponse有分号：" + mysemicolon);
        }else{
            splitone= new String[]{mysemicolon};
            System.out.println("myresponse没有有分号：" + mysemicolon);
        }
        for (String num:splitone) {
            System.out.println("分号分割成数组："+num);
        }
        return splitone;
    }

    /**
     *
     * @param myspecial 将 myspecial以&分割成一个数组，并返回
     * @return
     */
    public static String[] special(String myspecial){
        String[] splitone = null;
        System.out.println("以&分割长度："+myspecial);
        if(myspecial.contains("&")){
            splitone = myspecial.split("&");
        }else {
            splitone= new String[]{myspecial};
        }
        return splitone;
    }

    /**
     *
     * @param myequalSign 将 myequalSign以=分割成一个数组，并返回
     * @return
     */
    public static String[] equalSign(String myequalSign){
        String[] splitone = null;
        System.out.println("以=分割长度："+myequalSign);
        if(myequalSign.contains("=")){
            splitone= myequalSign.split("=");
        }else {
            splitone= new String[]{myequalSign};
        }
        return splitone;
    }


    /**
     * 以]分割;返回JSONObject
     * @param sponseone
     * @return
     */
    public static JSONObject myRes(String sponseone,JSONObject jsonObject) {
        String[] splitone = sponseone.split("]");
        JSONObject jsonObjecttwo = jsonObject;
        if (splitone.length == 2) {
            String splitsone = splitone[0];
            String splitstwos =splitone[1];
            int splitstwo;
            try {
                //将sting转化成int
                splitstwo = Integer.parseInt(splitstwos);
                System.out.println("获取以】分割后取第1位:" + splitsone);
                System.out.println("获取以】分割后取第2位:" + splitstwo);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                splitstwo=0;
            }
            //取jsonObject;key的值
            String strings = jsonObject.getString(splitsone);
            System.out.println("获取以】分割后取第1位:" + strings);
            JSONArray jsonObjectone = JSONArray.parseArray(strings);
            //取第几个
            jsonObjecttwo = (JSONObject) jsonObjectone.get(splitstwo);//得到json对象
            System.out.println("获取以】分割后数据2:" + jsonObjecttwo.toString());
        }else{
            System.out.println("获取以】分割后数据1异常:");
        }
        return jsonObjecttwo;
    }
}


