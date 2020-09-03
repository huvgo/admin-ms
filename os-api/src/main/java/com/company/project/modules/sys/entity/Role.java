package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.modules.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author root
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_role", autoResultMap = true)
public class Role extends BaseEntity<Integer> {


    /**
     * 角色名称
     */
    private String name;

    /**
     * 关联菜单id
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> menuIds;

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
    private Long deptId;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

}
