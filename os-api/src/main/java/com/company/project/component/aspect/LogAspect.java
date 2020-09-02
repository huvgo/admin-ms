package com.company.project.component.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import com.company.project.modules.sys.entity.Log;
import com.company.project.modules.sys.service.LogService;
import com.company.project.util.AdminOSUtil;
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
import java.util.Date;

@Component
@Aspect
@Slf4j
public class LogAspect {

    private final ObjectMapper objectMapper;
    private final LogService logService;

    @Autowired
    public LogAspect(ObjectMapper objectMapper, LogService logService){
        this.objectMapper = objectMapper;
        this.logService = logService;
    }


    @Pointcut("@annotation(com.company.project.component.annotation.Log2Db)")
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
        log.setIp(ServletUtil.getClientIP(request));
        log.setMethod(request.getMethod());
        log.setParams(objectMapper.writeValueAsString(point.getArgs()));
        log.setOperator(AdminOSUtil.getCurrentUser().getUsername());
        log.setOperatorId(AdminOSUtil.getCurrentUser().getId());
        log.setOperation(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
        log.setTime(timeDiff);
        log.setCreateDate(new Date());
        logService.save(log);
        return obj;
    }


}