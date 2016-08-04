package com.database.util;


import com.database.bean.Column;
import com.database.bean.Table;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by udbwcso on 2015/4/23.
 */
public class TableUtil {


    public static void main(String[] args) throws SQLException {
    }


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

    public static List<Map<String, Object>> getTables(String catalog, String schemaPattern,
                                                      String tableNamePattern, String[] types) throws Exception {
        Connection connection = DBUtil.getConnection();
        DatabaseMetaData databaseMetaData = connection.getMetaData();
//            if(!tableName.startsWith("%")){
//                tableName = "%" + tableName + "%";
//            }
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

//    public static List<Table> getTableList(String catalog, String schemaPattern,
//                                           String tableNamePattern, String[] types) throws Exception {
//        List<Table> tableList = new ArrayList<Table>();
//        List<Map<String, Object>> tableMapList = TableUtil.getTables(catalog, schemaPattern, tableNamePattern, types);
//        for (int i = 0; i < tableMapList.size(); i++) {
//            Table table = Converter.convert(tableMapList.get(i), Table.class, new KeyComparator());
//            List<Column> columnList = new ArrayList<Column>();
//            List<Map<String, Object>> columnMapList = TableUtil.getColumns(table.getTableCat(), table.getTableSchem(), table.getTableName(), null);
//            for (int j = 0; j < columnMapList.size(); j++) {
//                Column column = Converter.convert(columnMapList.get(j), Column.class, new KeyComparator());
//                columnList.add(column);
//            }
//            table.setColumnList(columnList);
//            tableList.add(table);
//        }
//        return tableList;
//    }
}
