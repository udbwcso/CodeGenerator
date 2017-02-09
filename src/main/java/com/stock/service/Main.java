package com.stock.service;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        StockDataService stockDataService = new FileStockDataServiceImpl();
        List<Stock> stockList = stockDataService.getStockList(ListingSpot.SZ);
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = stockList.get(i);
            List<StockPrice> priceList = stockDataService.getStockPriceList(stock);
            priceList.addAll(stockDataService.getStockPriceList(stock));

            CalculateService calculateService = new CalculateServiceImpl();
            Integer[] days = new Integer[]{5, 10, 20, 30};
            if (priceList.size() < days[days.length - 1] + days[0]) {
                continue;
            }
            calculateService.average(priceList, days, 3);
            for (int j = 0; j < 5; j++) {
                StockPrice price = priceList.get(j);
                BigDecimal[] result = new BigDecimal[days.length];
                for (int k = 0; k < days.length; k++) {
                    result[k] = priceList.get(j).getAverage().get(days[k]);
                }
                int cnt = 0;
                for (int k = 0; k < result.length; k++) {
                    BigDecimal sub = result[k].subtract(result[0]).abs();
                    BigDecimal deviate = sub.divide(priceList.get(0).getClosingPrice(), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    if (deviate.compareTo(new BigDecimal("0.05")) < 0) {
                        ++cnt;
                    }
                }
                if (cnt >= days.length - 1) {
                    for (BigDecimal average : result) {
                        System.out.print(average + " ");
                    }
                    System.out.println();
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(price.getDate());
                    System.out.println(stock.getCode() + "----" + date);
                }
            }
        }
    }
}