package com.company.project.modules.audit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.modules.base.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
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
public class Instance extends BaseEntity {

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

    private Integer userId;

    private Integer deptId;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> data;

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

    @TableField(exist = false)
    private List<TaskInstance> taskInstanceList;

}

