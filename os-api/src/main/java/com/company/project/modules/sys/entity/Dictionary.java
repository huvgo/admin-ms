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
 * @since 2020-08-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class Dictionary extends Entity {

    /**
    * 父节点
    */
    private Long parentId;

    /**
    * 内部编码
    */
    private String innerCode;

    /**
    * 编码
    */
    private String code;

    /**
    * 名称
    */
    private String name;

    /**
    * 层级
    */
    private Integer rank;

    /**
    * 排序号
    */
    private Integer sortNum;

    /**
    * 备注
    */
    private String remarks;

    /**
    * ext1
    */
    private String ext1;

    /**
    * ext2
    */
    private String ext2;

    /**
    * 数据状态 0:有效 1:无效
    */
    private Integer delFlag;

}

