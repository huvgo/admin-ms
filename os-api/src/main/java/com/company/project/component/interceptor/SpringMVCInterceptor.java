package com.company.project.component.interceptor;

import com.company.project.core.Result;
import com.company.project.core.ResultCode;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.CacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SpringMVCInterceptor extends HandlerInterceptorAdapter {

    private final CacheService cacheService;

    private final ObjectMapper objectMapper;

    public SpringMVCInterceptor(CacheService cacheService, ObjectMapper objectMapper) {
        this.cacheService = cacheService;
        this.objectMapper = objectMapper;
    }

    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 如果返回true
     * 执行下一个拦截器,直到所有的拦截器都执行完毕
     * 再执行被拦截的Controller
     * 然后进入拦截器链,
     * 从最后一个拦截器往回执行所有的postHandle()
     * 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        System.out.println("requestURI = " + requestURI);
        if ("/dev-api/sys/user/login".equals(requestURI)) {
            return true;
        }
        if ("/dev-api/sys/user/token".equals(requestURI)) {
            return true;
        }
        // 获取用户信息，获取用户拥有的菜单
        String token = request.getHeader("X-Token");
        System.out.println("token = " + token);
        Result<Object> result;
        if (token == null) {
            result = Result.fail(ResultCode.LOGIN_EXPIRED, "请先登录");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-type", "application/json;charset=UTF-8");
            response.setStatus(200);
            response.getWriter().println(objectMapper.writeValueAsString(result));
            return false;
        } else {
            User user = cacheService.getUser(token);
            if (user == null) {
                result = Result.fail(ResultCode.LOGIN_EXPIRED, "登录过期,请重新登陆");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.setStatus(200);
                response.getWriter().println(objectMapper.writeValueAsString(result));
                return false;
            }
        }


        return true;
    }
}
