package com.company.project.modules.process.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-18
 */
@Data
@Accessors(chain = true)
@TableName(value = "process_instance", autoResultMap = true)
public class Instance {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 流程定义ID（act_re_procdef表主键）
     */
    private String processDefinitionId;

    /**
     * 申请人ID
     */
    private Integer userId;

    /**
     * 部门ID
     */
    private Integer deptId;

    /**
     * 当前节点审批人ID
     */
    private String currNodeUserId;

    /**
     * 当前节点审批人
     */
    private String currNodeUserName;

    /**
     * 申请内容
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> data;

    /**
     * 流程状态（0已提交；1审批中；2审批
     * 通过；3审批不通过；4撤销）
     */
    private String status;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}

