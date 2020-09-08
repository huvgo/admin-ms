package com.company.project.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.project.modules.system.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统通知 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2020-09-02
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

}
