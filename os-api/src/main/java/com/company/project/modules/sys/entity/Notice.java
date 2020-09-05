package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.company.project.modules.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 系统通知
 * </p>
 *
 * @author author
 * @since 2020-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_notice")
public class Notice extends BaseEntity {

    /**
     * 发送人ID
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
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 状态
     */
    private Boolean status;

}

