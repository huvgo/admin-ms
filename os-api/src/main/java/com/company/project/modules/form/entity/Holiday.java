package com.company.project.modules.form.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.company.project.modules.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 请假申请
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "form_holiday")
public class Holiday extends BaseEntity<Integer> {

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 备注
     */
    private String remark;


}

