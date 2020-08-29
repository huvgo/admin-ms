package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.modules.com.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * <p>
 * 菜单管理
 * </p>
 *
 * @author root
 * @since 2020-08-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_menu", autoResultMap = true)
public class Menu extends TreeEntity<Menu> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String path;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Meta meta;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    private Integer type;


    /**
     * 附加信息
     */
    @TableField(exist = false)
    private Map<String, Object> remark;
}
