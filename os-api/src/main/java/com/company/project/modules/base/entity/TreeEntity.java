package com.company.project.modules.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.company.project.component.mybatis.IntegerArray2SplitTypeHandler;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TreeEntity<E> extends BaseEntity<Integer> {

    /**
     * 上级ID，一级ID为0
     */
    private Integer parentId = 0;

    /**
     * 所有上级ID
     * 子类 设置autoResultMap = true   例如 ：@TableName(value = "sys_dept", autoResultMap = true)
     */
    @TableField(typeHandler = IntegerArray2SplitTypeHandler.class)
    private List<Integer> parentIds;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 附加信息
     */
    @TableField(exist = false)
    private List<E> children;
}
