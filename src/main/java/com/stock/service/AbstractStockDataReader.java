package com.stock.service;

import com.office.ExcelReader;
import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/2/14.
 */
public abstract class AbstractStockDataReader implements StockDataReader {

    private static final String szPath = "E:\\stock\\A\\深圳A股列表.xlsx";
    private static final String shPath = "E:\\stock\\A\\上海A股.xlsx";

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
