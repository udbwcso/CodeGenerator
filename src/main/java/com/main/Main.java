package com.main;

import com.common.Constants;
import com.common.util.PropertiesUtil;
import com.code.entity.EntityGenerator;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/7/28.
 */
public class Main {
    public static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {

        Properties tableProp = PropertiesUtil.load("table.properties", "/properties/database/table.properties");
        Enumeration enumeration = tableProp.keys();
        while (enumeration.hasMoreElements()) {
            String key = String.valueOf(enumeration.nextElement());
            String entityName = tableProp.getProperty(key);
            String code = EntityGenerator.getEntityCode(null, null, key, null, entityName);
            File codeFile = new File(entityName + ".java");
            log.info(entityName + ".java" + ":" + codeFile.getAbsolutePath());
            FileUtils.writeStringToFile(codeFile, code, Constants.DEFAULT_ENCODING);
        }
    }
}
