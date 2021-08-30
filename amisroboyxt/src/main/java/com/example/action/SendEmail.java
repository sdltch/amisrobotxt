package com.example.action;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class SendEmail {
    public static void main(String[] args) throws MessagingException, GeneralSecurityException, IOException {
        File directory = new File("");
        directory. getCanonicalPath( );
        System.out.println("1234143"+directory. getCanonicalPath( ));
        //创建一个配置文件保存并读取信息
        Properties properties = new Properties();
        //设置qq邮件服务器
        properties.setProperty("mail.host","smtp.qq.com");
        //设置发送的协议
        properties.setProperty("mail.transport.protocol","smtp");
        //设置用户是否需要验证
        properties.setProperty("mail.smtp.auth", "true");
        //=================================只有QQ存在的一个特性，需要建立一个安全的链接
        // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        //=================================准备工作完毕
        //1.创建一个session会话对象；
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("596211206@qq.com", "fzhfhguoroelbbja");
            }
        });
        //可以通过session开启Dubug模式，查看所有的过程
        session.setDebug(true);
        //2.获取连接对象，通过session对象获得Transport，需要捕获或者抛出异常；
        Transport tp = session.getTransport();
        //3.连接服务器,需要抛出异常；
        tp.connect("smtp.qq.com","596211206@qq.com","fzhfhguoroelbbja");
        //4.连接上之后我们需要发送邮件；
        MimeMessage mimeMessage = imageMail(session);
        //5.发送邮件
        tp.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        //6.关闭连接
        tp.close();
    }
    public static void testemail() throws GeneralSecurityException, MessagingException, IOException {
        //创建一个配置文件保存并读取信息
        Properties properties = new Properties();
        //设置qq邮件服务器
        properties.setProperty("mail.host","smtp.qq.com");
        //设置发送的协议
        properties.setProperty("mail.transport.protocol","smtp");
        //设置用户是否需要验证
        properties.setProperty("mail.smtp.auth", "true");
        //=================================只有QQ存在的一个特性，需要建立一个安全的链接
        // 关于QQ邮箱，还要设置SSL加密，加上以下代码即可
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);
        //=================================准备工作完毕
        //1.创建一个session会话对象；
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("596211206@qq.com", "fzhfhguoroelbbja");
            }
        });
        //可以通过session开启Dubug模式，查看所有的过程
        session.setDebug(true);
        //2.获取连接对象，通过session对象获得Transport，需要捕获或者抛出异常；
        Transport tp = session.getTransport();
        //3.连接服务器,需要抛出异常；
        tp.connect("smtp.qq.com","596211206@qq.com","fzhfhguoroelbbja");
        //4.连接上之后我们需要发送邮件；
        MimeMessage mimeMessage = imageMail(session);
        //5.发送邮件
        tp.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        //6.关闭连接
        tp.close();
    }


    public static MimeMessage imageMail(Session session) throws MessagingException, IOException {
        //获取当前系统
        String mySystems = System.getProperties().getProperty("os.name");
        //System.out.println("===========os.namesxcel:"+mySystems);
        //获取当前路径
        File directory = new File("");
        String canonicalPath = directory.getCanonicalPath();
        String reportamirobot = null;
        if(mySystems.contains("Windows")){
            reportamirobot =canonicalPath+"\\amisrobot\\report\\reportamirobot.html";
            System.out.println("撒旦发射点方法烦烦烦烦烦烦烦烦烦烦烦烦"+reportamirobot);
        }else if (mySystems.contains("Linux")){
            reportamirobot =canonicalPath+"\\amisrobot\\report\\reportamirobot.html";
        }
        //消息的固定信息
        MimeMessage mimeMessage = new MimeMessage(session);
        //邮件发送人
        mimeMessage.setFrom(new InternetAddress("596211206@qq.com"));
        //邮件接收人，可以同时发送给很多人，我们这里只发给自己；

        //这里是发送给多个用户多个用户用都好分割xxx@xx.com,xxx@xx.com
        String users = "shudalong@163.com";
        Address[] internetAddressTo = new InternetAddress().parse(users);
        //单个
        //InternetAddress receiveAddress = new InternetAddress("shudalong@163.com");
//        mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress("596211206@qq.com"));
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO,  internetAddressTo);
        mimeMessage.setSubject("amirobot"); //邮件主题


        /*
        编写邮件内容
        1.图片
        2.附件
        3.文本
         */

        //文本
        MimeBodyPart body1 = new MimeBodyPart();
        //body1.setContent("请注意，这是ami测试报告 /n ddsfa<img src='cid:yhbxb.png'>","text/html;charset=utf-8");
        body1.setContent("本邮件由系统自动发出，无需回复！"+"<br>"+" <br>\n" +
                "<br>各位同事，大家好，以下为amirobot自动化测试项目测试信息\n" +
                "<br><br>项目名称 ：amirobt \n" +
                "<br>构建编号 ： \n" +
                "<br>触发原因 ： \n" +
                "<br>构建状态 ： \n" +
                "<br>构建日志 ： \n" +
                "<br>","text/html;charset=utf-8");

        //图片
        MimeBodyPart body2 = new MimeBodyPart();
//        body2.setDataHandler(new DataHandler(new FileDataSource
//                ("C:\\Users\\59621\\Pictures\\Camera Roll\\22.png")));
        body2.setDataHandler(new DataHandler(new FileDataSource(reportamirobot)));
        body2.setContentID("reportamirobot.html"); //图片设置ID



        //附件
        MimeBodyPart body3 = new MimeBodyPart();
        body3.setDataHandler(new DataHandler(new FileDataSource(reportamirobot)));
        body3.setFileName("reportamirobot.html"); //附件设置名字

//        MimeBodyPart body4 = new MimeBodyPart();
//        body4.setDataHandler(new DataHandler(new FileDataSource("src/resources/1.txt")));
//        body4.setFileName(""); //附件设置名字

        //拼装邮件正文内容
        MimeMultipart multipart1 = new MimeMultipart();
        multipart1.addBodyPart(body1);
        System.out.println();
        multipart1.addBodyPart(body2);
        multipart1.setSubType("related"); //1.文本和图片内嵌成功！

        //new MimeBodyPart().setContent(multipart1); //将拼装好的正文内容设置为主体
        MimeBodyPart contentText =  new MimeBodyPart();
        contentText.setContent(multipart1);

        //拼接附件
        MimeMultipart allFile =new MimeMultipart();
        allFile.addBodyPart(body3); //附件
        //allFile.addBodyPart(body4); //附件
        allFile.addBodyPart(contentText);//正文
        allFile.setSubType("mixed"); //正文和附件都存在邮件中，所有类型设置为mixed；


        //放到Message消息中
        mimeMessage.setContent(allFile);
        mimeMessage.saveChanges();//保存修改


        return mimeMessage;

    }

}
