package com.result;


import org.apache.commons.lang3.time.StopWatch;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/11/7.
 */
public class T {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        User u = User.class.newInstance();
        u.getId();
        User user = new User();
        user.getId();

        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < 1000; i++) {
            user.getId();
        }
        watch.stop();
        System.out.println(watch.getNanoTime());
        watch.reset();

        watch.start();
        for (int i = 0; i < 1000; i++) {
            u.getId();
        }
        watch.stop();
        System.out.println(watch.getNanoTime());
        watch.reset();

    }


    public static void main1(String[] args) throws IntrospectionException, InvocationTargetException,
            IllegalAccessException {
////        User user = new User();
////        user.setId(11L);
////        User u = new User();
////        BeanUtils.copyProperties(user, u);
////        System.out.println(u.getId());
//        BeanInfo beanInfo1 = Introspector.getBeanInfo(User.class);
//        PropertyDescriptor[] propertyDescriptors1 = beanInfo1.getPropertyDescriptors();
//        BeanUtils.getPropertyDescriptors(User.class);
//        long start = System.nanoTime();
//        Map<Class, PropertyDescriptor[]> map = new HashMap<>();
//        for (int i = 0; i < 100; i++) {
////            PropertyDescriptor[] propertyDescriptors = map.get(User.class);
////            if(propertyDescriptors == null) {
//                BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
//            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
////                map.put(User.class, propertyDescriptors);
////            }
////            propertyDescriptors[0].getReadMethod();
////            System.out.println(propertyDescriptors.length);
//        }
//        long mid = System.nanoTime();
//        for (int i = 0; i < 100; i++) {
//            PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(User.class);
////            System.out.println(propertyDescriptors.length);
////            propertyDescriptors[0].getReadMethod();
//        }
//        long end = System.nanoTime();
//        System.out.println(mid - start);
//        System.out.println(end - mid);

        User user = new User();
        user.setId(11L);
        StopWatch watch = new StopWatch();
        watch.start();
        for (int i = 0; i < 1000; i++) {
            user.getId();
        }
        watch.stop();
        System.out.println(watch.getNanoTime());
        watch.reset();

        BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
        Method method = null;
        for (int i = 0; i < methodDescriptors.length; i++) {
            if (methodDescriptors[i].getName().equals("getId")) {
                method = methodDescriptors[i].getMethod();
                method.setAccessible(true);
                method.invoke(user);
            }
        }


        watch.start();
        for (int i = 0; i < 1000; i++) {
            method.invoke(user);
        }
        watch.stop();
        System.out.println(watch.getNanoTime());
        watch.reset();
    }
}
