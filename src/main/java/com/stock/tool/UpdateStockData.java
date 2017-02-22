package com.stock.tool;

import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import com.stock.service.StockDataFileReader;
import com.stock.service.StockDataHttpReader;
import com.stock.service.StockDataReader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/14.
 */
public class UpdateStockData {
    public static void main(String[] args) throws IOException, ParseException {
        String dataPath = "E:\\stock";
        StockDataReader fileReader = new StockDataFileReader(dataPath);
        List<Stock> stockList = fileReader.getStockList();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 1, 21);
        Calendar endDate = Calendar.getInstance();
        UpdateStockData stockData = new UpdateStockData();
        stockData.storeData(startDate, endDate, stockList);
//        stockData.backUpHistoryData(dataPath);
    }

    public void backUpHistoryData(String path) throws IOException {
        FileUtils.deleteDirectory(new File("E:\\stock back up\\stock"));
        String backUpDirectory = "E:\\stock back up\\" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        FileUtils.moveDirectory(new File(path), new File(backUpDirectory));
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
        private static final String STOCK_PATH = "E:\\stock";

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
                    String filePath = "E:\\stock_1" + File.separator + fileName;
                    File file = new File(filePath);
                    System.out.println(stock.getCode());
                    FileUtils.writeStringToFile(file, sb.toString(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private String getHistoryDate(Stock stock) throws IOException {
            String path = STOCK_PATH + File.separator + stock.getSpot().getKey()
                    + File.separator + stock.getCode() + ".txt";
            File file = new File(path);
            if(!file.exists()) {
                return "";
            }
            return FileUtils.readFileToString(file, "UTF-8");
        }
    }


}
