package com.zlw.ysu.crawler.config;

import com.zlw.ysu.crawler.interceptor.ErrorInterceptor;
import com.zlw.ysu.crawler.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器配置类
 *
 * @author Ranger
 * @create 2019-05-20 9:33
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    /**
     * 自定义拦截器，添加拦截路径和排除拦截路径
     * addPathPatterns():添加需要拦截的路径
     * excludePathPatterns():添加不需要拦截的路径
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //对非注册用户进行拦截
        UserInterceptor userRoot = new UserInterceptor();
        //对普通用户实现编辑博客，点赞，留言功能实现留言
        //,"/author/message"    先对未登录用户进行留言不拦截
        registry.addInterceptor(userRoot)
                .addPathPatterns("/index", "/query/grade", "/query/classes", "/query/exam", "/query/classroom");                                               //AdminController
        //对跳转错误页面进行拦截
        ErrorInterceptor errorRoot = new ErrorInterceptor();
        registry.addInterceptor(errorRoot);
        super.addInterceptors(registry);

    }

}
