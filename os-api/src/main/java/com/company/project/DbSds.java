package com.company.project;

import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.company.project.util.FreemarkerTemplateEngine;
import com.company.project.util.JDBCUtils;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

public class DbSds {
    public static void main(String[] args) throws Exception {
        Connection conn = JDBCUtils.getConnection();
        /* 执行查询语句，返回实体列表，一个Entity对象表示一行的数据，Entity对象是一个继承自HashMap的对象，存储的key为字段名，value为字段值 */
        List<Entity> entityList = SqlExecutor.query(conn, "show full fields from sys_menu", new EntityListHandler());
        FreemarkerTemplateEngine freemarkerTemplateEngine = new FreemarkerTemplateEngine();
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("table", entityList);
        freemarkerTemplateEngine.writer(objectObjectHashMap, "", "");
    }
}
