package com.stock.service;

import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */
public class StockDataHttpReader extends AbstractStockDataReader implements StockDataReader {

    @Override
    public List<StockPrice> getStockPriceList(Stock stock) throws IOException, ParseException {
        Calendar startDate = Calendar.getInstance();
        startDate.setTime(stock.getListingDate());
        return getStockPriceList(stock, startDate, Calendar.getInstance());
    }

    @Override
    public List<StockPrice> getStockPriceList(Stock stock, Calendar startDate, Calendar endDate) throws IOException, ParseException {
        int startYear = startDate.get(Calendar.YEAR);
        int endYear = endDate.get(Calendar.YEAR);
        int endQuarter = Calendar.getInstance().get(Calendar.MONTH) / 3 + 1;
        List<String[]> stockList = new ArrayList<>();
        for (int i = endYear; i >= startYear; i--) {
            int j = i == endYear ? endQuarter : 4;
            for (; j >= 1; j--) {
                List<String[]> list = getHistoryData(stock, i, j);
                stockList.addAll(list);
            }
        }
        List<StockPrice> rstList = new ArrayList<>();
        DateTime start = new DateTime(startDate.getTimeInMillis());
        DateTime end = new DateTime(endDate.getTimeInMillis());
        for (int i = 0; i < stockList.size(); i++) {
            String[] strings = stockList.get(i);
            StockPrice price = new StockPrice(strings);
            if(start.isBefore(price.getDate().getTime())
                    && end.isAfter(price.getDate().getTime())) {
                rstList.add(price);
            }
        }
        return rstList;
    }

    /**
     * 获取股票价格信息
     * @param stock 股票信息
     * @param year 年份
     * @param quarter 季度
     * @return
     */
    private List<String[]> getHistoryData(Stock stock, int year, int quarter) {
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

    /**
     * 获取股票价格信息
     * @param stock 股票信息
     * @param year 年份
     * @param quarter 季度
     * @return
     */
    private List<String[]> search(String stock, int year, int quarter) {
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
            System.out.println(stock + "--" + year + "--" + quarter);
            String error = stock + ",";
            try {
                FileUtils.writeStringToFile(new File("D:\\error.txt"), error, true);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

}
