package com.stock.util;

import com.stock.bean.Stock;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 */
public class StoreStockDataThread implements Runnable {

    private List<Stock> stockList;
    private String directory;
    private Calendar startDate;
    private boolean append;

    public StoreStockDataThread(List<Stock> stockList, String directory, Calendar startDate, boolean append){
        this.stockList = stockList;
        this.directory = directory;
        this.startDate = startDate;
        this.append = append;
    }

    @Override
    public void run() {
//        for (Stock stock : stockList) {
//            try {
//                StockDateUtil.storeData(stock, directory, startDate, append);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
    }
}