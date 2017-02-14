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

    List<StockPrice> average(List<StockPrice> priceList, Integer[] counts, int scale);
}
