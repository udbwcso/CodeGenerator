package com.stock.service;

import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */
public class StockDataFileWriter implements StockDataWriter {

    @Override
    public void storeData(Stock stock, String directory, Calendar startDate, boolean append) throws IOException, ParseException {
        String fileName = stock.getCode() + ".txt";
        String filePath = directory + File.separator + fileName;
        String lineSeparator = System.getProperty("line.separator");
        List<StockPrice> list = stock.getPriceList();
        StringBuilder rst = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            StockPrice price = list.get(i);
            rst.append(price.toString()).append(lineSeparator);
        }
        File file = new File(filePath);
        if(!file.exists()) {
            FileUtils.writeStringToFile(file, rst.toString(), append);
        }
    }
}
