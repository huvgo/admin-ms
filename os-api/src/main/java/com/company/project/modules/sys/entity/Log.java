package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.company.project.modules.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName(value = "sys_log")
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
     * 请求URL
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 执行时长(毫秒)
     */
    private Long time;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 日志类型
     */
    private Integer type;

}

