package com.stock.service;

import com.office.ExcelReader;
import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/1/22.
 */
public class FileStockDataServiceImpl implements StockDataService {

    private static final String szPath = "E:\\stock\\A\\深圳A股列表.xlsx";
    private static final String shPath = "E:\\stock\\A\\上海A股.xlsx";
    private static final String stockPath = "D:\\stock";


//    @Override
//    public List<Stock> getStockByCode(List<Stock> stockList, String... codes) {
//        List<String> codeList = Arrays.asList(codes);
//        List<Stock> rstList = new ArrayList<>();
//        for (int i = 0; i < stockList.size(); i++) {
//            if(codeList.contains(stockList.get(i).getCode())) {
//                rstList.add(stockList.get(i));
//            }
//        }
//        return rstList;
//    }

    @Override
    public List<Stock> getStockList(ListingSpot spot) throws IOException, ParseException {
        if(spot.equals(ListingSpot.SH)) {
            return getStockList(spot, shPath, 2, 4);
        } else if(spot.equals(ListingSpot.SZ)) {
            return getStockList(spot, szPath, 5, 7);
        }
        return Collections.emptyList();
    }

    @Override
    public List<StockPrice> getStockPriceList(Stock stock) throws IOException, ParseException {
        String path = stockPath + File.separator + stock.getSpot().getKey()
                + File.separator + stock.getCode() + ".txt";
        File file = new File(path);
        if(!file.exists()) {
            return Collections.emptyList();
        }
        String priceData = FileUtils.readFileToString(file, "UTF-8");
        String[] strings = priceData.split(System.getProperty("line.separator"));
        List<StockPrice> priceList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < strings.length; i++) {
            if (StringUtils.isNotEmpty(strings[i].trim())) {
                String[] info = strings[i].split(" ");
                StockPrice price = new StockPrice();
                price.setDate(sdf.parse(info[0]));
                price.setOpeningPrice(new BigDecimal(info[1]));
                price.setHighestPrice(new BigDecimal(info[2]));
                price.setClosingPrice(new BigDecimal(info[3]));
                price.setLowestPrice(new BigDecimal(info[4]));
                price.setTradingVolume(new BigDecimal(info[5]));
                price.setAmount(new BigDecimal(info[6]));
                priceList.add(price);
            }
        }
        return priceList;
    }

    @Override
    public List<StockPrice> getStockPriceList(Stock stock, Calendar startDate, Calendar endDate) throws IOException, ParseException {
        List<StockPrice> priceList = getStockPriceList(stock);
        List<StockPrice> rstList = new ArrayList<>();
        DateTime start = new DateTime(endDate.getTimeInMillis());
        DateTime end = new DateTime(startDate.getTimeInMillis());
        for (int i = 0; i < priceList.size(); i++) {
            StockPrice price = priceList.get(i);
            if(start.isBefore(price.getDate().getTime())
                    && end.isAfter(price.getDate().getTime())) {
                rstList.add(price);
            }
        }
        return rstList;
    }

    @Override
    public List<StockPrice> getStockPriceList(List<StockPrice> priceList, Date startDate, Date endDate) {
        List<StockPrice> list = new ArrayList<>();
        for (int i = 0; i < priceList.size(); i++) {
            StockPrice price = priceList.get(i);
            DateTime dateTime = new DateTime(price.getDate());
            if(dateTime.isAfter(startDate.getTime())
                    && dateTime.isBefore(endDate.getTime())) {
                list.add(price);
            }
        }
        return list;
    }

    @Override
    public void storeStockPriceData(List<StockPrice> priceList) {

    }

    private List<Stock> getStockList(ListingSpot spot, String path, int codeCell, int dateCell) throws ParseException, IOException {
        List<String[]> list = ExcelReader.read(path);
        List<Stock> stockList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i < list.size(); i++) {
            String[] strings = list.get(i);
            Stock stock = new Stock();
            stock.setCode(strings[codeCell]);
            stock.setListingDate(sdf.parse(strings[dateCell]));
            stock.setSpot(spot);
            stockList.add(stock);
        }
        return stockList;
    }
}
