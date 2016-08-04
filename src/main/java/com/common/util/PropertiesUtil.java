package com.common.util;

import java.io.*;
import java.util.Properties;

/**
 * Created by udbwcso on 2015/7/14.
 */
public class PropertiesUtil {

    public static Properties loadFile(String propPath) throws IOException {
        InputStream is = null;
        Properties properties = new Properties();
        try {
            is = new FileInputStream(propPath);
            properties.load(is);
            return properties;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static Properties load(Class clz, String propName) throws IOException {
        InputStream is = clz.getResourceAsStream(propName);
        Properties properties = new Properties();
        try {
            properties.load(is);
            return properties;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public static Properties load(String pkg) throws IOException {
        InputStream is = Properties.class.getResourceAsStream (pkg);
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return properties;
    }

    public static Properties load(String fileName, String defaultProp) throws IOException {
        InputStream is = null;
        try {
            File file = new File(fileName);
            if(file.exists()){
                is = new FileInputStream(file);
            } else {
                is = Properties.class.getResourceAsStream(defaultProp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return properties;
    }
}
