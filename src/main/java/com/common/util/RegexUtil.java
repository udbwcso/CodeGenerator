package com.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    /**
     * 整个字符串str与正则表达式regex是否匹配
     *
     * @param regex
     * @param str
     * @return
     */
    public static boolean isMatch(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
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


    public static List<String> getMatchString(String regex, String str, int group) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            if (group < 0) {
                list.add(matcher.group());
            } else {
                list.add(matcher.group(group));
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String s = "中国电影(600977)";
        List<String> list = getMatchString("\\((.*?)\\)", s, 1);
        for (String s1 : list) {
            System.out.println(s1);
        }
    }


}