package com.stock.service;

import com.stock.bean.Stock;
import com.stock.bean.StockPrice;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/23.
 */
public class AverageStrategyServiceImpl implements StrategyService {
    //    @Override
    public void test1(Stock stock) throws IOException, ParseException {
        StockDataService stockDataService = new FileStockDataServiceImpl();
        List<StockPrice> priceList = stockDataService.getStockPriceList(stock);
        CalculateService calculateService = new CalculateServiceImpl();
        Integer[] days = new Integer[]{5, 20};
        calculateService.average(priceList, days, 3);
        boolean isBuy = false;
        //-1:初始化;0:等于;1:小于;2:大于;
        BigDecimal principal = new BigDecimal(10000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = priceList.size() - 22; i >= 1; i--) {
            StockPrice yesterday = priceList.get(i + 1);
            StockPrice today = priceList.get(i);
            Map<Integer, BigDecimal> yesterdayAverage = yesterday.getAverage();
            Map<Integer, BigDecimal> todayAverage = today.getAverage();
            if (yesterdayAverage.get(days[0]).compareTo(yesterdayAverage.get(days[1])) >= 0
                    && todayAverage.get(days[0]).compareTo(todayAverage.get(days[1])) < 0) {
                if (isBuy) {
                    System.out.println(today.getClosingPrice() + "--" + today.getAverage().get(days[0]) + "--" + isBuy);

                    //sell
                    BigDecimal sell = today.getLowestPrice().multiply(new BigDecimal(200));
                    principal = principal.add(sell).subtract(sell.multiply(new BigDecimal("0.01")));
                    isBuy = false;
                    System.out.println(sdf.format(today.getDate()) + " sell:" + sell.toString());
                }
            } else if (yesterdayAverage.get(days[0]).compareTo(yesterdayAverage.get(days[1])) <= 0
                    && todayAverage.get(days[0]).compareTo(todayAverage.get(days[1])) > 0) {
                if (!isBuy) {
                    //buy
                    BigDecimal buy = today.getHighestPrice().multiply(new BigDecimal(200));
                    if (principal.compareTo(buy) > 0 && !isBuy) {
                        System.out.println(today.getClosingPrice() + "--" + today.getAverage().get(days[0]) + "--" + isBuy);

                        principal = principal.subtract(buy).subtract(buy.multiply(new BigDecimal("0.01")));
                        isBuy = true;
                        System.out.println(sdf.format(today.getDate()) + " buy:" + buy.toString());
                    }
                }
            }
        }
        System.out.println(principal.toString());
    }

    @Override
    public void test(Stock stock) throws IOException, ParseException {
        StockDataService stockDataService = new FileStockDataServiceImpl();
        List<StockPrice> priceList = stockDataService.getStockPriceList(stock);
        CalculateService calculateService = new CalculateServiceImpl();
        Integer[] days = new Integer[]{5, 10, 20, 30};
        calculateService.average(priceList, days, 3);
        boolean isBuy = false;
        BigDecimal principal = new BigDecimal(20000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int buyCount = 0;
        for (int i = priceList.size() - 32; i >= 1; i--) {
            StockPrice today = priceList.get(i);
            BigDecimal[] result = new BigDecimal[days.length];
            for (int j = 0; j < days.length; j++) {
                result[j] = today.getAverage().get(days[j]);
            }
            Arrays.sort(result);
            int cnt = 0;
            for (int j = 0; j < result.length - 1; j++) {
                BigDecimal sub = result[j].subtract(result[j + 1]).abs();
                BigDecimal deviate = sub.divide(result[j], 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                if (deviate.compareTo(new BigDecimal("0.5")) < 0) {
                    ++cnt;
                }
            }
            if (cnt >= days.length - 2 && today.getClosingPrice().compareTo(result[3]) >= 0) {
                //buy
                BigDecimal buy = today.getHighestPrice().multiply(new BigDecimal(200));
                isBuy = true;
                System.out.println(today.getClosingPrice() + "--" + today.getAverage().get(days[0]) + "--" + isBuy);
                principal = principal.subtract(buy).subtract(buy.multiply(new BigDecimal("0.01")));
                buyCount = buyCount + 200;
                System.out.println(sdf.format(today.getDate()) + " buy:" + buy.toString());
                System.out.println();
            }
        }

        for (int i = priceList.size() - 32; i >= 1; i--) {
            StockPrice today = priceList.get(i);
            BigDecimal[] result = new BigDecimal[days.length];
            for (int j = 0; j < days.length; j++) {
                result[j] = today.getAverage().get(days[j]);
            }
            if(result[0].compareTo(result[2]) == 0) {
                System.out.println(sdf.format(today.getDate()) + " sell");
            }
        }
    }
}
