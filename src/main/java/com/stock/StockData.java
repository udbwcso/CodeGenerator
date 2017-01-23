package com.stock;

import com.stock.bean.Stock;

import java.util.List;

/**
 * Created by Administrator on 2017/1/20.
 */
public class StockData implements Runnable {

    private List<Stock> stockList;
    private String directory;

    public StockData(List<Stock> stockList, String directory){
        this.stockList = stockList;
        this.directory = directory;
    }

    @Override
    public void run() {
        for (Stock stock : stockList) {
            StockUtil.storeData(stock, directory);
        }
    }
}
