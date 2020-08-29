package com.company.project.modules.com;


import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * 将逗号分隔的字符串和数组对象之间的进行类型转换的抽象类
 */
@MappedTypes({Object.class})
@MappedJdbcTypes(JdbcType.VARCHAR)
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