package com.company.project.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.modules.base.entity.BaseEntity;
import com.company.project.modules.system.entity.bo.Option;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "system_dictionary", autoResultMap = true)
public class Dictionary extends BaseEntity<Integer> {

    /**
     * 上级节点ID
     */
    private Integer parentId;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 选项
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Option> options;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;


    /**
     * 是否删除
     */
    @TableField("is_deleted")
    private Boolean deleted;

}

