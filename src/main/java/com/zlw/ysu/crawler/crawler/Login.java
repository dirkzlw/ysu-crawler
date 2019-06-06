package com.zlw.ysu.crawler.crawler;

import com.zlw.ysu.crawler.pojo.User;
import com.zlw.ysu.crawler.utils.HttpUtils;
import com.zlw.ysu.crawler.utils.VCodeToText;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 登录的工具类
 */
public class Login {

    public static String login(CloseableHttpClient httpClient, User user) throws Exception {
        int i=0;
        while (true) {
            if(i==3){
                return "登录失败！请检查用户名和密码是否正确！";
            }
            //验证码保存路径
            String codeSavePath = "../../img/" + user.getUsername() + ".gif";
//            System.out.println(" [ INFO ] 请求验证码...\n");
            //得出图片上验证字符
            String vcode = VCodeToText.getCodeText(codeSavePath, httpClient);
            String loginhtml = HttpUtils.getHtml(httpClient, "http://202.206.243.62/default2.aspx", null);
//            System.out.println(" [ INFO ] 获取__VIEWSTATE...\n");
            String __VIEWSTATE = Jsoup.parse(loginhtml).select("input[name=__VIEWSTATE]").attr("value");

            //构造登录参数map
            Map<String, String> params = new HashMap<>();
            params.put("__VIEWSTATE", __VIEWSTATE);
            params.put("txtUserName", user.getUsername());
            params.put("Textbox1", "");
            params.put("Textbox2", user.getPassword());
            params.put("txtSecretCode", vcode);
            params.put("RadioButtonList1", "%D1%A7%C9%FA");
            params.put("Button1", "");
            params.put("lbLanguage", "");
            params.put("hidPdrs", "");
            params.put("hidsc", "");

            CloseableHttpResponse httpResponse = HttpUtils.post(httpClient, params, "http://202.206.243.62/default2.aspx", null);
            if (httpResponse.getStatusLine().getStatusCode() == 302) {

                //关闭response
                httpResponse.close();

                return "success";
            }else if(httpResponse.getStatusLine().getStatusCode() == 200){
                try {
                    String failHtml = HttpUtils.getHtmlFromResponse(httpResponse);
                    String text = Jsoup.parse(failHtml).select("script").get(1).data();
                    String substring = text.substring(text.indexOf("'")+1, text.indexOf(")")-1);
//                    System.out.println("substring = " + substring);
                    if (substring.equals("验证码不正确！！")){
//                        System.out.println(" [INFO] 验证码识别失败，自动进行下一次登录...\n---------------------------");
                    }else {

                        //关闭response
                        httpResponse.close();
                        return substring;
                    }
                }catch (Exception e){
                    throw new Exception();
                }
            }
            i++;
        }
    }

}
