package com.company.project.modules.com;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将逗号分隔的字符串和数组对象之间的进行类型转换的抽象类
 */
public abstract class StringTokenizerTypeHandler<T> extends BaseTypeHandler<List<T>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<T> ts, JdbcType jdbcType) throws SQLException {
        if (CollUtil.isNotEmpty(ts)) {
            StringBuilder result = new StringBuilder();
            for (T value : ts) {
                result.append(value).append(",");
            }
            result.deleteCharAt(result.length() - 1);
            ps.setString(i, result.toString());
        } else {
            ps.setString(i, "");
        }
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        return toArray(resultSet.getString(columnName));
    }

    @Override
    public List<T> getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        return toArray(resultSet.getString(columnIndex));
    }

    @Override
    public List<T> getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        return toArray(callableStatement.getString(columnIndex));
    }

    private List<T> toArray(String columnValue) {
        if (StrUtil.isBlank(columnValue)) {
            return createArray(0);
        }
        String[] values = columnValue.split(",");
        List<T> array = createArray(values.length);
        for (String value : values) {
            array.add(parseString(value));
        }
        return array;
    }

    private List<T> createArray(int size) {
        return new ArrayList<>(size);
    }

    abstract T parseString(String value);

}
