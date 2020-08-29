package com.company.project.modules.sys.service.impl;

import com.company.project.modules.sys.entity.Dept;
import com.company.project.modules.sys.mapper.DeptMapper;
import com.company.project.modules.sys.service.DeptService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2020-08-29
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

}
