package com.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/3.
 */
public class StringUtil {
    public static void main(String[] args) {

    }

    /**
     * 字符串指定位置字符大写
     * @param string 字符串
     * @param beginIndex 开始位置
     * @param endIndex 结束位置
     * @return
     */
    public static String uppercase(String string, int beginIndex, int endIndex) {
        String uppercaseStr = string.substring(beginIndex, endIndex);
        return string.substring(0, beginIndex) + uppercaseStr.toUpperCase() + string.substring(endIndex);
    }

    /**
     * 字符串指定位置字符小写
     * @param string 字符串
     * @param beginIndex 开始位置
     * @param endIndex 结束位置
     * @return
     */
    public static String lowercase(String string, int beginIndex, int endIndex) {
        String lowercaseStr = string.substring(beginIndex, endIndex);
        return string.substring(0, beginIndex) + lowercaseStr.toLowerCase() + string.substring(endIndex);
    }

    /**
     * 字符串str中是否存在与正则表达式regex匹配的部分
     *
     * @param regex
     * @param str
     * @return
     */
    public static boolean isExist(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 替换空白字符
     * @param str
     * @param replacement
     * @return
     */
    public static String replaceWhitespace(String str, String replacement){
        Pattern pattern = Pattern.compile("\\s+");
        Matcher matcher = pattern.matcher(str);
        return matcher.replaceAll(replacement);

    }

    /**
     *
     * @param string
     * @return
     */
    public static Set<String> split(String string){
        string = replaceWhitespace(string, " ");
        char[] chars = string.toCharArray();
        //在大字母前添加空格
        StringBuilder str = new StringBuilder();
        int i = 0;
        while (i < chars.length) {
            if(Character.isUpperCase(chars[i])){
                str.append(" ");
                while (i < chars.length && Character.isUpperCase(chars[i])){
                    str.append(chars[i]);
                    ++i;
                }
            } else {
                str.append(chars[i]);
                ++i;
            }
        }
        string = str.toString().trim().toLowerCase();
        String[] strings = string.split(" ");
        Set<String> words = new HashSet<String>();
        for (String s : strings) {
            if(!StringUtils.isEmpty(s)){
                words.add(s);
            }
        }
        return words;
    }

    /**
     * 过滤所有以"<"开头以">"结尾的标签
     *
     * @param str 需要过滤的字符串
     * @return String
     */
    private static String htmlFilter(String str) {
        Pattern pattern = Pattern.compile("<([^>]*)>");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 清除掉所有特殊字符
     * @param str
     * @return
     */
    private static String charFilter(String str) {
        String regEx = "[`~!@#$%^&*()+=_\\-|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll(" ").trim();
    }

    /**
     * 将中文替换为空格
     * @param str
     * @return
     */
    public static String filter(String str){
        String rst = htmlFilter(str);
        //过滤中文
        rst = rst.replaceAll("[\\u4E00-\\u9FA5]", " ");
        //过滤特殊字符
        rst = charFilter(rst);
        return rst;
    }

    /**
     * 根据单词频率过滤单词,如Map的key中有id,order,orderid
     * 如果id和order出现的次数之和比orderid大则删除orderid,
     * 反之则删除id和order
     * @param map key:单词,value:单词出现的次数
     * @return
     */
    public static Set<String> filter(Map<String, Integer> map){
        String[] strings = map.keySet().toArray(new String[0]);
        List<String> wordList = new ArrayList<String>();
        for (int i = 0; i < strings.length; i++) {
            if(map.get(strings[i]) == null){//已经在map中删除这个单词
                continue;
            }
            String word = strings[i];
            Integer cnt = 0;
            for (int j = 0; j < strings.length; j++) {
                if(i == j || map.get(strings[j]) == null){
                    continue;
                }
                if(word.contains(strings[j])){
                    word = word.replaceAll(strings[j], "");
                    cnt = cnt + map.get(strings[j]);
                    wordList.add(strings[j]);
                }
            }
            if(!StringUtils.isEmpty(word)){
                wordList.clear();
                continue;
            }
            if(cnt > map.get(strings[i])){
                map.remove(strings[i]);
            } else {
                for (int j = 0; j < wordList.size(); j++) {
                    map.remove(wordList.get(j));
                }
            }
            wordList.clear();
        }
        return map.keySet();
    }

    /**
     * 根据str的组成单词words,将str按驼峰命名规则命名
     * @param words 组成单词
     * @param str
     * @return
     */
    public static String camelCased(Set<String> words, String str){
        String field = str.toLowerCase();
        StringBuffer rst = new StringBuffer();
        while (!StringUtils.isEmpty(field)){
            String tmp = field;
            for (String s : words) {
                if(field.startsWith(s)){
                    field = field.replaceFirst(s, "");
                    rst.append(uppercase(s, 0, 1));
                    break;
                }
            }
            if(tmp.equals(field)){
                rst = rst.append(uppercase(field, 0, 1));
                break;
            }
        }
        return rst.toString();
    }

    /**
     * 根据str的组成单词words,将str按驼峰命名规则命名
     * @param str
     * @return
     */
    public static String camelCased(String str) {
        String[] words = str.split("_");
        if(words.length <= 1){
            return str;
        }
        StringBuffer rst = new StringBuffer();
        rst.append(words[0]);
        for (int i = 1; i < words.length; i++) {
            rst.append(uppercase(words[i].toLowerCase(), 0, 1));
        }
        return rst.toString();
    }
}
