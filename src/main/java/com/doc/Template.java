package com.doc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Administrator on 2016/9/8.
 */
public enum Template {
    INSTANCE;


    private Properties properties;

    Template() {
        InputStream is = Template.class.getResourceAsStream("/doc/template.properties");
        try {
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTemplate(String key) throws Exception {
        if (properties == null || !properties.containsKey(key)) {
            throw new Exception("读取json数据模板异常");
        }
        return properties.getProperty(key);
    }

    public <T> Map<String, Object> toMap(T bean, String templateName) throws Exception {
        JSONObject jsonObject = new JSONObject(getTemplate(templateName));
        return toMap(bean, jsonObject);
    }


    public <T> Map<String, Object> toMap(T bean, JSONObject template) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, Object> objectMap = new LinkedHashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (!template.has(key)) {
                continue;
            }
            Object templateObj = template.get(key);
            if (templateObj instanceof JSONObject
                    || templateObj instanceof JSONArray) {
                objectMap.put(key, property.getReadMethod().invoke(bean));
            } else {
                property.getReadMethod();
                map.put(key, property.getReadMethod().invoke(bean));
            }
        }
        Iterator<String> it = objectMap.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            Object value = objectMap.get(key);
            Object templateObj = template.get(key);
            if (templateObj instanceof JSONObject) {
                Map<String, Object> jsonObjMap = toMap(value, template.getJSONObject(key));
                map.put(key, jsonObjMap);
            } else if (templateObj instanceof JSONArray) {
                List<Map<String, Object>> mapList = toList((List) value, (JSONArray) templateObj);
                map.put(key, mapList);
            }
        }
        return map;
    }

    public List<Map<String, Object>> toList(List list, JSONArray template) throws IllegalAccessException,
            IntrospectionException, InvocationTargetException {
        List<Map<String, Object>> mapList = new ArrayList<>();
        JSONObject templateObj = template.getJSONObject(0);
        for (int i = 0; i < list.size(); i++) {
            mapList.add(toMap(list.get(i), templateObj));
        }
        return mapList;
    }
}
