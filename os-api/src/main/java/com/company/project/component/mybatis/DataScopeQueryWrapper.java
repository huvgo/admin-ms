package com.company.project.component.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.company.project.cache.UserCacheUtil;
import com.company.project.core.Assert;
import com.company.project.core.Results;
import com.company.project.modules.sys.constant.DataScopeConst;
import com.company.project.modules.sys.entity.Role;
import com.company.project.modules.sys.entity.User;
import com.company.project.modules.sys.service.DeptService;
import com.company.project.modules.sys.service.RoleService;

import java.util.List;

public class DataScopeQueryWrapper<T> extends QueryWrapper<T> {


    private final DeptService deptService;
    private final RoleService roleService;

    public DataScopeQueryWrapper(DeptService deptService, RoleService roleService){
        super(null);
        this.deptService = deptService;
        this.roleService = roleService;
        addScope(this.typedThis);
    }

    private void addScope(QueryWrapper<T> queryWrapper){
        User currentUser = UserCacheUtil.getCurrentUser();
        Assert.requireNonNull(currentUser, Results.LOGIN_EXPIRED);
        // 如果是超级管理员，则不过滤数据
        if(currentUser.isSuperAdmin()){
            return;
        }
        List<Integer> roleIds = currentUser.getRoleIds();
        List<Role> roleList = roleService.listByIds(roleIds);
        queryWrapper.and(c -> {
            for(Role role : roleList){
                Integer dataScope = role.getDataScope();
                if(DataScopeConst.ALL_DATA_PERMISSIONS == dataScope){
                    c.clear();
                    c.apply("1 = 1");
                    break;
                } else if(DataScopeConst.CUSTOM_DATA_PERMISSIONS == dataScope){
                    List<Integer> deptIds = role.getDeptIds();
                    c.or(q -> q.in("dept_id", deptIds));
                } else if(DataScopeConst.DEPARTMENT_DATA_PERMISSIONS == dataScope){
                    c.or(q -> q.eq("dept_id", currentUser.getDeptId()));
                } else if(DataScopeConst.DEPARTMENT_AND_BELOW_DATA_PERMISSIONS == dataScope){
                    List<Integer> deptIds = deptService.listChildrenIdById(currentUser.getDeptId());
                    deptIds.add(currentUser.getDeptId());
                    c.or(q -> q.in("dept_id", deptIds));
                } else if(DataScopeConst.PERSONAL_DATA_PERMISSIONS == dataScope){
                    c.or(q -> q.eq("create_user_id", currentUser.getId()));
                }
            }
        });

    }
}
