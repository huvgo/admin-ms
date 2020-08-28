package com.company.project.util;

import com.company.project.core.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * JDBC 工具类
 */
public class JDBCUtils {
    private static String url;
    private static String user;
    private static String password;

    /*
      文件读取，只会执行一次，使用静态代码块
     */
    static {
        //读取文件，获取值
        try {
            //1.创建Properties集合类
            Properties pro = new Properties();
            //获取src路径下的文件--->ClassLoader类加载器
            ClassLoader classLoader = JDBCUtils.class.getClassLoader();
            InputStream resource = classLoader.getResourceAsStream("application.properties");
            Assert.requireNonNull(resource, "application.properties配置文件不存在，需要在resources目录下放配置文件");
            //2.加载文件
            pro.load(resource);
            //3获取数据
            url = pro.getProperty("spring.datasource.url");
            user = pro.getProperty("spring.datasource.username");
            password = pro.getProperty("spring.datasource.password");
            String driver = pro.getProperty("spring.datasource.driver-class-name");
            //4.注册驱动
            Class.forName(driver);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     *
     * @return 连接对象
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * 释放资源
     */
    public static void close(ResultSet resultSet, Statement statement, Connection conn) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
