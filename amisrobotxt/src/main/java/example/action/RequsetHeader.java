package example.action;

import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicHeader;

public class RequsetHeader {

    /**
     * 处理请求头
     * @param myhead
     * @param httpRequest
     * @return
     *
     */
    public void myxthead(String myhead, HttpRequest httpRequest,String Authorization,String Authorizations) {
        //判断请求头并给请求头附值
        if(("").equals(myhead)||myhead==null){
            System.out.println("该请求头为空时，get默认："+myhead);
            System.out.println("该请求头为空时，get默认："+Authorization);
            httpRequest.addHeader(new BasicHeader(Authorizations, Authorization));
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

//    /**
//     * 处理post请求头
//     * @param myhead
//     * @param httpPost
//     * @return
//     *
//     */
//    public void myxtposthead(String myhead, HttpRequest httpPost,String Authorization,String Authorizations) {
//        //判断请求头并给请求头附值
//        if(("").equals(myhead)||myhead==null){
//            System.out.println("该请求头为空时，post默认："+myhead);
//            System.out.println("登录后Authorization"+Authorization);
//            httpPost.addHeader(new BasicHeader(Authorizations, Authorization));
//        }else if(myhead.equals("NO")){
//            System.out.println("该请求头不处理："+myhead);
//        }else{
//            System.out.println("myhead不为空");
//            if(myhead.contains("@")){
//                System.out.println("该请求头有@符号："+myhead);
//                String[] splitone = myhead.split("@");
//                for(int i=0;i<splitone.length;i++){
//                    if(splitone[i].contains(":")){
//                        System.out.println("该请求头有：符号："+myhead);
//                        String[] splittwo = splitone[i].split(":");
//                        httpPost.addHeader(splittwo[0], splittwo[1]);
//                    }else {
//                        System.out.println("该请求头格式不符合要求："+splitone[i]);
//                    }
//                }
//            }else{
//                System.out.println("该请求头没有@符号："+myhead);
//                if(myhead.contains(":")){
//                    System.out.println("该请求头有：符号："+myhead);
//                    String[] splittwo = myhead.split(":");
//                    httpPost.addHeader(new BasicHeader(splittwo[0], splittwo[1]));
//                }else{
//                    System.out.println("该请求头格式不符合要求："+myhead);
//                }
//            }
//        }
//    }
//
//    /**
//     * 处理put请求头
//     * @param myhead
//     * @param Httpput
//     * @return
//     *
//     */
//    public void myxtputhead(String myhead, HttpPut Httpput, String Authorization, String Authorizations) {
//        //判断请求头并给请求头附值
//        if(("").equals(myhead)||myhead==null){
//            System.out.println("该请求头为空时，put默认："+myhead);
//            System.out.println("该请求头为空时，put默认："+Authorization);
//            Httpput.addHeader(new BasicHeader(Authorizations, Authorization));
//        }else if(myhead.equals("NO")){
//            System.out.println("该请求头不处理："+myhead);
//        }else{
//            System.out.println("myhead不为空");
//            if(myhead.contains("@")){
//                System.out.println("该请求头有@符号："+myhead);
//                String[] splitone = myhead.split("@");
//                for(int i=0;i<splitone.length;i++){
//                    if(splitone[i].contains(":")){
//                        System.out.println("该请求头有：符号："+myhead);
//                        String[] splittwo = splitone[i].split(":");
//                        Httpput.addHeader(splittwo[0], splittwo[1]);
//                    }else {
//                        System.out.println("该请求头格式不符合要求："+splitone[i]);
//                    }
//                }
//            }else{
//                System.out.println("该请求头没有@符号："+myhead);
//                if(myhead.contains(":")){
//                    System.out.println("该请求头有：符号："+myhead);
//                    String[] splittwo = myhead.split(":");
//                    Httpput.addHeader(new BasicHeader(splittwo[0], splittwo[1]));
//                }else{
//                    System.out.println("该请求头格式不符合要求："+myhead);
//                }
//            }
//        }
//    }
}
