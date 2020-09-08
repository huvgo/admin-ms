package com.company.project.modules.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.modules.sys.entity.Dictionary;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author root
 * @since 2020-08-28
 */
@Mapper
public interface DictionaryMapper extends BaseMapper<Dictionary> {

}
