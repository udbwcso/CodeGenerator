package com.stock.tool;

import com.stock.bean.ListingSpot;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import com.stock.service.CalculateService;
import com.stock.service.CalculateServiceImpl;
import com.stock.service.StockDataFileReader;
import com.stock.service.StockDataReader;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 */
public class ChooseStock {
    public static void main(String[] args) throws IOException, ParseException {
        List<String> riseList = continuousRise();
        List<String> averageList = averageConvergence();
        System.out.println(riseList.size() + "-------" + averageList.size());
        for (int i = 0; i < riseList.size(); i++) {
            String rise = riseList.get(i);
            for (int j = 0; j < averageList.size(); j++) {
                if(rise.equals(averageList.get(j))) {
                    System.out.println("==========" + rise);
                }
            }
        }

    }

    /**
     * 持续上涨
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static List<String> continuousRise() throws IOException, ParseException {
        StockDataReader stockDataService = new StockDataFileReader();
        List<Stock> stockList = stockDataService.getStockList(ListingSpot.SZ);
        List<String> rstList = new ArrayList<>();
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = stockList.get(i);
            List<StockPrice> priceList = stockDataService.getStockPriceList(stock);
            int riseDay = 2;
            int count = 0;
            for (int j = 0; j < riseDay; j++) {
                StockPrice current = priceList.get(j);
                StockPrice before = priceList.get(j + 1);
                if(current.getOpeningPrice().compareTo(before.getOpeningPrice()) >= 0
                        && current.getClosingPrice().compareTo(before.getClosingPrice()) >= 0
                        && current.getHighestPrice().compareTo(before.getHighestPrice()) >= 0
                        && current.getLowestPrice().compareTo(before.getLowestPrice()) >= 0
                        && current.getTradingVolume().compareTo(current.getTradingVolume()) >= 0
                        && current.getClosingPrice().compareTo(current.getOpeningPrice()) >= 0) {
                    ++count;
                }
            }
            if(count == riseDay) {
                System.out.println(stock.getCode());
                rstList.add(stock.getCode());
            }
        }
        return rstList;
    }

    /**
     * 均线收敛
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static List<String> averageConvergence() throws IOException, ParseException {
        StockDataReader stockDataService = new StockDataFileReader();
        List<Stock> stockList = stockDataService.getStockList(ListingSpot.SZ);
        List<String> rstList = new ArrayList<>();
        for (int i = 0; i < stockList.size(); i++) {
            Stock stock = stockList.get(i);
            List<StockPrice> priceList = stockDataService.getStockPriceList(stock);

            CalculateService calculateService = new CalculateServiceImpl();
            Integer[] days = new Integer[]{5, 10, 20, 30, 60};
            if (priceList.size() < days[days.length - 1] + days[0]) {
                continue;
            }
            calculateService.average(priceList, days, 3);
            for (int j = 0; j < 3; j++) {
                StockPrice price = priceList.get(j);
                BigDecimal[] result = new BigDecimal[days.length];
                for (int k = 0; k < days.length; k++) {
                    result[k] = priceList.get(j).getAverage().get(days[k]);
                }
                int cnt = 0;
                Arrays.sort(result);
                for (int k = 0; k < result.length; k++) {
                    BigDecimal sub = result[k].subtract(result[0]).abs();
                    BigDecimal deviate = sub.divide(priceList.get(0).getClosingPrice(), 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    if (deviate.compareTo(new BigDecimal("0.2")) < 0) {
                        ++cnt;
                    }
                }
                if (cnt >= days.length - 2) {
                    for (BigDecimal average : result) {
                        System.out.print(average + " ");
                    }
                    System.out.println();
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(price.getDate());
                    System.out.println(stock.getCode() + "----" + date);
                    rstList.add(stock.getCode());
                }
            }
        }
        return rstList;
    }
}
