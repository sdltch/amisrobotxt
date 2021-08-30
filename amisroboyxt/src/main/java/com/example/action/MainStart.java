package com.example.action;

import org.testng.annotations.Test;

public class MainStart {
    @Test
    public static void main(String[] args) throws Exception {
        String startTime = Report.getCurrentTime();
        DataManipulation dataManipulation = new DataManipulation();
        dataManipulation.start();
        String endTime = Report.getCurrentTime();
        Report.generateReport(startTime, endTime);
        //发送邮件
//        SendEmail sendEmail = new SendEmail();
//        sendEmail.testemail();
//        while (true) {//获取当前时间
//            String currentTime = Report.getCurrentTime();
//            System.out.println("北京时间：" + currentTime);
//            //判断当前时间是否包含设定时间
//            if (currentTime.contains("14:31:")) {
//                String startTimeone = Report.getCurrentTime();
//                DataManipulation dataManipulationone = new DataManipulation();
//                dataManipulationone.start();
//                String endTimetwo = Report.getCurrentTime();
//                Report.generateReport(startTimeone, endTimetwo);
//                break;
//            }
//            Thread.sleep(1000);
//        }
    }


}
