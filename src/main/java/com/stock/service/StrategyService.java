package com.stock.service;

import com.stock.bean.Stock;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by Administrator on 2017/1/23.
 */
public interface StrategyService {

    void test(Stock stock) throws IOException, ParseException;
}
