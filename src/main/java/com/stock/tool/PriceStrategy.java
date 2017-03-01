package com.stock.tool;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import com.stock.service.CalculateService;
import com.stock.service.CalculateServiceImpl;
import com.stock.service.StockDataFileReader;
import com.stock.service.StockDataReader;
import com.stock.util.StockPriceUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/23.
 */
public class PriceStrategy {
    public static void main(String[] args) throws IOException, ParseException {
        Stock stock = new Stock();
        stock.setCode("000001");
        stock.setSpot(ListingSpot.SZ);
//        StrategyService strategyService = new PriceStrategyServiceImpl();
//        StrategyService strategyService = new AverageServiceImpl();
//        strategyService.test(stock);
        test(stock);
    }

    public static void test(Stock stock) throws IOException, ParseException {
        StockDataReader reader = new StockDataFileReader();
        List<StockPrice> priceList = reader.getStockPriceList(stock);
        Integer[] days = new Integer[]{5, 10, 20, 30};
        CalculateService calculateService = new CalculateServiceImpl();
        calculateService.average(priceList, days, 3);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int riseDay = 2;
        BigDecimal initMoney = new BigDecimal(20000);
        BigDecimal positionMoney = new BigDecimal(0);
        BigDecimal positionCount = new BigDecimal(0);
        BigDecimal buyCount = new BigDecimal(100);
        for (int i = priceList.size() - days[days.length - 1] - 2; i >= 0; i--) {
            StockPrice current = priceList.get(i);
            List<StockPrice> risePriceList = new ArrayList<>();
            for (int j = 1; j <= riseDay + 1; j++) {
                risePriceList.add(priceList.get(i + j));
            }
            Map<Integer, BigDecimal> averageMap = priceList.get(i + 1).getAverage();
            BigDecimal[] averages = averageMap.values().toArray(new BigDecimal[0]);
            if (StockPriceUtil.isContinuousRise(2, risePriceList)
                    && StockPriceUtil.isConvergent(averages, new BigDecimal(0.3))) {
                //buy
                BigDecimal openBuyMoney = current.getOpeningPrice().multiply(buyCount);
                BigDecimal closeBuyMoney = current.getClosingPrice().multiply(buyCount);
                initMoney = initMoney.subtract(openBuyMoney).subtract(closeBuyMoney).subtract(new BigDecimal(15));
                positionCount = positionCount.add(buyCount).add(buyCount);
                positionMoney = positionMoney.add(openBuyMoney).add(closeBuyMoney);
                System.out.println("buy---" + sdf.format(priceList.get(i).getDate()) + "---" + initMoney.add(positionMoney));
                continue;
            }
            if (positionCount.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            //sell
            BigDecimal cost = positionMoney.divide(positionCount, 6, BigDecimal.ROUND_HALF_UP);
            BigDecimal percent = cost.subtract(current.getOpeningPrice()).abs().divide(cost, 6, BigDecimal.ROUND_HALF_UP);
            if (cost.compareTo(current.getOpeningPrice()) < 0
                    && percent.compareTo(new BigDecimal(0.06)) >= 0) {//盈利
                BigDecimal sellMoney = positionCount.multiply(current.getOpeningPrice());
                initMoney = initMoney.add(sellMoney);
                positionCount = BigDecimal.ZERO;
                positionMoney = BigDecimal.ZERO;
                System.out.println("sell---盈利---" + sdf.format(priceList.get(i).getDate()) + "---" + initMoney);
            } else if (cost.compareTo(current.getOpeningPrice()) > 0
                    && percent.compareTo(new BigDecimal(0.04)) >= 0) {//亏损
                BigDecimal sellMoney = positionCount.multiply(current.getOpeningPrice());
                initMoney = initMoney.add(sellMoney).subtract(new BigDecimal(10));
                positionCount = BigDecimal.ZERO;
                positionMoney = BigDecimal.ZERO;
                System.out.println("sell---亏损---" + sdf.format(priceList.get(i).getDate()) + "---" + initMoney);
            }

            if (current.getAverage().get(days[3]).compareTo(current.getOpeningPrice()) < 0
                    && positionCount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal sellMoney = positionCount.multiply(current.getOpeningPrice());
                initMoney = initMoney.add(sellMoney);
                positionCount = BigDecimal.ZERO;
                positionMoney = BigDecimal.ZERO;
                System.out.println("sell---" + sdf.format(priceList.get(i).getDate()) + "---" + initMoney);
            }
        }
        System.out.println(initMoney.toString() + "----" + positionMoney);
    }




}
