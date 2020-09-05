package com.company.project.modules.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class BaseEntity<T> {
    @TableId(type= IdType.AUTO)
    private T id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 附加信息
     */
    @TableField(exist = false)
    private Map<String, Object> other;
}
