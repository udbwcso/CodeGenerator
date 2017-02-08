package com.stock;

import com.doc.JsonWrapper;
import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.service.FileStockDataServiceImpl;
import com.stock.service.StockDataService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/1/18.
 */
public class StockUtil {

    public static void main(String[] args) throws IOException, ParseException {
        String shDataDirectory = "D:\\stock1\\sh";
        String szDataDirectory = "D:\\stock1\\sz";
        StockDataService stockDataService = new FileStockDataServiceImpl();
        List<Stock> shStockList = stockDataService.getStockList(ListingSpot.SH);
        List<Stock> szStockList = stockDataService.getStockList(ListingSpot.SZ);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 0);
        storeData(shStockList, shDataDirectory, calendar, false);
        storeData(szStockList, szDataDirectory, calendar, false);

//        Stock stock = new Stock();
//        stock.setCode("000651");
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
//        stock.setSpot(ListingSpot.SZ);
//        storeData(stock, szDataDirectory, calendar, false);
    }

    public static void storeData(List<Stock> stockList, String directory, Calendar startDate, boolean append) {
        List<Stock> list = new ArrayList<>();
        for (int i = 0; i < stockList.size(); i++) {
            list.add(stockList.get(i));
            if((i + 1) % 20 == 0 || i == stockList.size() - 1) {
                StockData stockData = new StockData(list, directory, startDate, append);
                Thread thread = new Thread(stockData);
                thread.start();
                list = new ArrayList<>();
            }
        }
    }

    public static void storeData(Stock stock, String directory, Calendar startDate, boolean append) throws IOException, ParseException {
        String fileName = stock.getCode() + ".txt";
        String filePath = directory + File.separator + fileName;
        String lineSeparator = System.getProperty("line.separator");
        List<String[]> list = getHistoryData(stock, startDate);
        StringBuilder rst = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String[] strings = list.get(i);
            for (int j = 0; j < strings.length; j++) {
                rst.append(strings[j] + " ");
            }
            rst.append(lineSeparator);
        }
        FileUtils.writeStringToFile(new File(filePath), rst.toString(), append);
    }

    public static List<String[]> getHistoryData(Stock stock, Calendar startDate) throws ParseException {
        int startYear = startDate.get(Calendar.YEAR);
        int endYear = Calendar.getInstance().get(Calendar.YEAR);
        List<String[]> stockList = new ArrayList<>();
        for (int i = endYear; i >= startYear; i--) {
            for (int j = 4; j >= 1; j--) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<String[]> list = getHistoryData(stock, i, j);
                stockList.addAll(list);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<String[]> rstList = new ArrayList<>();
        for (int i = 0; i < stockList.size(); i++) {
            String[] strings = stockList.get(i);
            Date date = sdf.parse(strings[0]);
            DateTime start = new DateTime(startDate.getTimeInMillis());
            if(start.isBefore(date.getTime())) {
                rstList.add(strings);
            }
        }
        return rstList;
    }

    public static List<String[]> getHistoryData(Stock stock, int year, int quarter) {
        List<String[]> list = search(stock.getCode(), year, quarter);
        List<String[]> rstList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String[] strings = list.get(i);
            if(strings != null && strings.length != 0 && !strings[0].trim().equals("日期")) {
                rstList.add(strings);
            }
        }
        return rstList;
    }

    public static List<Stock> getStockList(String path, String charset) throws IOException {
        String string = FileUtils.readFileToString(new File(path), charset);
        String[] values = string.split(System.getProperty("line.separator"));
        List<Stock> stockList = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            String[] stockInfo = values[i].split("\\(");
            Stock stock = new Stock();
            stock.setName(stockInfo[0]);
            stock.setCode(stockInfo[1].replace(")", ""));
            stockList.add(stock);
        }
        return stockList;
    }

    public static void getHistoryData(String directory, Stock stock, int year, int quarter, boolean append) throws IOException {
        List<String[]> list = search(stock.getCode(), year, quarter);
        System.out.println(JsonWrapper.writeValue(stock) + "----" + year + "--" + quarter + "--" + list.size());
        String fileName = stock.getCode() + ".txt";
        String filePath = directory + File.separator + fileName;
        String lineSeparator = System.getProperty("line.separator");
        StringBuilder rst = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String[] strings = list.get(i);
            for (int j = 0; j < strings.length; j++) {
                rst.append(strings[j] + " ");
            }
            rst.append(lineSeparator);
        }
        FileUtils.writeStringToFile(new File(filePath), rst.toString(), append);
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

    public static List<String[]> search(String stock, int year, int quarter) {
        Document doc = null;
        // create URL string
        String url = String.format("http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/%s.phtml?year=%s&jidu=%s",
                stock, year, quarter);
        try {
            // parse html by Jsoup
            doc = Jsoup.parse(new URL(url), 60000);
            //取所有查询结果
            Element element = doc.getElementById("FundHoldSharesTable");
            if(element == null) {
                return Collections.emptyList();
            }
            Elements children = element.select("tr");
            List<String[]> list = new ArrayList<>();
            for (int i = 0; i < children.size(); i++) {
                Element child = children.get(i);
                Elements tds = child.select("td");
                String[] values = new String[tds.size()];
                for (int j = 0; j < tds.size(); j++) {
                    Element td = tds.get(j);
                    String value = td.html();
                    value = value.replaceAll("<.*?>", "").trim();
                    values[j] = value;
                }
                list.add(values);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(stock);
        }
        return Collections.emptyList();
    }
}
