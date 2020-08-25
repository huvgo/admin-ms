package com.company.project.modules.sys.mapper;

import com.company.project.modules.sys.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统用户 Mapper 接口
 * </p>
 *
 * @author root
 * @since 2020-08-12
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
