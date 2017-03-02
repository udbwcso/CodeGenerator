package com.stock.strategy;

import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import com.stock.service.StockDataFileReader;
import com.stock.service.StockDataReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */
public class PriceStrategyServiceImpl implements StrategyService {
    @Override
    public void test(Stock stock) throws IOException, ParseException {
        StockDataReader stockDataService = new StockDataFileReader();
        List<StockPrice> priceList = stockDataService.getStockPriceList(stock);

        int days = 3;
        BigDecimal buyCount = new BigDecimal(200);
        BigDecimal init = new BigDecimal(20000);
        for (int i = priceList.size() - days; i > 0; i--) {
            StockPrice current = priceList.get(i);
            int cnt = 0;
            for (int j = 1; j < days; j++) {
                StockPrice price = priceList.get(i + j);
                if(current.getHighestPrice().compareTo(price.getHighestPrice()) > 0
                        && current.getLowestPrice().compareTo(price.getLowestPrice()) > 0) {
                    ++cnt;
                }
                current = price;
            }

            current = priceList.get(i);
            StockPrice tomorrow = priceList.get(i - 1);
            if(cnt == days - 1) {
                BigDecimal buyMoney = current.getClosingPrice().multiply(buyCount);
                BigDecimal sellMoney = tomorrow.getClosingPrice().multiply(buyCount);
                init = init.subtract(buyMoney).subtract(buyMoney.multiply(new BigDecimal("0.005")));
                init = init.add(sellMoney).subtract(sellMoney.multiply(new BigDecimal("0.005")));
                System.out.println(init.toString());
            }
        }
    }
}
