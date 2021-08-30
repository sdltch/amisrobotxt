package com.example.action;


import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JosnConversionMap {
    /**
     *传入string，转换成map
     */
    public static Map<String, String> jsonmap(String strArr) throws Exception {
//        Request request = new Request();
        System.out.println(strArr);
        Map<String, String> textMap = new HashMap<String, String>();
        String[] splitone = strArr.split(",");
        System.out.println("以,分割后："+Arrays.toString(splitone));
        //循环取值，并插入map
        for(int i=0;i<splitone.length;i++){
            String[] ss = splitone[i].split("=");
            //String[] ss = splitone[i].split(":");
            System.out.println("以=分割后："+Arrays.toString(ss));
            String sone = ss[0];
            String stwo = ss[1];
            textMap.put(sone,stwo);
        }
        System.out.println("testmap集合: "+textMap.toString());
        return textMap;
    }
//    测试
    private Map<String, String> test() throws Exception {
//        Request request = new Request();
        String  strArr1 = "file=(binary)," +
                "pkgId=BG2020081300045," +
                "type=invoice_file," +
                "attachedType=0,zip=0," +
                "path=/uploads/amiintellect-customs/dec/2020/3Q/8/7823107e-7d90-4200-814a-a29d63ed9cbf/bgd001234/BG2020081300044," +
                "appName=dec," +
                "customsBrokerId=7823107e-7d90-4200-814a-a29d63ed9cbf," +
                "companyName=bgd001\n";
        System.out.println(strArr1);
        Map<String, String> textMap1 = new HashMap<String, String>();
        String[] splitone = strArr1.split(",");
        System.out.println(Arrays.toString(splitone));
        String[] splittwo= null;
        for(int i=0;i<splitone.length;i++){
            String[] ss = splitone[i].split("=");
            //String[] ss = splitone[i].split(":");

            System.out.println(Arrays.toString(ss));
            String sone = ss[0];
            String stwo = ss[1];
            textMap1.put(sone,stwo);
        }
        System.out.println("testmap1::::"+textMap1.toString());
        String requestURL = "http://apis.develop.edge.customs.dev.amiintellect.com/api/customs/core/file/filesUpload";
        Map<String, String> textMap = new HashMap<String, String>();

        //textMap.put("file", "(binary)");
        textMap.put("pkgId", "BG2020081300045");
        textMap.put("type", "invoice_file");
        textMap.put("attachedType", "0");
        textMap.put("zip", "0");
        textMap.put("path", "/uploads/amiintellect-customs/dec/2020/3Q/8/7823107e-7d90-4200-814a-a29d63ed9cbf/bgd001234/BG2020081300044");
        textMap.put("appName", "dec");
        textMap.put("customsBrokerId", "7823107e-7d90-4200-814a-a29d63ed9cbf");
        textMap.put("companyName", "bgd001");
        System.out.println("testmap::::"+textMap.toString());
        Map<String, File> requestFile = new HashMap<String, File>();
        requestFile.put("file", new
                File("D:\\amibook\\amisbook\\1货物表头\\货物\\发票.xls"));

        //request.sendpost(requestURL, textMap1, requestFile);
        return textMap;
    }

}
