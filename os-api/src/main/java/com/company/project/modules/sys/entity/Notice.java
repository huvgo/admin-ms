package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.company.project.modules.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 系统通知
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_notice")
public class Notice extends BaseEntity<Integer> {

    /**
     * 发送人
     */
    private String sender;

    /**
     * 发送人ID
     */
    private Integer senderId;

    /**
     * 发送人头像
     */
    private String senderAvatar;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 推送日期时间
     */
    private Date pushTime;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 是否启用
     */
    @TableField("is_enabled")
    private Boolean enabled;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    private Boolean deleted;

}

