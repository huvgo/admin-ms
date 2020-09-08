package com.company.project.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.modules.system.entity.UserNotice;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户通知 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2020-09-04
 */
@Mapper
public interface UserNoticeMapper extends BaseMapper<UserNotice> {

}
