package com.company.project.common.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Profile({"dev", "test", "prod"})
@Component
@Aspect
@Slf4j
public class AuthorityVerificationAspect {

    /**
     * 定义一个公共的方法，实现切入点
     * 拦截Controller下面的所有方法  任何参数(..表示拦截任何参数)
     * 以@RestController注解作为切入点  可切入其他业务模块的方法
     * <p>
     * // @within和@target 针对类的注解,
     * // @annotation 是针对方法的注解，为自定义注解
     */
    /*    @Pointcut("execution(public * com.*.controller..*.*(..))")*/
    @Pointcut("@annotation(com.company.project.common.annotation.RequiresPermissions)")
    public void cut() {
    }

    /**
     * 拦截方法之前的一段业务逻辑
     */
    @Before("cut()")
    public void doBefore(JoinPoint joinPoint) throws JsonProcessingException {
        Class<?> classTarget = joinPoint.getTarget().getClass();
        RequestMapping[] annotationsByType = classTarget.getAnnotationsByType(RequestMapping.class);
        String[] value = annotationsByType[0].value();
        String methodName = joinPoint.getSignature().getName();
        System.out.println("name = " + methodName);
        //---------------------/sys/menu
        log.info(value[0]);
    }



}
