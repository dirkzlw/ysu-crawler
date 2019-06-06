package com.zlw.ysu.crawler.controller;

import com.zlw.ysu.crawler.pojo.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Ranger
 * @create 2019-05-15 9:06
 */
@Controller
public class MainController {

    /**
     * 跳转到登录页面
     * @return
     */
    @GetMapping("/")
    public String toLogin(HttpServletRequest request){

        //从session中获取author，判断是否登录
        User user = (User) request.getSession().getAttribute("sessionUser");

        if(user!=null){
            return "redirect:/index";
        }

        return "login";
    }

    /**
     * 跳转错误页面
     * @return
     */
    @RequestMapping("/error/page")
    public String toErrorPage(){
        return "error";
    }
}
