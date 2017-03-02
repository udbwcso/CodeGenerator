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
import java.util.*;

/**
 * Created by Administrator on 2017/2/14.
 * 更新数据
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
        stockData.getStartDate(stockList);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2017, 1, 28);
        Calendar endDate = Calendar.getInstance();

        List<Thread> threadList = stockData.storeData(startDate, endDate, stockList);
        while (threadList.size() > 0) {
            if(!threadList.get(0).isAlive()) {
                threadList.remove(0);
            }
        }
        System.out.println("===========" + stockData.correction(startDate, endDate) + "==========");
    }

    public void getStartDate(List<Stock> stockList) throws IOException {
        Map<String, Integer> dateMap = new HashMap<>();
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = stockList.get(i);
            String path = OLD_STOCK_DATA_PATH + File.separator + stock.getSpot().getKey()
                    + File.separator + stock.getCode() + ".txt";
            File file = new File(path);
            String price = FileUtils.readLines(file, "UTF-8").get(0);
            String date = price.split(" ")[0];
            if(dateMap.get(date) == null) {
                System.out.println(date + "----" + stock.getCode());
                dateMap.put(date, 1);
            } else {
                dateMap.put(date, dateMap.get(date) + 1);
            }
        }
        for (Map.Entry<String, Integer> entry : dateMap.entrySet()) {
            System.out.println(entry.getKey() + "----" + entry.getValue());
        }
    }

    /**
     * 更新数据前的准备工作
     * 删除上次更新时生成的数据文件和错误信息文件
     * @throws IOException
     */
    public void prepare() throws IOException {
        File newDataDir = new File(NEW_STOCK_DATA_PATH);
        if(newDataDir.exists()) {
            FileUtils.deleteDirectory(newDataDir);
        }
        File errorFile = new File(ERROR_FILE_PATH);
        if(errorFile.exists()) {
            FileUtils.forceDelete(errorFile);
        }
    }

    /**
     * 更新数据更新过程种由于网络原因未完成更新的数据
     * @param startDate 开始时间
     * @param endDate 结果时间
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public boolean correction(Calendar startDate, Calendar endDate) throws IOException, ParseException {
        File errorFile = new File(ERROR_FILE_PATH);
        int count = 0;
        do {
            String code = FileUtils.readFileToString(errorFile, "UTF-8");
            FileUtils.forceDelete(errorFile);
            System.out.println("-------------------------");
            System.out.println(code);
            System.out.println("-------------------------");
            String[] codes = code.split(",");
            correction(startDate, endDate, codes);
            errorFile = new File(ERROR_FILE_PATH);
            ++count;
        } while (errorFile.exists() && count < 10);
        return errorFile.exists();
    }

    private void correction(Calendar startDate, Calendar endDate, String[] codes) throws IOException, ParseException {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < codes.length; i++) {
            set.add(codes[i]);
        }
        StockDataReader stockDataService = new StockDataFileReader();
        List<Stock> stockList = stockDataService.getStockList();
        List<Stock> newList = new ArrayList<>();
        for (int i = 0; i < stockList.size(); i++) {
            if(set.contains(stockList.get(i).getCode())) {
                newList.add(stockList.get(i));
            }
        }
        storeData(startDate, endDate, newList);
    }

    public boolean check() {
        return true;
    }

    public List<Thread> storeData(Calendar startDate, Calendar endDate, List<Stock> stockList) {
        List<Stock> list = new ArrayList<>();
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < stockList.size(); i++) {
            list.add(stockList.get(i));
            if((i + 1) % 50 == 0 || i == stockList.size() - 1) {
                StoreStockDataThread stockData = new StoreStockDataThread(startDate, endDate, list);
                Thread thread = new Thread(stockData);
                thread.start();
                threadList.add(thread);
                list = new ArrayList<>();
            }
        }
        return threadList;
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
                List<StockPrice> priceList;
                try {
                    priceList = httpReader.getStockPriceList(stock, startDate, endDate);
                    StringBuilder sb = new StringBuilder("");
                    for (int j = 0; j < priceList.size(); j++) {
                        sb.append(priceList.get(j).toString()).append(System.getProperty("line.separator"));
                    }
                    System.out.println(sb);
                    sb.append(getHistoryDate(stock));
                    String fileName = File.separator + stock.getSpot().getKey() + File.separator + stock.getCode() + ".txt";
                    String filePath = NEW_STOCK_DATA_PATH + File.separator + fileName;
                    File file = new File(filePath);
//                    System.out.println(stock.getCode());
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
