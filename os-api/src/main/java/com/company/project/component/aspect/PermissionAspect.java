package com.company.project.component.aspect;

import com.company.project.cache.UserCache;
import com.company.project.core.Assert;
import com.company.project.core.Results;
import com.company.project.modules.system.entity.Menu;
import com.company.project.modules.system.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * 权限判断 拦截器
 * 拦截所有标有注解@Permissions的方法
 * 将请求的方法名与system_menu表中的perms字段比较
 */
@Profile({"dev", "test", "prod"})
@Component
@Aspect
@Slf4j
public class PermissionAspect {

    @Autowired
    private UserCache userCache;


    @Pointcut("@annotation(com.company.project.component.annotation.RequirePermission)")
    public void permissions() {
    }

    /**
     * 拦截方法之前的一段业务逻辑
     */
    @Before("permissions()")
    public void doBefore(JoinPoint joinPoint) {
        // 获取用户信息，获取用户拥有的菜单
        HttpServletRequest request = Objects.requireNonNull((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("X-Token");
        User user = userCache.getUser(token);
        Assert.requireNonNull(user, Results.LOGIN_EXPIRED);
        Set<Menu> menuList = user.getMenus();

        // 获取当前访问的菜单和当前访问菜单的权限
        Class<?> classTarget = joinPoint.getTarget().getClass();
        RequestMapping[] annotations = classTarget.getAnnotationsByType(RequestMapping.class);
        String[] value = annotations[0].value();
        // 请求菜单路径
        String requestPath = value[0];
        // 请求方法，约定方法名和权限名一样
        String methodPerms = joinPoint.getSignature().getName();

        // 判断用户是否具有该菜单
        Optional<Menu> menuOptional = menuList.stream().filter(menu -> requestPath.equals(menu.getPath()) || ("/" + requestPath).equals(menu.getPath())).findFirst();
        Assert.requireTrue(menuOptional.isPresent(), Results.UNAUTHORIZED);
        Menu requestMenu = menuOptional.get();

        // 判断用户是否具有该权限
        boolean hasPermission = false;
        String perms = requestMenu.getPerms();
        if (methodPerms.equals(perms)) {
            hasPermission = true;
        } else {
            for (Menu menu : menuList) {
                if (requestMenu.getId().equals(menu.getParentId())) {
                    String childPerms = menu.getPerms();
                    if (methodPerms.equals(childPerms)) {
                        hasPermission = true;
                        break;
                    }
                }
            }
        }

        Assert.requireTrue(hasPermission, Results.UNAUTHORIZED);
    }


}
