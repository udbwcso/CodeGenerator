package com.stock.service;

import com.stock.bean.StockPrice;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2017/1/22.
 */
public interface CalculateService {
    /**
     * 平均值
     * @param numbers
     * @param count
     * @return
     */
    BigDecimal getAverage(BigDecimal[] numbers, int count, int scale);

    /**
     * 移动平均值
     * @param numbers
     * @param fast
     * @param slow
     * @param standard
     * @return
     */
    BigDecimal getMovingAverage(BigDecimal[] numbers, int fast, int slow, int standard);

    List<StockPrice> average(List<StockPrice> priceList, Integer[] counts, int scale);
}
