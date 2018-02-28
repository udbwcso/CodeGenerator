package com.ymt.test;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class T {

    public static void main(String[] args) {
//        TreeMap treeMap = new TreeMap();
//        treeMap.put(null, "a");
//        System.out.println(treeMap.get(null));

//        Hashtable  hashtable = new Hashtable();
//        hashtable.put("a", null);

//        HashMap hashMap = new HashMap();
//        hashMap.put(null, "a");
//        hashMap.put("null", null);

        ConcurrentHashMap map = new ConcurrentHashMap();
//        map.put(null, "a");
        map.put("null", null);

        ArrayList arrayList = new ArrayList();
//        arrayList.subList();
    }
}
