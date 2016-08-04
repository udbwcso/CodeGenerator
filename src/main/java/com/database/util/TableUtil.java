package com.database.util;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by udbwcso on 2015/4/23.
 */
public class TableUtil {

    /**
     * 获取数据库字段的信息
     * @param catalog ""表示没有catalog,null表示不将catalog作为查询条件
     * @param schemaPattern ""表示没有schema,null表示不将schema作为查询条件
     * @param tableNamePattern 表名
     * @param columnNamePattern 字段名
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> getColumns(String catalog, String schemaPattern,
                                                       String tableNamePattern, String columnNamePattern) throws Exception {
        Connection connection = DBUtil.getConnection();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet rs = databaseMetaData.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
        ResultSetMetaData metaData = rs.getMetaData();
        int cols = metaData.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 1; i <= cols; i++) {
                map.put(metaData.getColumnName(i), rs.getObject(i));
            }
            list.add(map);
        }
        DBUtil.close(connection, null, rs);
        return list;
    }

    /**
     * 获取数据库表的信息,可根据表名模糊查询"%" + tableName + "%"
     * @param catalog ""表示没有catalog,null表示不将catalog作为查询条件
     * @param schemaPattern ""表示没有schema,null表示不将schema作为查询条件
     * @param tableNamePattern 表名
     * @param types 表的类型
     * @return
     * @throws Exception
     */
    public static List<Map<String, Object>> getTables(String catalog, String schemaPattern,
                                                      String tableNamePattern, String[] types) throws Exception {
        Connection connection = DBUtil.getConnection();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet rs = databaseMetaData.getTables(catalog, schemaPattern, tableNamePattern, types);
        ResultSetMetaData metaData = rs.getMetaData();
        int cols = metaData.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 1; i <= cols; i++) {
                map.put(metaData.getColumnName(i), rs.getObject(i));
            }
            list.add(map);
        }
        DBUtil.close(connection, null, rs);
        return list;
    }
}
