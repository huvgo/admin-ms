package com.company.project.modules.process.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.modules.base.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * <p>
 * 请假申请
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-21
 */
@Data
@Accessors(chain = true)
@TableName(value = "process_apply", autoResultMap = true)
public class Apply extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 申请人ID
     */
    private Integer applyUserId;

    /**
     * 扩展内容
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> ext;

    /**
     * 类型
     */
    private Integer type;

}

