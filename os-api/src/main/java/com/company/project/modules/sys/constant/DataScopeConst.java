package com.company.project.modules.sys.constant;

/**
 * 角色的数据权限常量
 */
public interface DataScopeConst {

    /**
     * 全部数据权限
     */
    int ALL_DATA_PERMISSIONS = 1;
    /**
     * 自定数据权限
     */
    int CUSTOM_DATA_PERMISSIONS = 2;
    /**
     * 本部门数据权限
     */
    int DEPARTMENT_DATA_PERMISSIONS = 3;
    /**
     * 部门及一下数据权限
     */
    int DEPARTMENT_AND_BELOW_DATA_PERMISSIONS = 4;
    /**
     * 仅本人数据权限
     */
    int PERSONAL_DATA_PERMISSIONS = 5;
}
