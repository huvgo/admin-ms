package com.company.project.component.aspect;

import cn.hutool.core.util.StrUtil;
import com.company.project.cache.UserCacheUtil;
import com.company.project.component.annotation.DataScope;
import com.company.project.modules.system.constant.DataScopeConst;
import com.company.project.modules.system.entity.Role;
import com.company.project.modules.system.entity.User;
import com.company.project.modules.system.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 数据过滤处理
 *
 * @author ruoyi
 */
@Slf4j
@Aspect
@Component
public class DataScopeAspect {

    private final DeptService deptService;

    public DataScopeAspect(DeptService deptService) {
        this.deptService = deptService;
    }

    // 配置织入点
    @Pointcut("@annotation(com.company.project.component.annotation.DataScope)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint) {
        // 获得注解
        DataScope controllerDataScope = getAnnotationLog(joinPoint);
        if (controllerDataScope == null) {
            return;
        }
        // 获取当前的用户
        User currentUser = UserCacheUtil.getCurrentUser();
        if (currentUser != null) {
            // 如果是超级管理员，则不过滤数据
            if (!currentUser.isSuperAdmin()) {
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias());
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param user      用户
     * @param userAlias 别名
     */
    public void dataScopeFilter(JoinPoint joinPoint, User user, String deptAlias, String userAlias) {
        StringBuilder sqlString = new StringBuilder();

        for (Role role : user.getRoleList()) {
            Integer dataScope = role.getDataScope();
            if (DataScopeConst.ALL_DATA_PERMISSIONS == dataScope) {
                sqlString = new StringBuilder();
                break;
            } else if (DataScopeConst.CUSTOM_DATA_PERMISSIONS == dataScope) {
                List<Integer> deptIdList = role.getDeptIds();
                String deptIdsStr = StrUtil.join(",", deptIdList);
                sqlString.append(StrUtil.format(" OR {}.dept_id IN ( {} ) ", deptAlias, deptIdsStr));
            } else if (DataScopeConst.DEPARTMENT_DATA_PERMISSIONS == dataScope) {
                sqlString.append(StrUtil.format(" OR {}.dept_id = {} ", deptAlias, user.getDeptId()));
            } else if (DataScopeConst.DEPARTMENT_AND_BELOW_DATA_PERMISSIONS == dataScope) {
                List<Integer> childrenIds = deptService.listChildrenIdById(user.getDeptId());
                String deptIdsStr = StrUtil.join(",", user.getDeptId(), childrenIds);
                sqlString.append(StrUtil.format(
                        " OR {}.dept_id IN ( {} )",
                        deptAlias, deptIdsStr));
            } else if (DataScopeConst.PERSONAL_DATA_PERMISSIONS == dataScope) {
                if (StrUtil.isNotBlank(userAlias)) {
                    sqlString.append(StrUtil.format(" OR {}.create_user_id = {} ", userAlias, user.getId()));
                } else {
                    // 数据权限为仅本人且没有userAlias别名不查询任何数据
                    sqlString.append(" OR 1=0 ");
                }
            }
        }
        log.info(sqlString.toString());
      /*  if(StrUtil.isNotBlank(sqlString.toString())){
            Object params = joinPoint.getArgs()[0];
            if(StrUtil.isNotNull(params) && params instanceof BaseEntity){
                BaseEntity baseEntity = (BaseEntity) params;
                baseEntity.getParams().put(DATA_SCOPE, " AND (" + sqlString.substring(4) + ")");
            }
        }*/
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DataScope getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(DataScope.class);
        }
        return null;
    }
}