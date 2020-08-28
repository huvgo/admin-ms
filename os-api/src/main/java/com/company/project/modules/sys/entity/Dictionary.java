package com.company.project.modules.sys.entity;

import com.company.project.core.Entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;



/**
 * <p>
 * 数据字典
 * </p>
 *
 * @author root
 * @since 2020-08-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName(value = "sys_dictionary")
public class Dictionary extends Entity<Integer> {

        /**
        * 上级节点ID
        */
        private Integer parentId;

        /**
        * 编码
        */
        private String code;

        /**
        * 名称
        */
        private String name;

        /**
        * 排序
        */
        private Integer sort;

        /**
        * 备注
        */
        private String remarks;

        /**
        * 逻辑删除
        */
        private Integer deleted;

}
