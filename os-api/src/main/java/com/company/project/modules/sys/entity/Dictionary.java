package com.company.project.modules.sys.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.company.project.modules.common.TreeEntity;
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
public class Dictionary extends TreeEntity<Integer, Dictionary> {

        /**
        * 编码
        */
        private String code;

        /**
        * 名称
        */
        private String name;

        /**
        * 备注
        */
        private String remarks;

        /**
        * 逻辑删除
        */
        private Integer deleted;

}

