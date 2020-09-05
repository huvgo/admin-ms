package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.company.project.modules.base.entity.BaseTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 部门管理
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_dept", autoResultMap = true)
public class Dept extends BaseTreeEntity<Dept> {


    /**
     * 部门名称
     */
    private String name;

    /**
     * 机构类型
     */
    private Integer type;

    /**
     * 创建者ID
     */
    private Integer createUserId;


    /**
     * 修改者ID
     */
    private Integer updateUserId;


    /**
     * 是否启用
     */
    @TableField("is_enabled")
    private Boolean enabled;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    private Boolean deleted;

}

