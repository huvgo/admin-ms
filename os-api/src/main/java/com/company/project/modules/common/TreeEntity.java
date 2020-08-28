package com.company.project.modules.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class TreeEntity<E, T> {

    @TableId(type = IdType.AUTO)
    private E id;


    /**
     * 父菜单ID，一级菜单为0
     */
    private E parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 附加信息
     */
    @TableField(exist = false)
    private List<T> children;
}
