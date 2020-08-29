package com.company.project.modules.dev.mapper;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityHandler;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.handler.NumberHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.company.project.modules.dev.util.Convert;
import com.company.project.util.JDBCUtils;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Component
public class TableMapper {
    public List<Entity> page(Integer offset, Integer limit, Map<String, Object> params) throws SQLException {
        String sql = "select table_name tableName, engine, table_comment tableComment, create_time createTime " +
                "from information_schema.tables " +
                "where table_schema = (select database()) {} order by create_time desc limit {}, {}";
        String condition = "";
        String tableName = (String) params.get("tableName");
        if (StrUtil.isNotBlank(tableName)) {
            condition = "and table_name like concat('%', " + tableName + ", '%')";
        }
        String formatSql = StrUtil.format(sql, condition, offset, limit);
        Connection conn = JDBCUtils.getConnection();
        List<Entity> page = SqlExecutor.query(conn, formatSql, new EntityListHandler());
        DbUtil.close(conn);
        return page;
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

    public List<Entity> queryColumns(String tableName) throws SQLException {
        String sql = "select column_name name, data_type dataType, column_comment comment, column_key primarykey, extra from information_schema.columns where table_name = '" + tableName + "' and table_schema = (select database()) order by ordinal_position";
        Connection conn = JDBCUtils.getConnection();
        List<Entity> columnList = SqlExecutor.query(conn, sql, new EntityListHandler());
        DbUtil.close(conn);

        columnList.forEach(entity -> {
            entity.put("javaType", Convert.jdbcType2JavaType(entity.getStr("dataType")));
            entity.put("name", StrUtil.toCamelCase(entity.getStr("name")));
        });
        return columnList;
    }
}
