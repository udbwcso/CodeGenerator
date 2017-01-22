package com.stock.util;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * file path util
 */
public class FilePathUtil {
    private static List<String> filePathList = new ArrayList<String>();


    /**
     * 获取path目录下的所有文件路径
     *
     * @param path
     */
    public static List<String> getFilePath(String path) {
        findFilePath(path, null);
        return filePathList;
    }


    /**
     * 获取path目录下的所有文件路径
     *
     * @param path
     */
    public static List<String> getFilePath(String path, String extension) {
        findFilePath(path, extension);
        return filePathList;
    }

    /**
     * 获取path目录下的所有文件路径
     *
     * @param path
     */
    private static void findFilePath(String path, String extension) {
        File[] files = new File(path).listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (int i = 0; i < files.length; ++i) {
            String filePath = files[i].getAbsolutePath();
            if (extension == null || filePath.endsWith("." + extension)) {
                filePathList.add(filePath);
            }
            if (files[i].isDirectory()) {
                findFilePath(files[i].getAbsolutePath(), extension);
            }
        }
    }


}
