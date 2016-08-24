package com.code.entity;

import com.code.Configuration;
import com.common.util.PropertiesUtil;
import com.common.util.StringUtil;
import com.database.util.TableUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/8/3.
 */
public class EntityGenerator {

    private static Logger log = Logger.getLogger(EntityGenerator.class.getName());

    private static final String SEARCH_URL = "http://cn.bing.com/search?q=";


    public static Map<String, Object> getTableInfo(String catalog, String schemaPattern,
                                       String tableName, String[] types, String entityName) throws Exception {
        Properties typeProp = PropertiesUtil.load(Configuration.get("type"));
        Properties wordProp = PropertiesUtil.load(Configuration.get("word"));
        List<Map<String, Object>> tableList = TableUtil.getTables(catalog, schemaPattern, tableName, types);
        Map<String, Object> table = tableList.get(0);
        List<String> primaryKeyList = TableUtil.getPrimaryKeys(catalog, schemaPattern, tableName);
        log.log(Level.INFO, "database table:" + tableName);
        List<Map<String, Object>> columnList = TableUtil.getColumns(catalog, schemaPattern, tableName, null);
        boolean camelCased = false;
        for (Map<String, Object> column : columnList) {
            String typeName = String.valueOf(column.get("TYPE_NAME"));
            column.put("javaType", typeProp.getProperty(typeName.replaceAll(" ", "_")));
            String columnName = String.valueOf((column.get("COLUMN_NAME")));
            for (int i = 0; i < primaryKeyList.size(); i++) {
                if(columnName.equals(primaryKeyList.get(i))){
                    column.put("isPrimaryKey", true);
                }
            }
            log.log(Level.INFO, "column:" + columnName);
            //读取配置文件
            String field = wordProp.getProperty(columnName);
            if (StringUtils.isEmpty(field)) {
                if (columnName.contains("_") || camelCased) {
                    field = StringUtil.camelCased(columnName);
                    camelCased = true;
                } else if (!(StringUtil.isExist("\\p{Lower}", columnName)
                        && StringUtil.isExist("\\p{Upper}", columnName))) {//字段名没有区分大小写
                    //在网上查询
                    field = getField(columnName);
                }
            }
            column.put("field", field);
            column.put("firstUpperCaseField", StringUtil.uppercase(field, 0, 1));
        }
        table.put("entity", entityName);
        table.put("columnList", columnList);
        return table;
    }

    /**
     * 根据字段名获取其按照驼峰命名规则的名称
     * @param column 字段名
     * @return
     */
    public static String getField(String column){
        String[] strings = search(column);
        Map<String, Integer> wordMap = wordStatistics(column, strings);
        Set<String> words = StringUtil.filter(wordMap);
        String rst = StringUtil.camelCased(words, column);
        return StringUtil.lowercase(rst, 0, 1);
    }

    /**
     * 根据字段名称获取成员变量名称,
     * 在http://cn.bing.com查询字段名称,
     * 用Jsoup解析查询结果并取值.
     * @param column 字段名称
     * @return
     */
    public static String[] search(String column) {
        InputStream is = null;
        Document doc = null;
        // create URL string
        String url = SEARCH_URL + column;
        log.log(Level.INFO, url);
        try {
            // parse html by Jsoup
            doc = Jsoup.parse(new URL(url), 60000);
            //取所有查询结果
            Elements elements = doc.getElementById("b_results").getElementsByTag("strong");
            String result = elements.html();
            return result.split("\\n");
        } catch (Exception e) {
            log.log(Level.SEVERE, "search column error", e);
        } finally {
            IOUtils.closeQuietly(is);
            is = null;
            doc = null;
        }
        return new String[0];
    }


    /**
     * 单词频率统计
     * @param column
     * @param args
     * @return
     */
    private static Map<String, Integer> wordStatistics(String column, String[] args) {
        Set<String> wordSet = null;
        Map<String, Integer> wordMap = new HashMap<String, Integer>();
        for (String arg : args) {
            arg = StringUtil.filter(arg);
            if(StringUtils.isEmpty(arg)){
                continue;
            }
            wordSet = StringUtil.split(arg);
            //统计单词出现的频率
            for (String s : wordSet) {
                Integer cnt = wordMap.get(s);
                if(cnt == null){
                    wordMap.put(s, 1);
                } else {
                    wordMap.put(s, cnt + 1);
                }
            }
        }
        /**
         * 由于查询的过程中会增加所查询的单词(字段名)的频率
         * 在这里适量的减少字段名的频率
         */
        Iterator<String> it = wordMap.keySet().iterator();
        while (it.hasNext()){
            String key = it.next();
            if(key.equalsIgnoreCase(column)) {
                Integer value = wordMap.get(key) / 2 + 1;
                wordMap.put(key, value);
            }
        }
        return wordMap;
    }
}
