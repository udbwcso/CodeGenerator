package com.stock.service;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/1/22.
 */
public class CalculateServiceImpl implements CalculateService{
    @Override
    public BigDecimal getAverage(BigDecimal[] numbers, int count) {
        if(numbers == null || numbers.length < count) {
            throw new IllegalArgumentException("numbers is Illegal");
        }
        BigDecimal sum = new BigDecimal(0);
        for (int i = 0; i < count; i++) {
            sum = sum.add(numbers[i]);
        }
        return sum.divide(new BigDecimal(count), 3, BigDecimal.ROUND_DOWN);
    }

    @Override
    public BigDecimal getMovingAverage(BigDecimal[] numbers, int fast, int slow, int standard) {
        return null;
    }
}
