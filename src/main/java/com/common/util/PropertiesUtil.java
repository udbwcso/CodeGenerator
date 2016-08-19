package com.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
}
