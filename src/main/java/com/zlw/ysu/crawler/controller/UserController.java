package com.zlw.ysu.crawler.controller;

import com.zlw.ysu.crawler.crawler.Login;
import com.zlw.ysu.crawler.crawler.QueryClass;
import com.zlw.ysu.crawler.crawler.QueryClassroom;
import com.zlw.ysu.crawler.crawler.QueryExam;
import com.zlw.ysu.crawler.crawler.QueryGrade;
import com.zlw.ysu.crawler.service.UserService;
import com.zlw.ysu.crawler.utils.MD5Utils;
import com.zlw.ysu.crawler.vo.Classes;
import com.zlw.ysu.crawler.pojo.User;
import com.zlw.ysu.crawler.utils.HttpUtils;
import com.zlw.ysu.crawler.vo.Classroom;
import com.zlw.ysu.crawler.vo.Exam;
import com.zlw.ysu.crawler.vo.Grade;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Ranger
 * @create 2019-05-15 14:00
 */
@Controller
public class UserController {

    //注入
    @Value("${SCHOOL_START_TIME}")
    private String SCHOOL_START_TIME;

    @Autowired
    private UserService userService;

    private CookieStore cookieStore = new BasicCookieStore();
    private CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    private User user;

    /**
     * 跳转到主页
     */
    @GetMapping("/index")
    public String toIndex(Model model){
        model.addAttribute("realname", this.user.getRealname());
        return "index";
    }

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public String login(User user,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response){
        user.setCookieStore(this.cookieStore);

        try {
            String login = Login.login(this.httpClient, user);
            if("success".equals(login)){
                //获取系统主页源码
                String indexhtml = HttpUtils.getHtml(this.httpClient, "http://202.206.243.62/xs_main.aspx?xh=" + user.getUsername(), null);
                //获取用户真实姓名
                String name = Jsoup.parse(indexhtml).getElementById("xhxm").ownText().replace("同学", "");
                user.setRealname(name);
                this.user = user;

                //将JSESSIONID存于本地cookie中
                Cookie cookiesessionid = new Cookie("JSESSIONID",request.getSession().getId());
                cookiesessionid.setMaxAge(24*60*60);
                response.addCookie(cookiesessionid);

                //存Session
                //根据用户角色，设置用户权限
                HttpSession session = request.getSession();
                session.setAttribute("sessionUser", user);
                session.setMaxInactiveInterval(3*24*60);    //设置session生存时间

                //创建新线程，将用户信息保存到数据库
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //密码加密
                        user.setPassword(MD5Utils.md5(user.getPassword()));
                        userService.saveUser(user);
                    }
                }).start();

                return "loginSuccess";
            }else{
                return "loginFail";
            }
        }catch (Exception e){
            return "loginFail";
        }
    }

    /**
     * 查询成绩
     */
    @GetMapping("/query/grade")
    public String toGrade(Model model){

        try {
            List<Grade> gradeList = QueryGrade.query(this.httpClient, this.user);
            model.addAttribute("gradeList", gradeList);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "grade";
    }

    /**
     * 查询课程表
     */
    @GetMapping("/query/classes")
    public String toClass(Model model,Integer weekNum){
        if(weekNum == null){
            try {
                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date1 = sdf.parse(SCHOOL_START_TIME);
                String dateStr2 = sdf.format(currentDate);
                Date date2 = sdf.parse(dateStr2);

                weekNum = QueryClass.differentDays(date1, date2)/7+1;

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        try {
            List<Classes> classesList = QueryClass.query(this.httpClient, this.user,weekNum);
            model.addAttribute("classesList", classesList);
            //添加到页面
            model.addAttribute("currentWeek", weekNum);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "class";
    }

    /**
     * 查询考试
     */
    @GetMapping("/query/exam")
    public String toExam(Model model){

        try {
            List<Exam> examList = QueryExam.query(this.httpClient, this.user);
            model.addAttribute("examList", examList);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "exam";
    }

    /**
     * 查询空教室
     */
    @RequestMapping("/query/classroom")
    public String toClassRoom(Model model,String select){

        if(select == null){
            select = "'11'|'1','3','5','7','9','11','0','0','0'";
        }

        //选择查询不同时间段的空
        switch (select){
            case "1":
                select = "'1'|'1','0','0','0','0','0','0','0','0'";
                break;
            case "2":
                select = "'2'|'0','3','0','0','0','0','0','0','0'";
                break;
            case "3":
                select = "'3'|'0','0','5','0','0','0','0','0','0'";
                break;
            case "4":
                select = "'4'|'0','0','0','7','0','0','0','0','0'";
                break;
            case "5":
                select = "'5'|'0','0','0','0','9','0','0','0','0'";
                break;
            case "6":
                select = "'6'|'0','0','0','0','0','11','0','0','0'";
                break;
            case "7":
                select = "'7'|'1','3','0','0','0','0','0','0','0'";
                break;
            case "8":
            select = "'8'|'0','0','5','7','0','0','0','0','0'";
            break;
            case "9":
                select = "'9'|'0','0','0','0','9','11','0','0','0'";
                break;
            case "10":
                select = "'10'|'1','3','5','7','0','0','0','0','0'";
                break;
            default:
                select = "'11'|'1','3','5','7','9','11','0','0','0'";
                break;
        }

        try {
            List<Classroom> classroomList = QueryClassroom.query(this.httpClient, this.user,select);
            model.addAttribute("classroomList", classroomList);

        }catch (Exception e){
            e.printStackTrace();
        }

        return "classroom";
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){

        //清除cookie和session
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0);
        }

        request.getSession().removeAttribute("sessionUser");

        return "login";
    }

}
