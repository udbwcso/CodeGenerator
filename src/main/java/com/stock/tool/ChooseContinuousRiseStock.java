package com.stock.tool;

/**
 * Created by Administrator on 2017/2/22.
 */
public class ChooseContinuousRiseStock {
//    public static void main(String[] args) throws IOException, ParseException {
//        StockDataReader stockDataService = new StockDataFileReader();
//        List<Stock> stockList = stockDataService.getStockList(ListingSpot.SZ);
//        for (int i = 0; i < stockList.size(); i++) {
//            Stock stock = stockList.get(i);
//            List<StockPrice> priceList = stockDataService.getStockPriceList(stock);
//
//            CalculateService calculateService = new CalculateServiceImpl();
//            Integer[] days = new Integer[]{5, 10, 20, 30, 60};
//            if (priceList.size() < days[days.length - 1] + days[0]) {
//                continue;
//            }
//            calculateService.average(priceList, days, 3);
//            int riseDay = 4;
//            int count = 0;
//            for (int j = 0; j < riseDay; j++) {
//                StockPrice current = priceList.get(j);
//                StockPrice before = priceList.get(j + 1);
//                if(current.getOpeningPrice().compareTo(before.getOpeningPrice()) >= 0
//                        && current.getClosingPrice().compareTo(before.getClosingPrice()) >= 0
//                        && current.getHighestPrice().compareTo(before.getHighestPrice()) >= 0
//                        && current.getLowestPrice().compareTo(before.getLowestPrice()) >= 0
//                        && current.getTradingVolume().compareTo(current.getTradingVolume()) >= 0
//                        && current.getClosingPrice().compareTo(current.getOpeningPrice()) >= 0) {
//                    ++count;
//                }
//            }
//            if(count == riseDay) {
//                System.out.println(stock.getCode());
//            }
//        }
//    }
}
