package com.doc.read;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/10/14.
 */
public class T {
    public static void main1(String[] args) throws IOException {
        String path = "D:\\code\\wxshop-service\\wxshop-order-service\\src\\main\\java\\com\\ymt\\wxshop\\order\\domain\\BackOrder.java";
        String content = new String(Files.readAllBytes(Paths.get(path)), Charset.forName("UTF-8"));
//        String regex = "^(\\\\*).*";
        String regex = "\\s+\\\\*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            System.out.println("--");
            System.out.println(matcher.group());
        }

    }

    public static List<String> getMatchString(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

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

    public static void main(String[] args) throws IOException {
        //读文件
        String path = "E:\\workspace\\code\\src\\main\\java\\com\\doc\\read\\Order.txt";
//        String path = "E:\\workspace\\wxshop-service\\wxshop-integral-service\\src\\main\\java\\com\\ymt\\wxshop\\integral\\domain\\IntegralChangeFlows.java";
        String content = new String(Files.readAllBytes(Paths.get(path)), Charset.forName("UTF-8"));
        //取class内的代码
//        int start = StringUtils.indexOf(content, "{");
//        int end = StringUtils.lastIndexOf(content, "}");
//        String code = content.substring(start + 1, end).trim();
////        code = code.substring(0, StringUtils.indexOf(code, "{"));
//        while (true) {
//            int index = StringUtils.lastIndexOf(code, "public");
//            if (index == -1) {
//                index = StringUtils.lastIndexOf(code, "private");
//            }
//
//            String remove = code.substring(index, StringUtils.lastIndexOf(code, "}") + 1);
//            String firstLine = remove.split("\n")[0].trim();
//            if(firstLine.endsWith("{") && firstLine.replace("{", "").trim().endsWith(")")){
//                code = code.replace(remove, "");
//            } else {
//                break;
//            }
//
//
//        }
//        code = code.replaceAll("\\s*?(public).*?\\(.*?\\{(.|\\s)*?\\}", "");
        String code = content;
        System.out.println(code);
        List<String> list = getMatchString("/\\*(.|\\s)*?(private).*?\\;", code);
        List<Field> fileList = new ArrayList<>();
        for (String property : list) {
            Field field = new Field();
            String[] strings = property.split("\\*/");

            String remark = strings[0].trim();
            if (remark.startsWith("/*")) {
                remark = remark.replaceFirst("/\\*", "");
            }
            String[] remarkLines = remark.split("\n");
            StringBuilder remarkContent = new StringBuilder();
            for (int i = 0; i < remarkLines.length; i++) {
                String line = remarkLines[i];
                if (line.trim().startsWith("*")) {
                    remarkContent.append(line.trim().replaceFirst("\\*", "").trim());
                }
            }
            field.setRemark(remarkContent.toString());

            String fieldStr = strings[1].trim();
            String[] tmp = fieldStr.split("private");
            String[] fieldItem = tmp[1].trim().split("\\s+");
            field.setType(fieldItem[0]);
            field.setName(fieldItem[1].replace(";", ""));
            fileList.add(field);
        }

        for (int i = 0; i < fileList.size(); i++) {
            Field field = fileList.get(i);
            System.out.println("--------------------");
            System.out.println(field.getName());
            System.out.println(field.getType());
            System.out.println(field.getRemark());
            System.out.println("--------------------");
        }
    }
}
