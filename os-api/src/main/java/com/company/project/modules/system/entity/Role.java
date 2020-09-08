package com.company.project.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.modules.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 角色权限
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "system_role", autoResultMap = true)
public class Role extends BaseEntity<Integer> {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色与菜单对应关系
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> menuIds;

    /**
     * 自定义数据权限 角色与部门对应关系
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> deptIds;

    /**
     * 数据权限范围
     */
    private Integer dataScope;

    /**
     * 备注
     */
    private String remark;

    /**
     * 部门Id
     */
    private Integer deptId;

    /**
     * 创建者ID
     */
    private Integer createUserId;


}

