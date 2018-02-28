package com.result;

import com.doc.XPathParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Administrator on 2016/9/14.
 */
public class XMLConfigBuilder {

    private XPathParser parser;

    private Node root;


    public XMLConfigBuilder(String resource) throws Exception {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        InputStream inputStream = XMLConfigBuilder.class.getClassLoader().getResourceAsStream(resource);
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr = xpath.compile("/result");
        Document document = builder.parse(inputStream);
        this.root = (Node) expr.evaluate(document, XPathConstants.NODE);
        this.parser = new XPathParser();
        inputStream.close();
    }

    public static void main(String[] args) throws Exception {
        XMLConfigBuilder builder = new XMLConfigBuilder("doc/result.xml");
        Map<String, ResultMap> map = builder.build();
        User user = new User();
        Child child = new Child();
        user.setId(1L);
        child.setName("99");
        user.setChild(child);
//        System.out.println(new JSONObject(builder.toMap(user, "1")).toString());
    }

    private Map<String, ResultMap> beanMap = new HashMap<>();

//    public static <T> BeanMapInfo read(T bean, JSONObject jsonObject) throws IllegalAccessException, IntrospectionException,
//            InvocationTargetException {
//        Stack<BeanMapInfo> stack = new Stack<>();
//        BeanMapInfo basicMap = toMap(bean, jsonObject);
//        stack.push(basicMap);
//        while (!stack.isEmpty()) {
//            BeanMapInfo beanInfo = stack.peek();
//            if (beanInfo.isFinish()) {
//                stack.pop();
//                continue;
//            }
//            Map<String, Object> value = beanInfo.getValue();
//            Map<String, Object> objMap = beanInfo.getObjectMap();
//            Iterator<String> it = objMap.keySet().iterator();
//            while (it.hasNext()) {
//                String key = it.next();
//                BeanMapInfo vb = toMap(objMap.get(key), beanInfo.getTemplate().getJSONObject(key));
//                Map<String, Object> objectValue = vb.getValue();
//                value.put(key, objectValue);
//                stack.push(beanInfo);
//            }
//
//            Map<String, Object> arrMap = beanInfo.getArrayMap();
//            it = arrMap.keySet().iterator();
//            while (it.hasNext()) {
//                String key = it.next();
//                List<Map<String, Object>> list = new ArrayList<>();
//                List valueList = (List) arrMap.get(key);
//                for (int i = 0; i < valueList.size(); i++) {
//                    Object v = valueList.get(i);
//                    BeanMapInfo vb = toMap(v, beanInfo.getTemplate().getJSONArray(key)
//                            .getJSONObject(0));
//                    Map<String, Object> objectValue = vb.getValue();
//                    list.add(objectValue);
//                    stack.push(beanInfo);
//                }
//                value.put(key, list);
//            }
//            beanInfo.setFinish(true);
//        }
//        return basicMap;
//    }
//
//    public <T> Map<String, Object> toMap(T bean, String templateName) throws
//            IntrospectionException,
//            InvocationTargetException, IllegalAccessException {
//        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
//        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//        boolean finish = true;
//        Map<String, Object> value = new HashMap<>();
//        for (PropertyDescriptor property : propertyDescriptors) {
//            String key = property.getName();
//            if (!template.has(key)) {
//                continue;
//            }
//            Object templateObj = template.get(key);
//            if (templateObj instanceof JSONObject) {
//                finish = false;
//                objMap.put(key, property.getReadMethod().invoke(bean));
//            } else if (templateObj instanceof JSONArray) {
//                finish = false;
//                arrMap.put(key, property.getReadMethod().invoke(bean));
//            } else {
//                value.put(key, property.getReadMethod().invoke(bean));
//            }
//        }
//        BeanMapInfo info = new BeanMapInfo();
//        info.setValue(value);
//        info.setObjectMap(objMap);
//        info.setArrayMap(arrMap);
//        info.setFinish(finish);
//        info.setTemplate(template);
//        return info;
//    }

    public <T> Map<String, Object> toMap(T bean, ResultMap resultMap) throws IntrospectionException, InvocationTargetException,
            IllegalAccessException {
        Map<String, Object> valueMap = new LinkedHashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        List<ResultField> fieldList = resultMap.getFieldList();
        for (ResultField field : fieldList) {
            Method readMethod = null;
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                if(field.getProperty().equals(propertyDescriptor.getName())) {
                    readMethod = propertyDescriptor.getReadMethod();
                    readMethod.setAccessible(true);
                    break;
                }
            }
            String key = field.getAlias();
            if(key == null || key.equals("")) {
                key = field.getProperty();
            }
            if (FieldType.OBJECT.getCode().equalsIgnoreCase(field.getType())) {
//                Map<String, Object> value = toMap(readMethod.invoke(bean), beanMap.get(field.getResultMapId()));
//                valueMap.put(key, value);
            } else if (FieldType.ARRAY.getCode().equalsIgnoreCase(field.getType())) {
//                List list = toList((List) readMethod.invoke(bean), beanMap.get(field.getResultMapId()));
//                valueMap.put(key, list);
            } else {
                valueMap.put(key, readMethod.invoke(bean));
            }
        }
        return valueMap;
    }

//
//    public <T> Map<String, Object> toMap(T bean, ResultMap resultMap) throws IntrospectionException, InvocationTargetException,
//            IllegalAccessException {
//        Map<String, Object> valueMap = new LinkedHashMap<>();
//        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
//        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//        List<ResultField> fieldList = resultMap.getFieldList();
//        for (ResultField field : fieldList) {
//            Method readMethod = null;
//            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
//                if(field.getProperty().equals(propertyDescriptor.getName())) {
//                    readMethod = propertyDescriptor.getReadMethod();
//                    readMethod.setAccessible(true);
//                }
//            }
//            String key = field.getAlias();
//            if(key == null || key.equals("")) {
//                key = field.getProperty();
//            }
//            if (FieldType.OBJECT.name().equalsIgnoreCase(field.getType())) {
//                Map<String, Object> value = toMap(readMethod.invoke(bean), beanMap.get(field.getResultMapId()));
//                valueMap.put(key, value);
//            } else if (FieldType.ARRAY.name().equalsIgnoreCase(field.getType())) {
//                List list = toList((List) readMethod.invoke(bean), beanMap.get(field.getResultMapId()));
//                valueMap.put(key, list);
//            } else {
//                valueMap.put(key, readMethod.invoke(bean));
//            }
//        }
//        return valueMap;
//    }

//
//    public List<Map<String, Object>> toList(List list, ResultMap resultMap) throws IllegalAccessException,
//            IntrospectionException, InvocationTargetException {
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            mapList.add(toMap(list.get(i), resultMap));
//        }
//        return mapList;
//    }


    public Map<String, ResultMap> build() throws Exception {
        NodeList beanNodeList = parser.evalNodes(root, "resultMap");
//        Map<String, ResultMap> beanMap = new HashMap<>();
        for (int i = 0; i < beanNodeList.getLength(); i++) {
            Node node = beanNodeList.item(i);
            ResultMap resultMap = parseResultMap(node);
            beanMap.put(resultMap.getId(), resultMap);
        }
        return beanMap;
    }

    private ResultMap parseResultMap(Node node) throws XPathExpressionException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        NamedNodeMap nodeMap = node.getAttributes();
        ResultMap resultMap = new ResultMap();
        resultMap.setId(nodeMap.getNamedItem("id").getNodeValue());
        String handler = nodeMap.getNamedItem("resultHandler").getNodeValue();
        Class<ResultHandler> cls = (Class<ResultHandler>) Class.forName(handler);
        ResultHandler resultHandler = cls.newInstance();
        resultMap.setResultHandler(resultHandler);
        resultMap.setFieldList(parseFields(node));
        return resultMap;
    }

    private List<ResultField> parseFields(Node parent) throws XPathExpressionException {
        NodeList nodeList = parser.evalNodes(parent, "result");
        List<ResultField> fieldList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Properties properties = parseAttributes(node);
            ResultField field = new ResultField();
            field.setProperty(properties.getProperty("property"));
            field.setAlias(properties.getProperty("alias"));
            field.setResultMapId(properties.getProperty("resultMap"));
            field.setType(properties.getProperty("type"));
            fieldList.add(field);
        }
        return fieldList;
    }

    private Properties parseAttributes(Node n) {
        Properties attributes = new Properties();
        NamedNodeMap attributeNodes = n.getAttributes();
        if (attributeNodes == null) {
            return attributes;
        }
        for (int i = 0; i < attributeNodes.getLength(); i++) {
            Node attribute = attributeNodes.item(i);
            attributes.put(attribute.getNodeName(), attribute.getNodeValue());
        }
        return attributes;
    }



//    public Map<String, ResultBean> build() throws Exception {
//        NodeList beanNodeList = parser.evalNodes(root, "resultMap");
//        Map<String, ResultBean> beanMap = new HashMap<>();
//        for (int i = 0; i < beanNodeList.getLength(); i++) {
//            Node bean = beanNodeList.item(i);
//            Map beanAttrMap = getAttributes(bean);
//            ResultBean resultBean = toBean(ResultBean.class, beanAttrMap);
//            NodeList fieldNodeList = parser.evalNodes(bean, "result");
//            List<Field> fieldList = new ArrayList<>();
//            for (int j = 0; j < fieldNodeList.getLength(); j++) {
//                Node fieldName = fieldNodeList.item(j);
//                Map fieldAttrMap = getAttributes(fieldName);
//                Field field = toBean(Field.class, fieldAttrMap);
//                fieldList.add(field);
//            }
//            resultBean.setFieldList(fieldList);
//            beanMap.put(resultBean.getId(), resultBean);
//        }
//        return beanMap;
//    }


//    private Map<String, String> getAttributes(Node node) {
//        NamedNodeMap attributeNodes = node.getAttributes();
//        Map<String, String> map = new HashMap<>();
//        for (int i = 0; i < attributeNodes.getLength(); i++) {
//            Node attribute = attributeNodes.item(i);
//            map.put(attribute.getNodeName(), attribute.getNodeValue());
//        }
//        return map;
//    }
//
//    public <T> T toBean(Class<T> clz, Map<String, Object> map) throws Exception {
//        T bean = clz.newInstance();
//        BeanInfo beanInfo = Introspector.getBeanInfo(clz);
//        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//        Iterator<String> it = map.keySet().iterator();
//        while (it.hasNext()) {
//            String key = it.next();
//            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
//                if(key.equals(propertyDescriptor.getName())) {
//                    System.out.println(key);
//                    System.out.println(propertyDescriptor.getWriteMethod());
//                    propertyDescriptor.getWriteMethod().invoke(bean, map.get(key));
//                }
//            }
//        }
//        return bean;
//    }

}
