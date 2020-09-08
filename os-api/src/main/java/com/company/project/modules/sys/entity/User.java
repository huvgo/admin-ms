package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.modules.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 系统用户
 * </p>
 *
 * @author codeGenerator
 * @since 2020-09-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_user", autoResultMap = true)
public class User extends BaseEntity<Integer> {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * 关联角色id
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> roleIds;

    /**
     * 盐
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String salt;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 真实姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String idNumber;

    /**
     * 个人介绍
     */
    private String introduction;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 部门ID
     */
    private Integer deptId;

    /**
     * 所有上级部门ID
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> deptIds;

    /**
     * 创建者ID
     */
    private Integer createUserId;


    /**
     * 更新者ID
     */
    private Integer updateUserId;


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

    @TableField(exist = false)
    private List<Role> roleList;

    @TableField(exist = false)
    private List<Menu> menuList;

    @JsonIgnore
    public boolean isSuperAdmin() {
        return this.getId().equals(1);
    }
}

