package com.stock.bean;

/**
 * Created by Administrator on 2017/1/22.
 */
public enum ListingSpot {
    SH("sh", "上海"),
    SZ("sz", "深圳");

    private String key;
    private String name;

    ListingSpot(String key, String name){
        this.key = key;
        this.name = name;
    }
}
