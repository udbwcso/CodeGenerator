package com.stock.tool;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import com.stock.service.StockDataFileReader;
import com.stock.service.StockDataHttpReader;
import com.stock.service.StockDataReader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Administrator on 2017/2/14.
 */
public class UpdateStockData {

    private static final String ERROR_FILE_PATH = "D:\\error.txt";
    private static final String OLD_STOCK_DATA_PATH = "E:\\stock";
    private static final String NEW_STOCK_DATA_PATH = "E:\\stock_1";


    public static void main(String[] args) throws IOException, ParseException {
        UpdateStockData stockData = new UpdateStockData();
        stockData.prepare();

        StockDataReader fileReader = new StockDataFileReader(OLD_STOCK_DATA_PATH);
        List<Stock> stockList = fileReader.getStockList();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 1, 21);
        Calendar endDate = Calendar.getInstance();
        stockData.storeData(startDate, endDate, stockList);

        stockData.correction(startDate, endDate);
    }

    public static void prepare() throws IOException {
        FileUtils.deleteDirectory(new File(NEW_STOCK_DATA_PATH));
        File errorFile = new File(ERROR_FILE_PATH);
        if(errorFile.exists()) {
            FileUtils.forceDelete(errorFile);
        }
    }

    public void correction(Calendar startDate, Calendar endDate) throws IOException, ParseException {
        String code = FileUtils.readFileToString(new File(ERROR_FILE_PATH), "UTF-8");
        String[] codes = code.split(",");
        Set<String> set = new HashSet<>();
        for (int i = 0; i < codes.length; i++) {
            set.add(codes[i]);
        }
        StockDataReader stockDataService = new StockDataFileReader();
        List<Stock> shStockList = stockDataService.getStockList(ListingSpot.SH);
        List<Stock> szStockList = stockDataService.getStockList(ListingSpot.SZ);
        List<Stock> shNewList = new ArrayList<>();
        List<Stock> szNewList = new ArrayList<>();
        System.out.println(set.size());
        for (int i = 0; i < shStockList.size(); i++) {
            if(set.contains(shStockList.get(i).getCode())) {
                shNewList.add(shStockList.get(i));
            }
        }
        for (int i = 0; i < szStockList.size(); i++) {
            if(set.contains(szStockList.get(i).getCode())) {
                szNewList.add(szStockList.get(i));
            }
        }
        storeData(startDate, endDate, szNewList);
        storeData(startDate, endDate, shNewList);
    }

    public boolean check() {
        return true;
    }

    public void storeData(Calendar startDate, Calendar endDate, List<Stock> stockList) {
        List<Stock> list = new ArrayList<>();
        for (int i = 0; i < stockList.size(); i++) {
            list.add(stockList.get(i));
            if((i + 1) % 50 == 0 || i == stockList.size() - 1) {
                StoreStockDataThread stockData = new StoreStockDataThread(startDate, endDate, list);
                Thread thread = new Thread(stockData);
                thread.start();
                list = new ArrayList<>();
            }
        }
    }

    private class StoreStockDataThread implements Runnable{
        private Calendar startDate;
        private Calendar endDate;
        private List<Stock> stockList;

        public StoreStockDataThread(Calendar startDate, Calendar endDate, List<Stock> stockList) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.stockList = stockList;
        }

        @Override
        public void run() {
            StockDataReader httpReader = new StockDataHttpReader();
            for (int i = 0; i < stockList.size(); i++) {
                Stock stock = stockList.get(i);
                List<StockPrice> priceList = null;
                try {
                    priceList = httpReader.getStockPriceList(stock, startDate, endDate);
                    StringBuilder sb = new StringBuilder("");
                    for (int j = 0; j < priceList.size(); j++) {
                        sb.append(priceList.get(j).toString()).append(System.getProperty("line.separator"));
                    }
                    sb.append(getHistoryDate(stock));
                    String fileName = File.separator + stock.getSpot().getKey() + File.separator + stock.getCode() + ".txt";
                    String filePath = NEW_STOCK_DATA_PATH + File.separator + fileName;
                    File file = new File(filePath);
                    System.out.println(stock.getCode());
                    FileUtils.writeStringToFile(file, sb.toString(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private String getHistoryDate(Stock stock) throws IOException {
            String path = OLD_STOCK_DATA_PATH + File.separator + stock.getSpot().getKey()
                    + File.separator + stock.getCode() + ".txt";
            File file = new File(path);
            if(!file.exists()) {
                return "";
            }
            return FileUtils.readFileToString(file, "UTF-8");
        }
    }


}
