package com.code;

import com.common.util.PropertiesUtil;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Administrator on 2016/8/24.
 */
public class Configuration {
    private static Properties properties;
    static {
        try {
            properties = PropertiesUtil.load("/properties/config.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
