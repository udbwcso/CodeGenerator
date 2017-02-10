package com.stock.service;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Administrator on 2017/1/23.
 */
public class Strategy {
    public static void main(String[] args) throws IOException, ParseException {
        Stock stock = new Stock();
//        stock.setCode("000001");
        stock.setCode("600030");
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2016, 0, 1);
//        stock.setListingDate(calendar.getTime());
        stock.setSpot(ListingSpot.SH);
        StrategyService strategyService = new AverageStrategyServiceImpl();
        strategyService.test(stock);
    }
}
