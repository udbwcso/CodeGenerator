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

    public static ListingSpot getListingSpot(String key) {
        for (ListingSpot spot : ListingSpot.values()) {
            if (spot.getKey().equals(key)) {
                return spot;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
