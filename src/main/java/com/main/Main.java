package com.main;

import com.code.Configuration;
import com.code.entity.EntityGenerator;
import com.common.Constants;
import com.common.util.PropertiesUtil;
import com.template.util.TemplateUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Administrator on 2016/7/28.
 */
public class Main {
    public static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws Exception {

        Properties tableProp = PropertiesUtil.load(Configuration.get("table"));
        Enumeration enumeration = tableProp.keys();
        while (enumeration.hasMoreElements()) {
            String key = String.valueOf(enumeration.nextElement());
            String entityName = tableProp.getProperty(key);
            Map<String, Object> tableInfo = EntityGenerator.getTableInfo(null, null, key, null, entityName);
            String code = TemplateUtil.getContent(tableInfo, Configuration.get("entity"));
            File codeFile = new File(entityName + ".java");
            log.info(entityName + ".java" + ":" + codeFile.getAbsolutePath());
            FileUtils.writeStringToFile(codeFile, code, Constants.DEFAULT_ENCODING);
        }
    }
}
