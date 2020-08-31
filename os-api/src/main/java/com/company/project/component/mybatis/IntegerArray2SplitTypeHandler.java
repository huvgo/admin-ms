package com.company.project.component.mybatis;


import cn.hutool.core.util.StrUtil;

/**
 * 将逗号分隔的字符串和数组对象之间的进行类型转换
 */
public class IntegerArray2SplitTypeHandler extends StringTokenizerTypeHandler<Object> {
    public IntegerArray2SplitTypeHandler(Class<?> type) {

    }

    @Override
    Integer parseString(String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        return Integer.parseInt(value);
    }
}