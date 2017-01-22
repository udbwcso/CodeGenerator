package com.stock;

import com.stock.bean.Stock;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
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
        int endYear = Calendar.getInstance().get(Calendar.YEAR);
        for (Stock stock : stockList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(stock.getListingDate());
            int startYear = calendar.get(Calendar.YEAR);
            for (int i = endYear; i >= startYear; i--) {
                for (int j = 4; j >= 1; j--) {
                    try {
                        String path = directory + File.separator + i + File.separator + j;
                        StockUtil.getHistoryData(path, stock, i, j, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
