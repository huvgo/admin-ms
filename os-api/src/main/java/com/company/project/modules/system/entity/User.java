package com.company.project.modules.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.company.project.modules.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
@TableName(value = "system_user", autoResultMap = true)
public class User extends BaseEntity<Integer> implements UserDetails {

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 关联角色id
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    @NotEmpty(message = "角色不能为空")
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
    @Getter(value = AccessLevel.NONE)
    private Boolean enabled;

    /**
     * 是否删除
     */
    @TableField("is_deleted")
    private Boolean deleted;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private Set<Menu> menus;

    @JsonIgnore
    public boolean isSuperAdmin() {
        return 1 == this.getId();
    }

    /**
     * 返回用户的所有角色
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
//        for (Role role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role.getRemark()));
//        }
//        return authorities;
        return null;
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

