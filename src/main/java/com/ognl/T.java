package com.ognl;

import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;

/**
 * Created by Administrator on 2016/9/28.
 */
public class T {
    public static void main(String[] args) throws OgnlException {

        Person person = new Person();
        person.setUsername("113");
        person.setPassword("22");

        OgnlContext context = new OgnlContext();
        // OGNL实现了MAP接口
        context.put("person", person);
        context.setRoot(person);// 设置唯一的根对象
        Object object = Ognl.parseExpression("name");// 解析字符串，若没有#，则到根对象找。#是明确告诉OGNL从哪个对象中找。调用getName()方法
        System.out.println(object);// 显示name
        Object object2 = Ognl.getValue(object, context, context.getRoot());
        System.out.println(object2);
        System.out.println("-------------------");
    }


}
