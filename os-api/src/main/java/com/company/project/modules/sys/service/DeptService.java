package com.company.project.modules.sys.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.company.project.modules.base.entity.BaseEntity;
import com.company.project.modules.sys.entity.Dept;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门管理 服务类
 * </p>
 *
 * @author author
 * @since 2020-08-29
 */
public interface DeptService extends IService<Dept> {

    default List<Integer> listChildrenIdById(Integer parentId){

        QueryWrapper<Dept> queryWrapper = new QueryWrapper<Dept>()
                .apply("JSON_CONTAINS(parent_ids,{0})", String.valueOf(parentId));
        List<Dept> deptList = this.list(queryWrapper);
        if(CollUtil.isNotEmpty(deptList)){
            return deptList.stream().map(BaseEntity::getId).collect(Collectors.toList());
        }else{
            return new ArrayList<>();
        }
    }
}
