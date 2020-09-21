package com.company.project.modules.process.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-19
 */
@Data
@Accessors(chain = true)
@TableName(value = "process_task_instance")
public class TaskInstance {

    /**
     * 流程实例ID
     */
    private String instanceId;

    /**
     * 流程ID
     */
    private String processId;

    /**
     * 任务实例ID
     */
    private String taskId;

    /**
     * 任务节点key
     */
    private String taskKey;

    /**
     * 任务节点
     */
    private String taskName;

    /**
     * 应审批用户ID
     */
    private String shouldUserId;

    /**
     * 应审批用户
     */
    private String shouldUserName;

    /**
     * 实际处理用户ID
     */
    private String handleUserId;

    /**
     * 实际处理用户
     */
    private String handleUserName;

    /**
     * 处理时间
     */
    private Date handleTime;

    /**
     * 处理意见
     */
    private String handleOpinion;

    /**
     * 处理类型（2审批通过；3审批不通过；4
     * 撤销）
     */
    private String handleType;

}

