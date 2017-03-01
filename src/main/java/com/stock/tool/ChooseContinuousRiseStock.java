package com.stock.tool;

import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import com.stock.service.StockDataFileReader;
import com.stock.service.StockDataReader;
import com.stock.util.StockPriceUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Administrator on 2017/2/22.
 */
public class ChooseContinuousRiseStock {
    public static void main(String[] args) throws IOException, ParseException {
        StockDataReader stockDataService = new StockDataFileReader();
        List<Stock> stockList = stockDataService.getStockList();
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = stockList.get(i);
            List<StockPrice> priceList = stockDataService.getStockPriceList(stock);
            int riseDay = 3;
            int count = 0;
            for (int j = 0; j < riseDay; j++) {
                StockPrice current = priceList.get(j);
                StockPrice before = priceList.get(j + 1);
                if(StockPriceUtil.compare(current, before)) {
                    ++count;
                }
            }
            if(count == riseDay) {
                System.out.println(stock.getCode());
            }
        }
    }
}
