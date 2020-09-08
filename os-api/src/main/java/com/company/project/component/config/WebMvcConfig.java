package com.company.project.component.config;

import cn.hutool.extra.servlet.ServletUtil;
import com.company.project.cache.UserCacheUtil;
import com.company.project.core.Result;
import com.company.project.core.Results;
import com.company.project.modules.system.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring MVC 配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);


    private final ObjectMapper objectMapper;


    public WebMvcConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").
                allowedOrigins("*"). //allowedOrigins("https://www.xxx.cn"). //允许跨域的域名，可以用*表示允许任何域名使用
                allowedMethods("*"). //允许任何方法（post、get等）
                allowedHeaders("*"). //允许任何请求头
                allowCredentials(true);//带上cookie信息
    }


    //添加拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加登录状态拦截器
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
                // 判断是否是OPTIONS请求
                boolean isOptionsMethod = ServletUtil.METHOD_OPTIONS.equalsIgnoreCase(request.getMethod());
                if (isOptionsMethod) {
                    return true;
                }
                // 登录状态检查
                boolean isLogin = checkLoginStatus(request, response);
                if (!isLogin) {
                    logger.warn("未获取到登录状态，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), ServletUtil.getClientIP(request), objectMapper.writeValueAsString(request.getParameterMap()));
                }
                return isLogin;
            }
        });
        // 排除请求路径
        interceptorRegistration.excludePathPatterns("/system/user/login");
        interceptorRegistration.excludePathPatterns("/system/user/token");
        interceptorRegistration.excludePathPatterns("/avatar/**");// 静态资源路径
        interceptorRegistration.addPathPatterns("/**");
    }


    private boolean checkLoginStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取用户信息，获取用户拥有的菜单
        String token = request.getHeader("X-Token");
        if (token == null) {
            Result<?> fail = Results.NOT_LOGGED_IN;
            ServletUtil.write(response, objectMapper.writeValueAsString(fail), "application/json;charset=UTF-8");
            return false;
        } else {
            User user = UserCacheUtil.getUser(token);
            if (user == null) {
                Result<?> fail = Results.LOGIN_EXPIRED;
                ServletUtil.write(response, objectMapper.writeValueAsString(fail), "application/json;charset=UTF-8");
                return false;
            }
        }
        return true;
    }


}
