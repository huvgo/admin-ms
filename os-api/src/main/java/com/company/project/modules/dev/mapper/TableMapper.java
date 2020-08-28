package com.company.project.modules.dev.mapper;

import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityHandler;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.handler.NumberHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.company.project.util.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TableMapper {
    List<Entity> page(String tableName, Integer offset, Integer limit) throws SQLException {
        String sql = "select table_name tableName, engine, table_comment tableComment, create_time createTime " +
                "from information_schema.tables " +
                "where table_schema = (select database()) {} order by create_time desc limit {}, {}";
        String condition = "";
        if (StrUtil.isNotBlank(tableName)) {
            condition = "and table_name like concat('%', " + tableName + ", '%')";
        }
        String formatSql = StrUtil.format(sql, condition, offset, limit);
        Connection conn = JDBCUtils.getConnection();
        List<Entity> page = SqlExecutor.query(conn, formatSql, new EntityListHandler());
        DbUtil.close(conn);
        return page;
    }

    int total(String tableName) throws SQLException {
        String sql = "select count(*) from information_schema.tables where table_schema = (select database()) {}";
        String condition = "";
        if (StrUtil.isNotBlank(tableName)) {
            condition = "and table_name like concat('%', " + tableName + ", '%')";
        }
        String formatSql = StrUtil.format(sql, condition);
        Connection conn = JDBCUtils.getConnection();
        Number total = SqlExecutor.query(conn, formatSql, new NumberHandler());
        DbUtil.close(conn);
        return total.intValue();
    }

    Entity queryTable(String tableName) throws SQLException {
        String sql = "select table_name tableName, engine, table_comment tableComment, create_time createTime from information_schema.tables where table_schema = (select database()) and table_name = '" + tableName + "'";
        Connection conn = JDBCUtils.getConnection();
        Entity entity = SqlExecutor.query(conn, sql, new EntityHandler());
        DbUtil.close(conn);
        return entity;
    }

    List<Entity> queryColumns(String tableName) throws SQLException {
        String sql = "select column_name columnName, data_type dataType, column_comment columnComment, column_key columnKey, extra from information_schema.columns where table_name = '" + tableName + "' and table_schema = (select database()) order by ordinal_position";
        Connection conn = JDBCUtils.getConnection();
        List<Entity> page = SqlExecutor.query(conn, sql, new EntityListHandler());
        DbUtil.close(conn);
        return page;
    }
}
