package com.stock.service;

import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */
public class StockDataFileReader extends AbstractStockDataReader implements StockDataReader {

    private static String STOCK_PATH = "E:\\stock_1";

    public StockDataFileReader() {
    }

    public StockDataFileReader(String path) {
        STOCK_PATH = path;
    }

    @Override
    public List<StockPrice> getStockPriceList(Stock stock) throws IOException, ParseException {
        String path = STOCK_PATH + File.separator + stock.getSpot().getKey()
                + File.separator + stock.getCode() + ".txt";
        File file = new File(path);
        if(!file.exists()) {
            return Collections.emptyList();
        }
        String priceData = FileUtils.readFileToString(file, "UTF-8");
        String[] strings = priceData.split(System.getProperty("line.separator"));
        List<StockPrice> priceList = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            if (StringUtils.isNotEmpty(strings[i].trim())) {
                StockPrice price = new StockPrice(strings[i]);
                priceList.add(price);
            }
        }
        return priceList;
    }

    @Override
    public List<StockPrice> getStockPriceList(Stock stock, Calendar startDate, Calendar endDate) throws IOException, ParseException {
        List<StockPrice> priceList = getStockPriceList(stock);
        return getStockPriceList(priceList, startDate, endDate);
    }

    private List<StockPrice> getStockPriceList(List<StockPrice> priceList, Calendar startDate, Calendar endDate) {
        List<StockPrice> rstList = new ArrayList<>();
        DateTime start = new DateTime(endDate.getTimeInMillis());
        DateTime end = new DateTime(startDate.getTimeInMillis());
        for (int i = 0; i < priceList.size(); i++) {
            StockPrice price = priceList.get(i);
            if(start.isBefore(price.getDate().getTime())
                    && end.isAfter(price.getDate().getTime())) {
                rstList.add(price);
            }
        }
        return rstList;
    }
}
