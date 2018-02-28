/*
 * 项目名称：test
 * 类名称: Problem
 * 创建时间: 2016年9月2日 上午10:44:07
 * 创建人: zhailiang@pz365.com
 *
 * 修改历史:
 * 
 * Copyright: 2016 www.pz365.com Inc. All rights reserved.
 * 
 */
package com.ymt.test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 *
 * @author zhailiang@pz365.com
 * @version 1.0.0
 */
public class Examine {

    /**
     * 斐波那契数列的定义为：F(n+1)=F(n)+F(n-1),n为自然数。其中F(1)=F(2)=1,定义S(n)=F(1)+F(2)+...+F(n), n>3。请用java语言写一个方法计算S(n).
     * 
     * 注意：是计算S(n),不是计算f(n)
     * 
     * @param n
     * @return
     * @author zhailiang
     * @since 2016年9月2日
     */
    public int fibonacci(int n) {
        if (n < 1) {
            return 0;
        } else if (n == 1) {
            return 1;
        }
        List<Integer> fn = new ArrayList<>();
        fn.add(1);
        fn.add(1);
        int fnIndex = fn.size();
        for (int i = fnIndex; i < n; i++) {
            fn.add(fn.get(i - 1) + fn.get(i - 2));
        }
        int sum = 0;
        for (int i = 0; i < fn.size(); i++) {
            sum = sum + fn.get(i);
        }
        return sum;
    }
    
    
    /**
     * 假设有x个犯人，将他们围成一圈，杀掉第七个，又从第八个开始杀掉第七个，直到剩下最后一个。请编写程序，计算剩下的人是第几个。
     * 
     * @param x 犯人的个数
     * @return
     * @author zhailiang
     * @since 2016年9月2日
     */
    public int josephusKill(int x) {
        int[] person = new int[x + 1];
        person[0] = 0;
        for (int i = 1; i < x + 1; i++) {
            person[i] = 1;
        }
        int count = 0, index = 1, killPerCnt = 0;
        int last = 0;
        while (killPerCnt != x) {
            count = count + person[index];
            if (7 == count) {
                count = 0;
                person[index] = 0;
                killPerCnt++;
                last = index;
            }
            index++;
            index = index % (x + 1);
        }
        return last;
    }
    
    
    /**
     * 编写一个程序，将txt文件1中包含数字的单词写到到txt文件2中,写入时每个单词一行，txt文件1中的单词用回车或空格进行分隔。文件均为UTF-8编码.
     * 
     * @param file1Path 文件1路径
     * @param file2Path 文件2路径
     * @author zhailiang
     * @since 2016年9月2日
     */
    public void findWord(String file1Path, String file2Path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(file1Path)), Charset.forName("UTF-8"));
        String[] words = content.split("\\s+");
        String lineSeparator = System.getProperty("line.separator");
        Pattern pattern = Pattern.compile("\\d");
        StringBuilder rst = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            Matcher matcher = pattern.matcher(words[i]);
            if(matcher.find()){
                rst.append(words[i]).append(lineSeparator);
            }
        }
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(file2Path), Charset.forName("UTF-8"));
        writer.write(rst.toString());
        writer.flush();
        writer.close();
    }
    
}
