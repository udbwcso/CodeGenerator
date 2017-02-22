package com.stock.tool;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.strategy.AverageServiceImpl;
import com.stock.strategy.StrategyService;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Administrator on 2017/1/23.
 */
public class PriceStrategy {
    public static void main(String[] args) throws IOException, ParseException {
        Stock stock = new Stock();
        stock.setCode("000001");
        stock.setSpot(ListingSpot.SZ);
//        StrategyService strategyService = new PriceStrategyServiceImpl();
        StrategyService strategyService = new AverageServiceImpl();
        strategyService.test(stock);
    }
}
