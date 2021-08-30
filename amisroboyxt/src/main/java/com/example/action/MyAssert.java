package com.example.action;


import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
@Test
public class MyAssert {
    //断言方法
//    @Test
    public static boolean assertIt(String caseID, String actual, String myExpected,
                 String interfaceUrl,String notecase) throws UnsupportedEncodingException {
        String key="file";
        if(myExpected.contains("=")){
            String[] split = myExpected.split("=");
            key = split[0];
            myExpected = split[1];
        }
        String[] a = {caseID};
        boolean assertone = false;
//        Assert.assertEquals(actual,myExpected);
//        System.out.println("STRING ++++++ Test Case Pass; 用例编号：" + caseID);

        //断言
        if (key.toUpperCase().equals("STRING")) {
            if (actual.contains(myExpected)) {
                System.out.println("STRING ++++++ Test Case Pass; 用例编号：" + caseID);
                System.out.println("断言结束----------------------------------------------------------");
                Report.recordResult(a, true, actual,interfaceUrl,notecase);
                assertone =true;
            } else {
                System.out.println("STRING ++++++ Test Case Fail; 用例编号：" + caseID);
                System.out.println("断言结束----------------------------------------------------------");
                Report.recordResult(a, false, actual,interfaceUrl,notecase);
            }
        } else if (key.toUpperCase().equals("TYPE")) {
            if (myExpected.equals("ISNUMBER")) {
                System.out.println("#######################" + actual);
                if (actual.matches("^[1-9][0-9]*$")) {
                    System.out.println("TYPE  @@@@@@@@Test Case Pass; 用例编号：" + caseID);
                    System.out.println("断言结束----------------------------------------------------------");
                    Report.recordResult(a, true, actual,interfaceUrl,notecase);
                    assertone =true;
                } else {
                    System.out.println("TYPE  @@@@@@@@Test Case Fail; 用例编号：" + caseID);
                    System.out.println("断言结束----------------------------------------------------------");
                    Report.recordResult(a, false, actual,interfaceUrl,notecase);
                }
            } else if (myExpected.toUpperCase().equals("NOTNUMBER")) {
                if (!actual.matches("^[1-9][0-9]*$")) {
                    System.out.println("TYPE  ------Test Case Pass; 用例编号：" + caseID);
                    System.out.println("断言结束----------------------------------------------------------");
                    Report.recordResult(a, true, actual,interfaceUrl,notecase);
                    assertone =true;
                } else {
                    System.out.println("TYPE  ------Test Case Fail; 用例编号：" + caseID);
                    System.out.println("断言结束----------------------------------------------------------");
                    Report.recordResult(a, false, actual,interfaceUrl,notecase);
                }
            }
        }else {
                System.out.println("null ++++++ Test Case 断言为null; 用例编号：" + caseID);
                System.out.println("断言结束----------------------------------------------------------");
                Report.recordResult(a, false, actual,interfaceUrl,notecase);
        }
        return assertone;
    }
}
