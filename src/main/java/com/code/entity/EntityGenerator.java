package com.code.entity;

import com.code.Configuration;
import com.common.util.PropertiesUtil;
import com.common.util.StringUtil;
import com.common.util.WordUtil;
import com.database.util.TableUtil;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/8/3.
 */
public class EntityGenerator {

    private static Logger log = Logger.getLogger(EntityGenerator.class.getName());

    public static Map<String, Object> getTableInfo(String catalog, String schemaPattern,
                                                   String tableName, String[] types, String entityName) throws Exception {
        Properties typeProp = PropertiesUtil.load(Configuration.get("type"));

        List<Map<String, Object>> tableList = TableUtil.getTables(catalog, schemaPattern, tableName, types);
        Map<String, Object> table = tableList.get(0);
        List<String> primaryKeyList = TableUtil.getPrimaryKeys(catalog, schemaPattern, tableName);
        log.log(Level.INFO, "database table:" + tableName);
        List<Map<String, Object>> columnList = TableUtil.getColumns(catalog, schemaPattern, tableName, null);

        List<String> fieldWordList = new ArrayList<String>();
        for (Map<String, Object> column : columnList) {
            String typeName = String.valueOf(column.get("TYPE_NAME"));
            column.put("javaType", typeProp.getProperty(typeName.replaceAll(" ", "_")));
            String columnName = String.valueOf((column.get("COLUMN_NAME")));
            for (int i = 0; i < primaryKeyList.size(); i++) {
                if (columnName.equals(primaryKeyList.get(i))) {
                    column.put("isPrimaryKey", true);
                }
            }
            log.log(Level.INFO, "column:" + columnName);
            fieldWordList.addAll(WordUtil.splitByWord(columnName));
        }

        LinkedHashMap<String, Integer> wordMap = WordUtil.wordStatistics(fieldWordList);
        wordMap = sortMapByValue(wordMap);
        WordUtil.deleteRepeat(wordMap);

        for (Map<String, Object> column : columnList) {
            String columnName = String.valueOf((column.get("COLUMN_NAME")));
            String field = WordUtil.camelCased(columnName, wordMap);
            column.put("field", field);
            column.put("firstUpperCaseField", StringUtil.uppercase(field, 0, 1));
        }
        table.put("entity", entityName);
        table.put("columnList", columnList);
        return table;
    }

    /**
     * map排序
     *
     * @param oriMap
     * @return
     */
    public static LinkedHashMap<String, Integer> sortMapByValue(Map<String, Integer> oriMap) {
        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>(oriMap.entrySet());
        Collections.sort(entryList,
                new Comparator<Map.Entry<String, Integer>>() {
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        Integer rst = o2.getValue() - o1.getValue();
                        if (rst == 0) {
                            return o2.getKey().length() - o1.getKey().length();
                        }
                        return rst;
                    }
                });
        Iterator<Map.Entry<String, Integer>> iterator = entryList.iterator();
        Map.Entry<String, Integer> tmpEntry = null;
        while (iterator.hasNext()) {
            tmpEntry = iterator.next();
            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
        }
        return sortedMap;
    }
}
