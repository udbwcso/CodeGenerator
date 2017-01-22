package com.stock.service;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
public interface StockDataService {
    List<Stock> getStockList(ListingSpot spot) throws IOException, ParseException;

    List<StockPrice> getStockPriceList(Stock stock, int year, int quarter) throws IOException, ParseException;

    List<StockPrice> getStockPriceList(Stock stock, int year) throws IOException, ParseException;

    void storeStockPriceData(List<StockPrice> priceList);
}
