package com.code.entity;

import com.common.Constants;
import com.common.util.HttpClientUtil;
import com.common.util.PropertiesUtil;
import com.common.util.StringUtil;
import com.database.util.TableUtil;
import com.template.util.TemplateUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/8/3.
 */
public class EntityGenerator {

    public static Logger log = Logger.getLogger(EntityGenerator.class.getName());


    public static String getEntityCode(String catalog, String schemaPattern,
                                       String tableName, String[] types, String entityName) throws Exception {
        Properties typeProp = PropertiesUtil.load("type.properties", "/properties/mysql/type.properties");
        Properties wordProp = PropertiesUtil.load("word.properties", "/properties/word.properties");
        List<Map<String, Object>> tableList = TableUtil.getTables(catalog, schemaPattern, tableName, types);
        Map<String, Object> table = tableList.get(0);
        log.log(Level.INFO, "database table:" + tableName);
        List<Map<String, Object>> columnList = TableUtil.getColumns(catalog, schemaPattern, tableName, null);
        for (Map<String, Object> column : columnList) {
            column.put("javaType", typeProp.getProperty(String.valueOf((column.get("TYPE_NAME")))));
            String columnName = String.valueOf((column.get("COLUMN_NAME")));
            log.log(Level.INFO, "column:" + columnName);
            String field = columnName;
            //字段名没有区分大小写
            if(!(StringUtil.isExist("\\p{Lower}", columnName)
                    && StringUtil.isExist("\\p{Upper}", columnName))){
                //读取配置文件
                field = wordProp.getProperty(columnName);
                if (StringUtils.isEmpty(field)) {
                    //在网上查询
                    field = getField(columnName);
                }
            }
            column.put("field", field);
            column.put("firstUpperCaseField", StringUtil.firstLetterUppercase(field));
        }
        table.put("entity", entityName);
        table.put("columnList", columnList);
        return TemplateUtil.getContent(table, "/template/entity.vm");
    }

    /**
     * 根据字段名称获取成员变量名称,
     * 在http://cn.bing.com查询字段名称,
     * 用Jsoup解析查询结果并取值.
     *
     * @param column 字段名称
     * @return
     */
    public static String getField(String column) {
        InputStream is = null;
        Document doc = null;
        // create URL string
        String url = "http://cn.bing.com/search?q=" + column;
        log.log(Level.INFO, url);
        try {
            // connect & download html
            is = HttpClientUtil.downloadAsStream(url);
            // parse html by Jsoup
            doc = Jsoup.parse(is, Constants.DEFAULT_ENCODING, "");
            //取所有查询结果
            Elements elements = doc.getElementById("b_results").getElementsByTag("strong");
            String result = elements.html();
            Map<String, Integer> wordMap = wordStatistics(column, result.split("\\n"));
            Set<String> words = StringUtil.filter(wordMap);
            String rst = StringUtil.camelCased(words, column);
            return StringUtil.firstLetterLowercase(rst);
        } catch (Exception e) {

        } finally {
            IOUtils.closeQuietly(is);
            is = null;
            doc = null;
        }
        return column.toLowerCase();
    }


    /**
     * 单词频率统计
     * @param column
     * @param args
     * @return
     */
    private static Map<String, Integer> wordStatistics(String column, String[] args) {
        Set<String> wordSet;
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
