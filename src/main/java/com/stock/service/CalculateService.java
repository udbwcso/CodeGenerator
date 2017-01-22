package com.stock.service;

import java.math.BigDecimal;

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
    BigDecimal getAverage(BigDecimal[] numbers, int count);

    /**
     * 移动平均值
     * @param numbers
     * @param fast
     * @param slow
     * @param standard
     * @return
     */
    BigDecimal getMovingAverage(BigDecimal[] numbers, int fast, int slow, int standard);
}
