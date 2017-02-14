package com.stock.tool;

import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import com.stock.service.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/2/14.
 */
public class UpdateStockDate {
    public static void main(String[] args) throws IOException, ParseException {
        StockDataReader fileReader = new StockDataFileReader("E:\\stock");
        StockDataReader httpReader = new StockDataHttpReader();
        StockDataWriter writer = new StockDataFileWriter();
        List<Stock> stockList = fileReader.getStockList();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 1, 8);
        Calendar endDate = Calendar.getInstance();
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = stockList.get(i);
            List<StockPrice> priceList = new ArrayList<>();
            priceList.addAll(httpReader.getStockPriceList(stock, startDate, endDate));
            priceList.addAll(fileReader.getStockPriceList(stock));
            stock.setPriceList(priceList);
            writer.storeData(stock, "E:\\stock_1", false);
        }
    }


}
