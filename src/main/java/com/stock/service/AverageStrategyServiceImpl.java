package com.stock.service;

import com.stock.bean.Stock;
import com.stock.bean.StockPrice;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/23.
 */
public class AverageStrategyServiceImpl implements StrategyService {

    @Override
    public void test(Stock stock) throws IOException, ParseException {
        StockDataReader stockDataService = new StockDataFileReader();
        List<StockPrice> priceList = stockDataService.getStockPriceList(stock);
        CalculateService calculateService = new CalculateServiceImpl();
        Integer[] days = new Integer[]{5, 10, 20, 30};
        calculateService.average(priceList, days, 3);

        List<StockPrice> keyPointList = new ArrayList<>();
        for (int i = 300; i >= 2; i--) {
            StockPrice price = priceList.get(i);
            BigDecimal[] result = new BigDecimal[days.length];
            for (int j = 0; j < days.length; j++) {
                result[j] = price.getAverage().get(days[j]);
            }
            //均线收敛
            int cnt = 0;
            for (int j = 0; j < result.length; j++) {
                BigDecimal sub = result[j].subtract(result[0]).abs();
                BigDecimal deviate = sub.divide(priceList.get(0).getClosingPrice(), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                if (deviate.compareTo(new BigDecimal("0.5")) < 0) {
                    ++cnt;
                }
            }
            if (cnt >= days.length - 1) {
//                keyPointList.add(nextDayPrice);
                //均线收敛后观察15天后续走势
                int divergentDays = 0;//连续发散天数
                for (int j = 1; j < 15; j++) {
                    int divergentCnt = 0;
                    --i;
                    StockPrice latestPrice = priceList.get(i);
                    for (int k = 0; k < days.length - 1; k++) {
                        if(latestPrice.getAverage().get(days[k]).compareTo(latestPrice.getAverage().get(days[k + 1])) > 0) {
                            divergentCnt++;
                        }
                    }
                    if(divergentCnt >= days.length - 1) {
                        divergentDays++;
                    } else {
                        divergentDays = 0;
                    }
                    if(divergentDays >= 2) {
                        keyPointList.add(latestPrice);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        System.out.println(sdf.format(latestPrice.getDate()));
                        break;
                    }
                }
            }
        }

        BigDecimal initMoney = new BigDecimal(20000);
        int positions = 0;
        BigDecimal positionsMoney = new BigDecimal(0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = priceList.size() - 1; i >= 0; i--) {
            StockPrice price = priceList.get(i);
            boolean isBuy = false;
            for (int j = 0; j < keyPointList.size(); j++) {
                if(price.getDate().equals(keyPointList.get(j).getDate())) {
                    //buy
                    BigDecimal buy = price.getHighestPrice().multiply(new BigDecimal(200));
                    if(initMoney.compareTo(buy) >= 0) {
                        positions = positions + 200;
                        initMoney = initMoney.subtract(buy).subtract(buy.multiply(new BigDecimal("0.003")));
                        isBuy = true;
                        positionsMoney = positionsMoney.add(buy);
                        System.out.println("buy" + "--" + sdf.format(price.getDate()) + "--" + positions + "--" + initMoney.toString());
                    }
                }
            }
            //当天买的不能当天卖
            if(isBuy) {
                continue;
            }
            BigDecimal lossPercent = BigDecimal.ZERO;
            if(positions > 0) {
                lossPercent = BigDecimal.ONE.subtract(positionsMoney.divide(new BigDecimal(positions), 6, BigDecimal.ROUND_HALF_UP).divide(price.getClosingPrice(), 6, BigDecimal.ROUND_HALF_UP));
            }
            if(lossPercent.compareTo(BigDecimal.ZERO) != 0) {
                System.out.println(lossPercent.toString());
            }
            if(positions > 0 && price.getAverage().get(days[0]).compareTo(price.getAverage().get(days[2])) == -1) {
                //sell
                BigDecimal sell = price.getLowestPrice().multiply(new BigDecimal(positions));
                initMoney = initMoney.add(sell).subtract(sell.multiply(new BigDecimal("0.003")));
                positions = 0;
                positionsMoney = BigDecimal.ZERO;
                System.out.println("sell" + "--" + sdf.format(price.getDate()) + "--" + positions + "--" + initMoney.toString());
                System.out.println(lossPercent.toString());
            }
            if(positions > 100 && lossPercent.abs().compareTo(new BigDecimal(0.06)) >= 0) {
                //sell
                BigDecimal sell = price.getLowestPrice().multiply(new BigDecimal(100));
                initMoney = initMoney.add(sell).subtract(sell.multiply(new BigDecimal("0.003")));
                positions = positions - 100;
                positionsMoney = positionsMoney.subtract(sell);
                System.out.println("sell" + "--" + sdf.format(price.getDate()) + "--" + positions + "--" + initMoney.toString());
                System.out.println(lossPercent.toString());
            }
        }
        System.out.println(initMoney.toString() + "--" + positionsMoney.toString());
        System.out.println(positions);
    }
}
