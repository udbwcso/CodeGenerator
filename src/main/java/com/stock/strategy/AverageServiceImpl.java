package com.stock.strategy;

import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import com.stock.service.CalculateService;
import com.stock.service.CalculateServiceImpl;
import com.stock.service.StockDataFileReader;
import com.stock.service.StockDataReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2017/2/21.
 */
public class AverageServiceImpl implements StrategyService {
    @Override
    public void test(Stock stock) throws IOException, ParseException {
        StockDataReader stockDataService = new StockDataFileReader();
        List<StockPrice> priceList = stockDataService.getStockPriceList(stock);
        CalculateService calculateService = new CalculateServiceImpl();
        Integer[] days = new Integer[]{5, 10, 15, 20, 25, 30, 40, 50, 60, 100};
        calculateService.average(priceList, days, 3);
        Integer day = 100;
        BigDecimal initMoney = new BigDecimal(20000);
        BigDecimal buyCount = new BigDecimal(200);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = priceList.size() - 3 - days[days.length - 1]; i > 0; i--) {
            StockPrice beforeYesterday = priceList.get(i + 2);
            StockPrice yesterday = priceList.get(i + 1);
            StockPrice today = priceList.get(i);
            if (beforeYesterday.getAverage().get(day).compareTo(yesterday.getAverage().get(day)) <= 0
                    && yesterday.getAverage().get(day).compareTo(today.getAverage().get(day)) < 0) {
                //buy
                BigDecimal fee = today.getClosingPrice().multiply(buyCount);
                BigDecimal charge = fee.multiply(new BigDecimal("0.005"));
                initMoney = initMoney.subtract(fee).subtract(charge);
                System.out.println("buy----" + sdf.format(today.getDate()) + "----" + today.getClosingPrice().toString());
                //sell
                do {
                    i--;
                    yesterday = priceList.get(i + 1);
                    today = priceList.get(i);
                } while (today.getLowestPrice().compareTo(yesterday.getLowestPrice()) > 0);
                fee = today.getClosingPrice().multiply(buyCount);
                charge = fee.multiply(new BigDecimal("0.005"));
                initMoney = initMoney.add(fee).subtract(charge);
                System.out.println("sell----" + sdf.format(today.getDate()) + "----" + today.getClosingPrice().toString());
            }
        }
        System.out.println(initMoney.toString());
    }
}
