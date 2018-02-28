package com.doc.adoc;


import com.common.util.PropertiesUtil;
import com.database.util.TableUtil;
import com.doc.XMLConfigBuilder;
import com.doc.bean.ResultBean;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/27.
 */
public class T {
    public static void main(String[] args) throws Exception {
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder("doc/ProductLabelMapper.xml");
        Map<String, ResultBean> resultBeanMap = xmlConfigBuilder.build();
        Properties properties = PropertiesUtil.load("/doc/mapper.properties");
        List<Map<String, Object>> columnList = new ArrayList<>();
        for (Map.Entry<String, ResultBean> entry : resultBeanMap.entrySet()) {
            String tableName = properties.getProperty(entry.getKey());
            columnList.addAll(TableUtil.getColumns(null, null, tableName, null));
        }



        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        Bindings bind = engine.createBindings();
        bind.put("json", "{\"code\":200,\"msg\":\"操作成功\",\"result\":{\"uId\":100067,\"identity\":\"1\",\"token\":\"122118404b79c30db97d645544f8239d\"}}");
        engine.setBindings(bind, ScriptContext.ENGINE_SCOPE);
        Object format = engine.eval("JSON.stringify(JSON.parse(json), null, '  ')", bind);
        String formatString = String.valueOf(format);
        String[] lines = formatString.split("\n");
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        for (String line : lines) {
            System.out.println(line);
            Matcher matcher = pattern.matcher(line);
            String key = null;
            if (matcher.find()) {
                key = matcher.group(1);
                if(line.endsWith(",") || line.endsWith("\"")) {
                    System.out.println(key);
                }
            }
        }
    }

}
