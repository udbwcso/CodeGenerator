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

import java.io.IOException;
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
                if(columnName.equals(primaryKeyList.get(i))){
                    column.put("isPrimaryKey", true);
                }
            }
            log.log(Level.INFO, "column:" + columnName);
            fieldWordList.addAll(getFieldWord(columnName));
        }


        LinkedHashMap<String, Integer> wordMap = wordStatistics(fieldWordList);
        wordMap = sortMapByValue(wordMap);

//        Iterator<String> it1 = wordMap.keySet().iterator();
//        while (it1.hasNext()){
//            String key = it1.next();
//            System.out.println(key + "-------" + wordMap.get(key));
//        }

        for (Map<String, Object> column : columnList) {
            String columnName = String.valueOf((column.get("COLUMN_NAME")));
            String field = getField(columnName, wordMap);
            column.put("field", field);
            column.put("firstUpperCaseField", StringUtil.uppercase(field, 0, 1));
        }
        table.put("entity", entityName);
        table.put("columnList", columnList);
        return table;
    }

    public static String getField(String column, LinkedHashMap<String, Integer> wordMap) throws IOException {
        //读取配置文件
        Properties wordProp = PropertiesUtil.load(Configuration.get("word"));
        String field = wordProp.getProperty(column);
        if(StringUtils.isEmpty(field)){
            field = wordProp.getProperty(column.toLowerCase());
        }
        if (StringUtils.isEmpty(field)) {
            if (Configuration.get("column_separator") != null) {
                field = StringUtil.camelCased(column, Configuration.get("column_separator"));
            } else {
                List<String> columnWordList = new ArrayList<String>();
                Iterator<String> it = wordMap.keySet().iterator();
                while (it.hasNext()){
                    String key = it.next();
                    columnWordList.add(key);
                }
                field = StringUtil.camelCased(columnWordList, column);
            }
        }
        return field;
    }

    /**
     * map排序
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
                        if(rst == 0){
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

    /**
     * 获取字段名的组成单词
     * @param column 字段名
     * @return
     */
    public static List<String> getFieldWord(String column){
        List<String> searchRstList = new ArrayList<String>();
        for (int i = 1; i < 4; i++) {
            String[] strings = search(column, i);
            searchRstList.addAll(Arrays.asList(strings));
        }
        List<String> wordList = new ArrayList<String>();
        for (String arg : searchRstList) {
            arg = StringUtil.replaceSpecialChar(arg, " ");
            if(StringUtils.isEmpty(arg)){
                continue;
            }
            String[] words = arg.split(" ");
            for (int i = 0; i < words.length; i++) {
                if(StringUtils.isEmpty(words[i])){
                    continue;
                }
                wordList.addAll(Arrays.asList(StringUtil.splitByCase(words[i])));
            }
        }
        return wordList;
    }

    /**
     * 根据字段名称获取成员变量名称,
     * 在http://cn.bing.com查询字段名称,
     * 用Jsoup解析查询结果并取值.
     * @param column 字段名称
     * @param page 页数
     * @return
     */
    public static String[] search(String column, int page) {
        InputStream is = null;
        Document doc = null;
        // create URL string
        String url = SEARCH_URL + column;
        if(page > 1){
            url = url + "&first=" + (page + 1) + "1";
        }
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
        }
        return new String[0];
    }


    /**
     * 单词频率统计
     * @return
     */
    private static LinkedHashMap<String, Integer> wordStatistics(List<String> wordList) {
        LinkedHashMap<String, Integer> wordMap = new LinkedHashMap<String, Integer>();
        for (String word : wordList) {
            if(StringUtils.isEmpty(word)){
                continue;
            }
            Integer cnt = wordMap.get(word);
            if(cnt == null){
                wordMap.put(word, 1);
            } else {
                wordMap.put(word, cnt + 1);
            }
        }
        return wordMap;
    }
}
