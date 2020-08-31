package com.company.project.core.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Map;

@Data
public class BaseEntity<T> {
    @TableId(type= IdType.AUTO)
    private T id;

    /**
     * 附加信息
     */
    @TableField(exist = false)
    private Map<String, Object> other;
}
