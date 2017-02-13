package com.stock.tool;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.service.StockDataFileReader;
import com.stock.service.StockDataReader;
import com.stock.util.HttpStockDateUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Administrator on 2017/2/9.
 */
public class T {
    public static void main(String[] args) throws IOException, ParseException {
        String code = FileUtils.readFileToString(new File("D:\\error.txt"), "UTF-8");
        String[] codes = code.split(",");
        Set<String> set = new HashSet<>();
        for (int i = 0; i < codes.length; i++) {
            set.add(codes[i]);
        }

        String shDataDirectory = "D:\\stock_2_2\\sh";
        String szDataDirectory = "D:\\stock_1_2\\sz";
        StockDataReader stockDataService = new StockDataFileReader();
        List<Stock> shStockList = stockDataService.getStockList(ListingSpot.SH);
        List<Stock> szStockList = stockDataService.getStockList(ListingSpot.SZ);
        List<Stock> newList = new ArrayList<>();
        System.out.println(set.size());
        for (int i = 0; i < shStockList.size(); i++) {
            if(set.contains(shStockList.get(i).getCode())) {
                newList.add(shStockList.get(i));
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 0);
//        StockUtil.storeData(shStockList, shDataDirectory, calendar, false);
        HttpStockDateUtil.storeData(newList, shDataDirectory, calendar, false);
    }
}