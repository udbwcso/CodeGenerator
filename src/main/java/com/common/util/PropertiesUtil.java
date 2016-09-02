package com.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.Properties;


public class PropertiesUtil {

    public static Properties load(InputStream is) throws IOException {
        Properties properties = new Properties();
        properties.load(is);
        return properties;
    }

    public static Properties load(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        Properties properties = load(is);
        is.close();
        return properties;
    }

    public static Properties load(Class clz, String propName) throws IOException {
        InputStream is = clz.getResourceAsStream(propName);
        Properties properties = load(is);
        is.close();
        return properties;
    }

    /**
     * PropertiesUtil.load("/properties/word.properties")
     * @param pkg
     * @return
     * @throws IOException
     */
    public static Properties load(String pkg) throws IOException {
        InputStream is = Properties.class.getResourceAsStream(pkg);
        Properties properties = load(is);
        is.close();
        return properties;
    }

    public static void store(Properties properties, String path, boolean append) throws IOException {
        OutputStream fos = new FileOutputStream(path, append);
        properties.store(fos, "");
        fos.close();
    }

    public static String getPropertyIgnoreCase(Properties properties, String key){
        String value = properties.getProperty(key);
        if(StringUtils.isNotEmpty(value)){
            return value;
        }
        value = properties.getProperty(key.toLowerCase());
        if(StringUtils.isNotEmpty(value)){
            return value;
        }
        return properties.getProperty(key.toUpperCase());
    }
}
