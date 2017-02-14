package com.stock.service;

import com.stock.bean.Stock;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Administrator on 2017/2/13.
 */
public interface StockDataWriter {
    /**
     * 保存股票价格信息
     * @param stock 股票信息
     * @param directory 存储数据的根目录
     * @param append 写文件时是否是追加
     * @throws IOException
     * @throws ParseException
     */
    void storeData(Stock stock, String directory, boolean append) throws IOException, ParseException;

}
