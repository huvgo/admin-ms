package com.company.project.modules.process.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.company.project.modules.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 请假申请
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "process_approve")
public class Approve extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 审批人ID
     */
    private Integer approveUserId;

    /**
     * 申请ID
     */
    private Integer applyId;

    /**
     * 扩展内容
     */
    private String ext;

    /**
     * 类型
     */
    private Integer type;

    private Integer handleType;

}


