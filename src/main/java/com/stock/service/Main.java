package com.stock.service;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
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
            List<StockPrice> priceList = stockDataService.getStockPriceList(stock, 2017);
            priceList.addAll(stockDataService.getStockPriceList(stock, 2016));
            stock.setPriceList(priceList);

            BigDecimal[] numbers = new BigDecimal[priceList.size()];
            for (int j = 0; j < priceList.size(); j++) {
                numbers[j] = priceList.get(j).getClosingPrice();
            }
            CalculateService calculateService = new CalculateServiceImpl();
            int[] days = new int[]{5, 10, 20, 30};
            if(numbers.length < days[days.length - 1]) {
                continue;
            }
            BigDecimal[] result = new BigDecimal[days.length];
            int cnt = 0;
            for (int j = 0; j < days.length; j++) {
                result[j] = calculateService.getAverage(numbers, days[j]);
                BigDecimal sub = priceList.get(0).getClosingPrice().subtract(result[j]);
                BigDecimal deviate = sub.abs().divide(result[j], 5, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
//                System.out.println(stock.getCode() + "---" + result[j].toString() + "----" + sub.toString() + "----" + deviate.toString());
                if(deviate.compareTo(new BigDecimal("0.5")) < 0) {
                    cnt++;
                }
            }
            if(cnt == days.length) {
                for (BigDecimal average : result) {
                    System.out.print(average + " ");
                }
                System.out.println();
                System.out.println(stock.getCode());
            }
        }
    }
}
