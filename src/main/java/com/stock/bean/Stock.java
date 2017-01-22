package com.stock.bean;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/19.
 */
public class Stock {
    private String name;
    private String code;
    private Date listingDate;
    private ListingSpot spot;

    private List<StockPrice> priceList;

    public List<StockPrice> getPriceList() {
        return priceList;
    }

    public void setPriceList(List<StockPrice> priceList) {
        this.priceList = priceList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ListingSpot getSpot() {
        return spot;
    }

    public void setSpot(ListingSpot spot) {
        this.spot = spot;
    }

    public Date getListingDate() {
        return listingDate;
    }

    public void setListingDate(Date listingDate) {
        this.listingDate = listingDate;
    }
}
