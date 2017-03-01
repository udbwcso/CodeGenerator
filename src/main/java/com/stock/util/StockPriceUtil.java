package com.stock.util;

import com.stock.bean.StockPrice;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/3/1.
 */
public class StockPriceUtil {
    /**
     * 比较
     * @param var1
     * @param var2
     * @return true, or false as val1 is greater than or less than var2.
     */
    public static boolean compare(StockPrice var1, StockPrice var2) {
        return var1.getOpeningPrice().compareTo(var2.getOpeningPrice()) >= 0
                && var1.getClosingPrice().compareTo(var2.getClosingPrice()) >= 0
                && var1.getHighestPrice().compareTo(var2.getHighestPrice()) >= 0
                && var1.getLowestPrice().compareTo(var2.getLowestPrice()) >= 0
                && var1.getTradingVolume().compareTo(var2.getTradingVolume()) >= 0
                && var1.getClosingPrice().compareTo(var1.getOpeningPrice()) >= 0;
    }

    /**
     * 是否收敛
     *
     * @param numbers
     * @param percent
     * @return
     */
    public static boolean isConvergent(BigDecimal[] numbers, BigDecimal percent) {
        Arrays.sort(numbers);
        int count = 0;
        for (int i = 1; i < numbers.length; i++) {
            BigDecimal subtract = numbers[i].subtract(numbers[i - 1]);
            BigDecimal divide = subtract.abs().divide(numbers[i], 3, BigDecimal.ROUND_HALF_UP);
            divide = divide.divide(new BigDecimal(100), 3, BigDecimal.ROUND_HALF_UP);
            if (divide.compareTo(percent) <= 0) {
                ++count;
            }
        }
        return count >= numbers.length - 2;
    }

    /**
     * 是否连续上涨
     *
     * @param riseDay 连续上涨天数
     * @return
     */
    public static boolean isContinuousRise(int riseDay, List<StockPrice> priceList) {
        int count = 0;
        for (int i = 0; i < riseDay; i++) {
            StockPrice current = priceList.get(i);
            StockPrice before = priceList.get(i + 1);
            if (StockPriceUtil.compare(current, before)) {
                ++count;
            }
        }
        return count == riseDay;
    }
}
