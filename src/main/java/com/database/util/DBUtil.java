package com.database.util;


import com.code.Configuration;
import com.common.util.PropertiesUtil;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/7/28.
 */
public class DBUtil {
    private static Logger log = Logger.getLogger(DBUtil.class.getName());

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Properties properties = PropertiesUtil.load(Configuration.get("jdbc"));
            Class.forName(properties.getProperty("driver.class"));
            connection = DriverManager.getConnection(properties.getProperty("url"), properties);
        } catch (IOException e) {
            log.log(Level.SEVERE, "read properties error", e);
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "driver class error", e);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "get database connection error", e);
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
            log.log(Level.SEVERE, "close error", e);
        }
    }
}
