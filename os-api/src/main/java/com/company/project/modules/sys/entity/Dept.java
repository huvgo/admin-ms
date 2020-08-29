package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.company.project.modules.com.TreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * 部门管理
 * </p>
 *
 * @author author
 * @since 2020-08-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_dept", autoResultMap = true)
public class Dept extends TreeEntity<Dept> {

    /**
     * 部门名称
     */
    private String name;

    /**
     * 机构类型
     */
    private String type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改者ID
     */
    private Long modifyUserId;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;

}

