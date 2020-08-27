package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.core.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author root
 * @since 2020-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user", autoResultMap = true)
public class User extends Entity {

    private static final long serialVersionUID = 1L;

    /**
     * 真实姓名
     */
    private String name;


    private String introduction;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 关联角色id
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> roleIds;

    /**
     * 盐
     */
    private String salt;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态  0：禁用   1：正常
     */
    private Boolean status;

    /**
     * 部门Id
     */
    private Long deptId;

    /**
     * 创建者ID
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者ID
     */
    private Long modifyUserId;

    /**
     * 更新时间
     */
    private Date modifyTime;

    /**
     * 是否删除
     */
    private Integer delFlag;

    /**
     * 头像
     */
    private String avatar;

    @TableField(exist = false)
    private List<Menu> menuList;

}
