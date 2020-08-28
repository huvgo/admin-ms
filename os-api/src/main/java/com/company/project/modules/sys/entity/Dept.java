package com.company.project.modules.sys.entity;

import com.company.project.modules.common.Entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.Date;

/**
 * <p>
 * 部门管理
 * </p>
 *
 * @author author
 * @since 2020-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_dept")
public class Dept extends Entity<Integer> {

        /**
        * 上级部门ID
        */
        private Integer parent_id;

        /**
        * 所有上级部门ID
        */
        private String parent_ids;

        /**
        * 部门名称
        */
        private String name;

        /**
        * 机构类型
        */
        private String type;

        /**
        * 排序
        */
        private Integer sort;

        /**
        * 联系电话
        */
        private String mobile;

        /**
        * 逻辑删除
        */
        private Integer status;

        /**
        * 创建者ID
        */
        private Long create_user_id;

        /**
        * 创建时间
        */
        private Date create_time;

        /**
        * 修改者ID
        */
        private Long modify_user_id;

        /**
        * 修改时间
        */
        private Date modify_time;

        /**
        * 是否删除  1：已删除  0：正常
        */
        private Integer deleted;

}

