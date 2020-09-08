package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.modules.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * <p>
 * 系统日志
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_log", autoResultMap = true)
public class Log extends BaseEntity<Integer> {

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作用户ID
     */
    private Integer operatorId;

    /**
     * 日志内容
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> content;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 日志类型
     */
    private Integer type;

}

