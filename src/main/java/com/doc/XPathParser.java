package com.doc;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class XPathParser {

    private XPath xpath;

    public XPathParser(){
        XPathFactory factory = XPathFactory.newInstance();
        this.xpath = factory.newXPath();
    }

    public String evalString(String expression, Object item) throws XPathExpressionException {
        return String.valueOf(evaluate(expression, item, XPathConstants.STRING));
    }

    public NodeList evalNodes(Object item, String expression) throws XPathExpressionException {
        return (NodeList) evaluate(expression, item, XPathConstants.NODESET);
    }

    public Node evalNode(Object item, String expression) throws XPathExpressionException {
        return (Node) evaluate(expression, item, XPathConstants.NODE);
    }

    private Object evaluate(String expression, Object item, QName returnType) throws XPathExpressionException {
        return xpath.evaluate(expression, item, returnType);
    }

}
