package com.company.project.modules.sys.mapper;

import com.company.project.modules.sys.entity.Log;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
/**
 * <p>
 * 系统日志 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2020-09-03
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {

}
