package com.company.project.modules.process.entity;

import com.company.project.modules.base.entity.BaseEntity;
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
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "process_instance")
public class Instance extends BaseEntity<Integer> {

    /**
     * 流程实例ID
     */
    private String processId;

    /**
     * 流程标识
     */
    private String processKey;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程定义ID
     */
    private String processDefinitionId;

    /**
     * 流程状态（0已提交；1审批中；2审批
     * 通过；3审批不通过；4撤销）
     */
    private String processState;

    /**
     * 申请人ID
     */
    private String userId;

    /**
     * 申请人
     */
    private String username;

    /**
     * 申请时间
     */
    private Date procApplyTime;

    /**
     * 当前节点审批人ID
     */
    private String procCurrNodeUserId;

    /**
     * 当前节点审批人
     */
    private String procCurrNodeUserName;

    /**
     * 结束流程时间
     */
    private Date procEndTime;

    /**
     *
     */
    private String procData;

    /**
     *
     */
    private String departmentId;

    /**
     *
     */
    private String departmentName;

    /**
     *
     */
    private Date timeOfEntry;

}

