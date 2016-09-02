package com.common.util;

import com.code.Configuration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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
 * Created by Administrator on 2016/9/2.
 */
public class WordUtil {
    private static Logger log = Logger.getLogger(WordUtil.class.getName());

    private static final String SEARCH_URL = "http://cn.bing.com/search?q=";

    /**
     * 根据单词频率过滤单词,如Map的key中有id,order,orderid
     * 如果id和order出现的次数之和比orderid大则删除orderid.
     *
     * @param wordMap
     */
    public static void deleteRepeat(LinkedHashMap<String, Integer> wordMap) {
        List<String> keyList = getMapKeyList(wordMap);
        for (String key : keyList) {
            if (wordMap.get(key) == null) {
                continue;
            }
            List<String> tmpKeyList = getMapKeyList(wordMap);
            tmpKeyList.remove(key);
            List<String> subWordList = new ArrayList<String>();
            String remain = subWord(key, tmpKeyList, subWordList);
            if (StringUtils.isNotEmpty(remain) || subWordList.size() < 2) {
                continue;
            }
            Integer cnt = 0;
            for (String subWord : subWordList) {
                cnt = cnt + wordMap.get(subWord);
            }
            if (cnt >= wordMap.get(key)) {
                wordMap.remove(key);
            }
        }
    }

    /**
     * 将map的所有key存入List中
     *
     * @param map
     * @return
     */
    private static List<String> getMapKeyList(LinkedHashMap<String, Integer> map) {
        List<String> keyList = new ArrayList<String>();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            keyList.add(key);
        }
        return keyList;
    }


    /**
     * 在wordList中查找string的组成单词
     * 并将string的组成单词存入subWordList
     *
     * @param string
     * @param wordList
     * @param subWordList
     * @return
     */
    private static String subWord(String string, List<String> wordList, List<String> subWordList) {
        if (StringUtils.isEmpty(string)) {
            return string;
        }
        for (int i = 0; i < wordList.size(); i++) {
            if (string.startsWith(wordList.get(i))) {
                subWordList.add(wordList.get(i));
                return subWord(string.replaceFirst(wordList.get(i), ""), wordList, subWordList);
            }
        }
        return string;
    }

    /**
     * 获取字符串的组成单词
     *
     * @param string
     * @return
     */
    public static List<String> splitByWord(String string) {
        List<String> searchRstList = new ArrayList<String>();
        for (int i = 1; i < 4; i++) {
            String[] strings = search(string, i);
            searchRstList.addAll(Arrays.asList(strings));
        }
        List<String> wordList = new ArrayList<String>();
        for (String arg : searchRstList) {
            arg = StringUtil.replaceSpecialChar(arg, " ");
            if (StringUtils.isEmpty(arg)) {
                continue;
            }
            String[] words = arg.split(" ");
            for (int i = 0; i < words.length; i++) {
                if (StringUtils.isEmpty(words[i])) {
                    continue;
                }
                wordList.addAll(Arrays.asList(StringUtil.splitByCase(words[i])));
            }
        }
        return wordList;
    }

    /**
     * 获取字符串的组成单词
     * 在http://cn.bing.com查询字符串,
     * 用Jsoup解析查询结果并取值.
     *
     * @param string
     * @param page   页数
     * @return
     */
    public static String[] search(String string, int page) {
        InputStream is = null;
        Document doc = null;
        // create URL string
        String url = SEARCH_URL + string;
        if (page > 1) {
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
     * 删除null和""
     *
     * @return
     */
    public static LinkedHashMap<String, Integer> wordStatistics(List<String> wordList) {
        LinkedHashMap<String, Integer> wordMap = new LinkedHashMap<String, Integer>();
        for (String word : wordList) {
            if (StringUtils.isEmpty(word)) {
                continue;
            }
            Integer cnt = wordMap.get(word);
            if (cnt == null) {
                wordMap.put(word, 1);
            } else {
                wordMap.put(word, cnt + 1);
            }
        }
        return wordMap;
    }

    /**
     * 根据string的组成单词words,将string按驼峰命名规则命名
     *
     * @param string
     * @param wordMap
     * @return
     * @throws IOException
     */
    public static String camelCased(String string, LinkedHashMap<String, Integer> wordMap) throws IOException {
        //读取配置文件
        Properties wordProp = PropertiesUtil.load(Configuration.get("word"));
        String camelCased = PropertiesUtil.getPropertyIgnoreCase(wordProp, string);
        if (StringUtils.isEmpty(camelCased)) {
            if (Configuration.get("column_separator") != null) {
                camelCased = StringUtil.camelCased(string, Configuration.get("column_separator"));
            } else {
                List<String> wordList = getMapKeyList(wordMap);
                camelCased = StringUtil.camelCased(wordList, string);
            }
        }
        return camelCased;
    }
}
