package com.stock.bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/22.
 */
public class StockPrice {
    /**
     * 日期
     */
    private Date date;
    /**
     * 开盘价
     */
    private BigDecimal openingPrice;
    /**
     * 最高价
     */
    private BigDecimal highestPrice;
    /**
     * 收盘价
     */
    private BigDecimal closingPrice;
    /**
     * 最低价
     */
    private BigDecimal lowestPrice;
    /**
     * 交易量(股)
     */
    private BigDecimal tradingVolume;
    /**
     * 交易金额(元)
     */
    private BigDecimal amount;

    /**
     * 平均值
     */
    private Map<Integer, BigDecimal> average;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getOpeningPrice() {
        return openingPrice;
    }

    public void setOpeningPrice(BigDecimal openingPrice) {
        this.openingPrice = openingPrice;
    }

    public BigDecimal getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(BigDecimal highestPrice) {
        this.highestPrice = highestPrice;
    }

    public BigDecimal getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(BigDecimal closingPrice) {
        this.closingPrice = closingPrice;
    }

    public BigDecimal getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(BigDecimal lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public BigDecimal getTradingVolume() {
        return tradingVolume;
    }

    public void setTradingVolume(BigDecimal tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Map<Integer, BigDecimal> getAverage() {
        return average;
    }

    public void setAverage(Map<Integer, BigDecimal> average) {
        this.average = average;
    }
}
