package com.stock.service;

import com.stock.bean.StockPrice;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/22.
 */
public class CalculateServiceImpl implements CalculateService{
    @Override
    public BigDecimal getAverage(BigDecimal[] numbers, int count, int scale) {
        if(numbers == null || numbers.length < count) {
            throw new IllegalArgumentException("numbers is Illegal");
        }
        BigDecimal sum = new BigDecimal(0);
        for (int i = 0; i < count; i++) {
            sum = sum.add(numbers[i]);
        }
        return sum.divide(new BigDecimal(count), scale, BigDecimal.ROUND_DOWN);
    }

    @Override
    public BigDecimal getMovingAverage(BigDecimal[] numbers, int fast, int slow, int standard) {
        return null;
    }

    @Override
    public List<StockPrice> average(List<StockPrice> priceList, Integer[] counts, int scale) {
        for (int i = 0; i < priceList.size() - counts[counts.length - 1]; i++) {
            StockPrice price = priceList.get(i);
            Map<Integer, BigDecimal> averageMap = new LinkedHashMap<>();
            for (int j = 0; j < counts.length; j++) {
                BigDecimal[] numbers = new BigDecimal[counts[j]];
                int index = 0;
                for (int k = i; k < i + counts[j]; k++) {
                    numbers[index] = priceList.get(k).getClosingPrice();
                    ++index;
                }
                BigDecimal average = getAverage(numbers, counts[j], scale);
                averageMap.put(counts[j], average);
            }
            price.setAverage(averageMap);
        }
        return priceList;
    }
}
