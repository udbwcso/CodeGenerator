package com.result;

import com.doc.XPathParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.InputStream;
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
        builder.build();
    }

    public Map<String, ResultMap> build() throws Exception {
        NodeList beanNodeList = parser.evalNodes(root, "resultMap");
        Map<String, ResultMap> beanMap = new HashMap<>();
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
