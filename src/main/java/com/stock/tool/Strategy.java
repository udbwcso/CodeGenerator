package com.stock.tool;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.strategy.AverageConvergenceServiceImpl;
import com.stock.strategy.StrategyService;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Administrator on 2017/1/23.
 */
public class Strategy {
    public static void main(String[] args) throws IOException, ParseException {
        Stock stock = new Stock();
//        stock.setCode("000001");
        stock.setCode("000651");
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(2016, 0, 1);
//        stock.setListingDate(calendar.getTime());
        stock.setSpot(ListingSpot.SZ);
        StrategyService strategyService = new AverageConvergenceServiceImpl();
        strategyService.test(stock);
    }
}
