package com.stock.service;

import com.office.ExcelReader;
import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */
public class StockDataFileReader implements StockDataReader {

    private static final String szPath = "E:\\stock\\A\\深圳A股列表.xlsx";
    private static final String shPath = "E:\\stock\\A\\上海A股.xlsx";
    private static final String stockPath = "D:\\stock";


    @Override
    public List<Stock> getStockList() throws IOException, ParseException {
        List<Stock> list = new ArrayList<>();
        list.addAll(getStockList(ListingSpot.SH, shPath, 2, 4));
        list.addAll(getStockList(ListingSpot.SZ, szPath, 5, 7));
        return list;
    }

    @Override
    public List<Stock> getStockList(ListingSpot spot) throws IOException, ParseException {
        if(spot.equals(ListingSpot.SH)) {
            return getStockList(spot, shPath, 2, 4);
        } else if(spot.equals(ListingSpot.SZ)) {
            return getStockList(spot, szPath, 5, 7);
        }
        return Collections.emptyList();
    }

    @Override
    public List<StockPrice> getStockPriceList(Stock stock) throws IOException, ParseException {
        String path = stockPath + File.separator + stock.getSpot().getKey()
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

    private List<Stock> getStockList(ListingSpot spot, String path, int codeCell, int dateCell) throws ParseException, IOException {
        List<String[]> list = ExcelReader.read(path);
        List<Stock> stockList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i < list.size(); i++) {
            String[] strings = list.get(i);
            Stock stock = new Stock();
            stock.setCode(strings[codeCell]);
            stock.setListingDate(sdf.parse(strings[dateCell]));
            stock.setSpot(spot);
            stockList.add(stock);
        }
        return stockList;
    }
}
