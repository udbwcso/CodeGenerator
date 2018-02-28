package com.doc;

import com.doc.bean.Field;
import com.doc.bean.ResultBean;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        XPathExpression expr = xpath.compile("/mapper");
        Document document = builder.parse(inputStream);
        this.root = (Node) expr.evaluate(document, XPathConstants.NODE);
        this.parser = new XPathParser();
        inputStream.close();
    }



    public Map<String, ResultBean> build() throws Exception {
        NodeList beanNodeList = parser.evalNodes(root, "resultMap");
        Map<String, ResultBean> beanMap = new HashMap<>();
        for (int i = 0; i < beanNodeList.getLength(); i++) {
            Node bean = beanNodeList.item(i);
            Map beanAttrMap = getAttributes(bean);
            ResultBean resultBean = toBean(ResultBean.class, beanAttrMap);
            NodeList fieldNodeList = parser.evalNodes(bean, "result");
            List<Field> fieldList = new ArrayList<>();
            for (int j = 0; j < fieldNodeList.getLength(); j++) {
                Node fieldName = fieldNodeList.item(j);
                Map fieldAttrMap = getAttributes(fieldName);
                Field field = toBean(Field.class, fieldAttrMap);
                fieldList.add(field);
            }
            resultBean.setFieldList(fieldList);
            beanMap.put(resultBean.getId(), resultBean);
        }
        return beanMap;
    }


    private Map<String, String> getAttributes(Node node){
        NamedNodeMap attributeNodes = node.getAttributes();
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < attributeNodes.getLength(); i++) {
            Node attribute = attributeNodes.item(i);
            map.put(attribute.getNodeName(), attribute.getNodeValue());
        }
        return map;
    }

    public static <T> T toBean(Class<T> clz, Map<String, Object> map) throws Exception {
        T bean = clz.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(clz);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if(map.containsKey(key)){
                property.getWriteMethod().invoke(bean, map.get(key));
            }
        }
        return bean;
    }

}
