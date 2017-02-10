package com.stock.service;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
public interface StockDataService {

    List<Stock> getStockList() throws IOException, ParseException;

    List<Stock> getStockList(ListingSpot spot) throws IOException, ParseException;

    List<StockPrice> getStockPriceList(Stock stock) throws IOException, ParseException;


    /**
     * 取某一时间的数据
     * @param stock
     * @param startDate
     * @param endDate
     * @return
     * @throws IOException
     * @throws ParseException
     */
    List<StockPrice> getStockPriceList(Stock stock, Calendar startDate, Calendar endDate) throws IOException, ParseException;

    List<StockPrice> getStockPriceList(List<StockPrice> priceList, Calendar startDate, Calendar endDate);

    void storeStockPriceData(List<StockPrice> priceList);
}
