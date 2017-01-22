package com.stock.service;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;

import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
public interface DataService {
    List<Stock> getStockList(ListingSpot spot);

    void storeStockPriceData(List<StockPrice> priceList);
}
