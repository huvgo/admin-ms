package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.modules.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.company.project.modules.base.entity.BaseTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 菜单管理
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_menu", autoResultMap = true)
public class Menu extends BaseTreeEntity<Menu> {


    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单URL
     */
    private String path;

    /**
     *
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Meta meta;

    /**
     * 授权
     */
    private String perms;

    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    private Integer type;


}

