package com.zlw.ysu.crawler.test;

import com.zlw.ysu.crawler.crawler.QueryClass;
import com.zlw.ysu.crawler.pojo.User;
import com.zlw.ysu.crawler.utils.HttpUtils;
import com.zlw.ysu.crawler.crawler.Login;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;

import java.util.Scanner;

/**
 * 测试类
 */
public class Test {

    public static void main(String[] args) {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        String account = "160104010170";
        String pw = "627911zz";
        User user = new User();
        user.setUsername(account);
        user.setPassword(pw);
        user.setCookieStore(cookieStore);
        System.out.println("-------------------------------");
        try {
            //登录
            try {
                Login.login(httpClient, user);
            } catch (Exception e) {
                System.out.println("登录失败");
                System.exit(-1);
            }
            //获取系统主页源码
            String indexhtml = HttpUtils.getHtml(httpClient, "http://202.206.243.62/xs_main.aspx?xh=" + account, null);
            //获取用户真实姓名
            String name = Jsoup.parse(indexhtml).getElementById("xhxm").ownText().replace("同学", "");
            user.setRealname(name);
            System.out.print("[ " + name + " ]登录成功！是否查看课表？[y/n]: ");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            if (s.equals("y")) {

                QueryClass.query(httpClient, user, 8);

            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
