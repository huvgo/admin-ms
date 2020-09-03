package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.company.project.core.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 系统日志
 * </p>
 *
 * @author author
 * @since 2020-09-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_log")
public class Log extends BaseEntity {

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
        * 创建时间
        */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date createDate;

}

