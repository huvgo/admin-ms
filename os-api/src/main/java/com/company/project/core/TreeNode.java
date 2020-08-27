package com.company.project.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class TreeNode<T> {
    @TableId(type= IdType.AUTO)
    private Integer id;


    /**
     * 父菜单ID，一级菜单为0
     */
    private Integer parentId;


    /**
     * 附加信息
     */
    @TableField(exist = false)
    private List<T> children;
}
