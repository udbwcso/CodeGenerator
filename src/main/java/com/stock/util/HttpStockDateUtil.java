package com.stock.util;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.service.StockDataFileReader;
import com.stock.service.StockDataReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */
public class HttpStockDateUtil {

    public static void main(String[] args) throws IOException, ParseException {
        String shDataDirectory = "D:\\stock_2_1\\sh";
        String szDataDirectory = "D:\\stock_1_1\\sz";
        StockDataReader stockDataService = new StockDataFileReader();
        List<Stock> shStockList = stockDataService.getStockList(ListingSpot.SH);
        List<Stock> szStockList = stockDataService.getStockList(ListingSpot.SZ);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 0);
        storeData(shStockList, shDataDirectory, calendar, false);
//        storeData(szStockList, szDataDirectory, calendar, false);

//        Stock stock = new Stock();
//        stock.setCode("000651");
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
//        stock.setSpot(ListingSpot.SZ);
//        storeData(stock, szDataDirectory, calendar, false);
    }

    /**
     * 从网上获取股票价格数据
     * 并保存到文件中
     * @param stockList 股票信息列表
     * @param directory 存储数据的根目录
     * @param startDate 开始时间
     * @param append 写文件时是否是追加
     */
    public static void storeData(List<Stock> stockList, String directory, Calendar startDate, boolean append) {
        List<Stock> list = new ArrayList<>();
        for (int i = 0; i < stockList.size(); i++) {
            list.add(stockList.get(i));
            if((i + 1) % 200 == 0 || i == stockList.size() - 1) {
                StoreStockDataThread stockData = new StoreStockDataThread(list, directory, startDate, append);
                Thread thread = new Thread(stockData);
                thread.start();
                list = new ArrayList<>();
            }
        }
    }

    public static String getCurrentData(String code, String address) throws IOException {
        String url = "http://hq.sinajs.cn/list=" + address + code;
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(new HttpGet(url));
        int statusCode = response.getStatusLine().getStatusCode();
        String filePath = "E:\\workspace\\code\\src\\main\\java\\com\\stock\\data.txt";
        if(statusCode == 200) {
            StringWriter sw = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), sw, "GBK");
            String string = sw.toString();
            FileUtils.writeStringToFile(new File(filePath), string, true);
            return string;
        }
        return null;
    }


}
