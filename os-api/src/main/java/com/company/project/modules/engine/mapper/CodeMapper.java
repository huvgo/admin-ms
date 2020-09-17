package com.company.project.modules.engine.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.BeanListHandler;
import cn.hutool.db.handler.EntityHandler;
import cn.hutool.db.handler.NumberHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.company.project.modules.engine.entity.code.Column;
import com.company.project.modules.engine.entity.code.Table;
import com.company.project.modules.engine.util.CodeUtils;
import com.company.project.modules.engine.util.JDBCUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CodeMapper {
    public List<Table> page(Integer offset, Integer limit, Map<String, Object> params) throws SQLException {
        String sql = "select table_name name, engine, table_comment comment, create_time createTime " +
                "from information_schema.tables " +
                "where table_schema = (select database()) {} order by create_time desc limit {}, {}";
        String condition = "";
        String tableName = (String) params.get("tableName");
        if (StrUtil.isNotBlank(tableName)) {
            condition = "and table_name like \"%" + tableName + "%\"";
        }
        String formatSql = StrUtil.format(sql, condition, offset, limit);
        log.info(formatSql);
        Connection conn = JDBCUtils.getConnection();
        List<Table> tableList = SqlExecutor.query(conn, formatSql, new BeanListHandler<>(Table.class));
        DbUtil.close(conn);
        if (CollUtil.isNotEmpty(tableList)) {
            for (Table table : tableList) {
                table.setColumns(this.queryColumns(table.getName()));
            }
        }
        return tableList;
    }

    public int total(Map<String, Object> params) throws SQLException {
        String sql = "select count(*) from information_schema.tables where table_schema = (select database()) {}";
        String condition = "";
        String tableName = (String) params.get("tableName");
        if (StrUtil.isNotBlank(tableName)) {
            condition = "and table_name like concat('%', " + tableName + ", '%')";
        }
        String formatSql = StrUtil.format(sql, condition);
        Connection conn = JDBCUtils.getConnection();
        Number total = SqlExecutor.query(conn, formatSql, new NumberHandler());
        DbUtil.close(conn);
        return total.intValue();
    }

    public Entity queryTable(String tableName) throws SQLException {
        String sql = "select table_name name, engine, table_comment comment, create_time createTime from information_schema.tables where table_schema = (select database()) and table_name = '" + tableName + "'";
        Connection conn = JDBCUtils.getConnection();
        Entity entity = SqlExecutor.query(conn, sql, new EntityHandler());
        DbUtil.close(conn);
        return entity;
    }

    public List<Column> queryColumns(String tableName) throws SQLException {
        String sql = "select column_name columnName, data_type dataType, column_comment comment, column_key primarykey, extra from information_schema.columns where table_name = '" + tableName + "' and table_schema = (select database()) order by ordinal_position";
        Connection conn = JDBCUtils.getConnection();
        List<Column> columnList = SqlExecutor.query(conn, sql, new BeanListHandler<>(Column.class));
        DbUtil.close(conn);

        columnList.forEach(entity -> {
            String javaType = CodeUtils.jdbcType2JavaType(entity.getDataType());
            if ("date".equalsIgnoreCase(javaType)) {
                entity.setElement("3");
            }
            if ("boolean".equalsIgnoreCase(javaType)) {
                entity.setElement("4");
            }
            entity.setJavaType(javaType);
            entity.setName(StrUtil.toCamelCase(entity.getColumnName()));
        });
        return columnList;
    }
}
