package com.company.project.component.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import com.company.project.cache.UserCacheUtil;
import com.company.project.modules.sys.constant.LogType;
import com.company.project.modules.sys.entity.Log;
import com.company.project.modules.sys.service.impl.LogServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Component
@Aspect
@Slf4j
public class LogAspect {

    private final ObjectMapper objectMapper;
    private final LogServiceImpl logService;

    @Autowired
    public LogAspect(ObjectMapper objectMapper, LogServiceImpl logService){
        this.objectMapper = objectMapper;
        this.logService = logService;
    }


    @Pointcut("@annotation(com.company.project.component.annotation.Log2DB)")
    public void log(){
    }

    /**
     * 环绕通知  在方法的调用前、后执行
     */
    @Around("log()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //开始时间
        long begin = System.currentTimeMillis();
        //方法环绕proceed结果
        Object obj = point.proceed();
        //结束时间
        long end = System.currentTimeMillis();
        //时间差
        long timeDiff = (end - begin);

        Log log = new Log();
        log.setType(LogType.SYSTEM_LOG)
                .setOperator(UserCacheUtil.getCurrentUser().getUsername())
                .setOperatorId(UserCacheUtil.getCurrentUser().getId())
                .setIp(ServletUtil.getClientIP(request));
        HashMap<String, Object> content = new HashMap<>();
        content.put("method", request.getMethod());
        content.put("url", request.getRequestURL().toString());
        content.put("time", timeDiff);
        content.put("params", objectMapper.writeValueAsString(point.getArgs()[0]));
        log.setContent(content);
        logService.save(log);
        return obj;
    }


}