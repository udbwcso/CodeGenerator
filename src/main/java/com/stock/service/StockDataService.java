package com.stock.service;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
public interface StockDataService {
    List<Stock> getStockList(ListingSpot spot) throws IOException, ParseException;

    List<StockPrice> getStockPriceList(Stock stock, int year, int quarter) throws IOException, ParseException;

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
    List<StockPrice> getStockPriceList(Stock stock, Date startDate, Date endDate) throws IOException, ParseException;

    List<StockPrice> getStockPriceList(List<StockPrice> priceList, Date startDate, Date endDate);


    List<StockPrice> getStockPriceList(Stock stock, int year) throws IOException, ParseException;

    void storeStockPriceData(List<StockPrice> priceList);
}
