package com.company.project.component.config;

import com.company.project.component.interceptor.SpringMVCInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public WebMvcConfig(SpringMVCInterceptor springMVCInterceptor) {
        this.springMVCInterceptor = springMVCInterceptor;
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

    private final SpringMVCInterceptor springMVCInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(springMVCInterceptor);
    }
}
