package com.stock.tool;

import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import com.stock.service.StockDataFileReader;
import com.stock.service.StockDataReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/2.
 */
public class ChooseTrendStock {
    public static void main(String[] args) throws IOException, ParseException {
        StockDataReader stockDataService = new StockDataFileReader();
        List<Stock> stockList = stockDataService.getStockList();
        int trendDays = 3;
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = stockList.get(i);
            List<StockPrice> priceList = stockDataService.getStockPriceList(stock);
            if(resistance(priceList.subList(0, trendDays), new BigDecimal("0.01"))){
                System.out.println(stock.getCode());
            }
        }
    }

    public static boolean resistance(List<StockPrice> priceList, BigDecimal range) {
        List<BigDecimal> numberList = new ArrayList<>();
        for (StockPrice price : priceList) {
            numberList.add(price.getOpeningPrice());
            numberList.add(price.getClosingPrice());
            numberList.add(price.getLowestPrice());
            numberList.add(price.getHighestPrice());
        }
        BigDecimal num = BigDecimal.ZERO;
        int maxCount = 0;
        for (int i = 0; i < numberList.size(); i++) {
            int count = 0;
            for (int j = 0; j < numberList.size(); j++) {
                if(numberList.get(i).subtract(numberList.get(j)).abs().compareTo(range) <= 0) {
                    ++count;
                }
            }
            if(count > maxCount) {
                maxCount = count;
                num = numberList.get(i);
            }
        }
        return maxCount >= priceList.size() * 2;
    }
}
