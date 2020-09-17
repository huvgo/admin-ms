package com.company.project.component.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
//    MethodSecurityConfig
    private final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    private final ObjectMapper objectMapper;

    public WebMvcConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // 解决跨域问题
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 表示允许那些原始域进行跨域访问，这里"*"表示允许任意网站，实际开发建议修改为配置项。例如 allowedOrigins("https://www.xxx.cn")
                .allowedOrigins("*")
                // 表示是否允许客户端发送Cookie等凭证信息，这里"true"表示支持发送，涉及登陆此处必须开启。
                .allowCredentials(true)
                // 表示允许原始域发起哪些请求方式，这里"*"表示支持GET/POST等全部提交方式。
                .allowedMethods("*")
                // 表示允许原始域携带哪些请求头 这里"*"表示支持全部请求头
                .allowedHeaders("*")
                // 表示允许暴露哪些响应头，这里特指那些非简单的头部信息，所以用"*"无效。
                .exposedHeaders(HttpHeaders.AUTHORIZATION);
    }

    // 添加拦截器
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 该拦截器为了判断用户是否登录
//        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new HandlerInterceptorAdapter() {
//            @Override
//            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//                    throws IOException {
//                // 判断是否是OPTIONS请求
//                boolean isOptionsMethod = ServletUtil.METHOD_OPTIONS.equalsIgnoreCase(request.getMethod());
//                if (isOptionsMethod) {
//                    return true;
//                }
//
//                // 获取token
//                String token = request.getHeader("X-Token");
//                if (token == null) {
//                    Result<?> fail = Results.NOT_LOGGED_IN;
//                    ServletUtil.write(response, objectMapper.writeValueAsString(fail), "application/json;charset=UTF-8");
//                    return false;
//                } else {
//                    User user = UserCacheUtil.getUser(token);
//                    if (user == null) {
//                        Result<?> fail = Results.LOGIN_EXPIRED;
//                        ServletUtil.write(response, objectMapper.writeValueAsString(fail), "application/json;charset=UTF-8");
//                        return false;
//                    }
//                }
//                return true;
//
//            }
//        });
//        // 排除请求路径
//        interceptorRegistration.excludePathPatterns("/system/user/login");
//        interceptorRegistration.excludePathPatterns("/system/user/token");
//        interceptorRegistration.excludePathPatterns("/avatar/**");// 静态资源路径
//        interceptorRegistration.addPathPatterns("/**");
//    }


    /**
     * 跨域配置
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 设置访问源地址
        config.addAllowedOrigin("*");
        // 设置访问源请求头
        config.addAllowedHeader("*");
        // 设置访问源请求方法
        config.addAllowedMethod("*");
        // 对接口配置跨域设置
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
