package com.company.project.modules.process.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

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
     * 流程ID
     */
    private String processId;

    /**
     * 流程定义ID（act_re_procdef表主键）
     */
    private String processDefinitionId;

    /**
     * 申请ID
     */
    private Integer applyId;

    /**
     * 当前节点审批人ID
     */
    private String currNodeUserId;

    /**
     * 流程状态（0已提交；1审批中；2审批
     * 通过；3审批不通过；4撤销）
     */
    private Integer status;

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

    /**
     * 申请内容
     */
    @TableField(exist = false)
    private Apply apply;
}

