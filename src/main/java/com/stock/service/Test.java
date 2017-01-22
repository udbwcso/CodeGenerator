package com.stock.service;

import com.stock.StockUtil;
import com.stock.bean.Stock;
import com.stock.bean.StockPrice;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
public class Test {
    public static void main(String[] args) throws IOException, ParseException {
        Stock stock = new Stock();
        stock.setCode("000001");
        StockUtil.getHistoryData("D:\\stock\\sz", stock, 2017, 1);
        StockUtil.getHistoryData("D:\\stock\\sz", stock, 2016, 4);
        StockUtil.getHistoryData("D:\\stock\\sz", stock, 2016, 3);
        StockUtil.getHistoryData("D:\\stock\\sz", stock, 2016, 2);

        String path = "D:\\stock\\sz\\000001.txt";
        String priceData = FileUtils.readFileToString(new File(path), "UTF-8");
        String[] strings = priceData.split(System.getProperty("line.separator"));
        List<StockPrice> priceList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < strings.length; i++) {
            if (StringUtils.isNotEmpty(strings[i].trim())
                    && !strings[i].trim().startsWith("日期")) {
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
        BigDecimal[] numbers = new BigDecimal[priceList.size()];
        for (int i = 0; i < priceList.size(); i++) {
            numbers[i] = priceList.get(i).getClosingPrice();
        }
        CalculateService calculateService = new CalculateServiceImpl();
        int[] days = new int[]{5, 10, 20, 30, 60, 120};
        BigDecimal[] result = new BigDecimal[days.length];
        for (int i = 0; i < days.length; i++) {
            result[i] = calculateService.getAverage(numbers, days[i]);
            BigDecimal sub = priceList.get(0).getClosingPrice().subtract(result[i]);
            BigDecimal deviate = sub.divide(result[i], 5, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            System.out.println(result[i].toString() + "----" + sub.toString() + "----" + deviate.toString());
        }
    }
}
