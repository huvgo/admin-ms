package com.company.project.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.modules.system.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author root
 * @since 2020-08-11
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

}
