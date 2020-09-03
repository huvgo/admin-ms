package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.modules.base.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author root
 * @since 2020-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dictionary", autoResultMap = true)
public class Dictionary extends BaseEntity<Integer> {

    /**
     * 上级ID，一级ID为0
     */
    private Integer parentId = 0;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 选项
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Option> options;

    /**
     * 逻辑删除
     */
    private Integer deleted;

}



