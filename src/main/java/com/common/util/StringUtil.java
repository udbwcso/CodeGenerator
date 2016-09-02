package com.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/8/3.
 */
public class StringUtil {

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
     * 根据字母大小写分隔字符串
     * 只分隔小写字母后面是大写字母的情况
     * @param string 由字母和数字组成的字符串
     * @return
     */
    public static String[] splitByCase(String string){
        char[] chars = string.toCharArray();
        //在大字母前添加分隔符
        StringBuilder str = new StringBuilder();
        str.append(chars[0]);
        String separator = "_";
        for (int i = 1; i < chars.length; i++) {
            if (Character.isLowerCase(chars[i-1]) && Character.isUpperCase(chars[i])) {
                str.append(separator);
            }
            str.append(chars[i]);
        }
        string = str.toString().trim().toLowerCase();
        String[] strings = string.split(separator);
        return strings;
    }

    /**
     * 替换所有以"<"开头以">"结尾的标签
     * @param str 需要过滤的字符串
     * @return String
     */
    private static String replaceTag(String str, String replacement) {
        Pattern pattern = Pattern.compile("<([^>]*)>");
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, replacement);
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


    /**
     * 替换不是数字或字母的字符
     * @param str
     * @param replacement
     * @return
     */
    public static String replaceCharacters(String str, String replacement){
        char[] chars = str.toCharArray();
        StringBuffer rst = new StringBuffer();
        for (char c : chars) {
            if(Character.isLetterOrDigit(c)) {
                rst.append(c);
            } else {
                rst.append(replacement);
            }

        }
        return rst.toString();
    }

    /**
     * 替换html,中文,特殊字符
     * @param str
     * @return
     */
    public static String replaceSpecialChar(String str, String replacement){
        //替换所有以"<"开头以">"结尾的标签
        String rst = replaceTag(str, replacement);
        //替换中文
        rst = rst.replaceAll("[\\u4E00-\\u9FA5]", replacement);
        //替换不是数字或字母的字符
        rst = replaceCharacters(rst, replacement);
        //替换空白字符
        rst = replaceWhitespace(rst, replacement);
        return rst;
    }

    /**
     * 根据str的组成单词words,将str按驼峰命名规则命名
     * @param words 组成单词
     * @param str
     * @return
     */
    public static String camelCased(List<String> words, String str){
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
        return lowercase(rst.toString(), 0, 1);
    }

    /**
     * 将str按驼峰命名规则命名
     * @param str
     * @param separator 分隔符
     * @return
     */
    public static String camelCased(String str, String separator) {
        str = str.toLowerCase();
        String[] words = str.split(separator);
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
