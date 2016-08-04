package com.database.util;


import com.common.util.PropertiesUtil;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * Created by Administrator on 2016/7/28.
 */
public class DBUtil {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Properties properties = PropertiesUtil.load("jdbc.properties", "/properties/database/jdbc.properties");
            Class.forName(properties.getProperty("driver.class"));
            connection = DriverManager.getConnection(properties.getProperty("url"), properties);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet)  {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
