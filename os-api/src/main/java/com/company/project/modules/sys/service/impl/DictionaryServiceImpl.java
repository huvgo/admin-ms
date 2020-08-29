package com.company.project.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.project.modules.sys.entity.Dictionary;
import com.company.project.modules.sys.mapper.DictionaryMapper;
import com.company.project.modules.sys.service.DictionaryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author root
 * @since 2020-08-28
 */
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, Dictionary> implements DictionaryService {

    @Override
    public Dictionary getByCode(String code) {
        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        return this.getOne(queryWrapper);
    }


}
