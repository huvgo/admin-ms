package com.company.project.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.modules.system.entity.Dept;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 部门管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2020-08-29
 */
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

}
