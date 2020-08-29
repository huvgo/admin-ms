package com.company.project.modules.sys.service;

import com.company.project.modules.sys.entity.Dictionary;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author root
 * @since 2020-08-28
 */
public interface DictionaryService extends IService<Dictionary> {

    Dictionary getByCode(String code);
}
