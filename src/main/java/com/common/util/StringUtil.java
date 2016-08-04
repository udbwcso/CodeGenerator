package com.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/3.
 */
public class StringUtil {
    /**
     * 首字母大写
     * @param string
     * @return
     */
    public static String firstLetterUppercase(String string) {
        String firstLetter = string.substring(0, 1);
        return firstLetter.toUpperCase() + string.substring(1);
    }

    /**
     * 首字母小写
     * @param string
     * @return
     */
    public static String firstLetterLowercase(String string) {
        String firstLetter = string.substring(0, 1);
        return firstLetter.toLowerCase() + string.substring(1);
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

    public static Set<String> split(String string){
        Set<String> words = new HashSet<String>();
        char[] chars = string.toCharArray();
        int start;
        int i = 0;
        while (i < chars.length) {
            if(!Character.isLetter(chars[i])){
                ++i;
                continue;
            }
            start = i;
            if(Character.isUpperCase(chars[i])
                    && Character.isUpperCase(chars[i+1])) {
                do {
                    ++i;
                } while (i < chars.length && Character.isUpperCase(chars[i]));
            } else if(Character.isUpperCase(chars[i])
                    && Character.isLowerCase(chars[i+1])){
                do {
                    ++i;
                }while (i < chars.length && Character.isLowerCase(chars[i]));
            } else if(Character.isLowerCase(chars[i])
                    && Character.isLowerCase(chars[i+1])){
                do {
                    ++i;
                }while (i < chars.length && Character.isLowerCase(chars[i]));
            } else {
                ++i;
            }
            words.add(string.substring(start, i).toLowerCase());
        }
        return words;
    }

    /**
     *
     * 基本功能：过滤所有以"<"开头以">"结尾的标签
     * <p>
     *
     * @param str
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

    private static String charFilter(String str) {
        // 清除掉所有特殊字符
        String regEx = "[`~!@#$%^&*()+=_\\-|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll(" ").trim();
    }

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
                    rst.append(firstLetterUppercase(s));
                    break;
                }
            }
            if(tmp.equals(field)){
                rst = rst.append(firstLetterUppercase(field));
                break;
            }
        }
        return rst.toString();
    }
}
