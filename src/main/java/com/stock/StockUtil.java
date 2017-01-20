package com.stock;

import com.doc.JsonWrapper;
import com.office.ExcelReader;
import com.stock.bean.Stock;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/1/18.
 */
public class StockUtil {

    public static void main(String[] args) throws IOException, ParseException {
        String shDataDirectory = "D:\\stock\\sh";
        String szDataDirectory = "D:\\stock\\sz";
        String szPath = "E:\\stock\\A\\深圳A股列表.xlsx";
        String shPath = "E:\\stock\\A\\上海A股.xlsx";
        List<Stock> shStockList = getStockList(shPath, 2, 4);
        List<Stock> szStockList = getStockList(szPath, 5, 7);
        storeDate(szStockList, szDataDirectory);
    }

    public static void storeDate(List<Stock> stockList, String dataDirectory) {
        List<Stock> list = new ArrayList<>();
        for (int i = 0; i < stockList.size(); i++) {
            list.add(stockList.get(i));
            if((i + 1) % 20 == 0 || i == stockList.size() - 1) {
                StockData stockData = new StockData(list, dataDirectory);
                Thread thread = new Thread(stockData);
                thread.start();
                list = new ArrayList<>();
            }
        }
    }

    public static List<Stock> getStockList(String path, int codeCell, int dateCell) throws ParseException, IOException {
        List<String[]> list = ExcelReader.read(path);
        List<Stock> stockList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i < list.size(); i++) {
            String[] strings = list.get(i);
            Stock stock = new Stock();
            stock.setCode(strings[codeCell]);
            stock.setListingDate(sdf.parse(strings[dateCell]));
            stockList.add(stock);
        }
        return stockList;
    }

    public static List<Stock> getStockList(String path, String charset) throws IOException {
        String string = FileUtils.readFileToString(new File(path), charset);
        String[] values = string.split(System.getProperty("line.separator"));
        List<Stock> stockList = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            String[] stockInfo = values[i].split("\\(");
            Stock stock = new Stock();
            stock.setName(stockInfo[0]);
            stock.setCode(stockInfo[1].replace(")", ""));
            stockList.add(stock);
        }
        return stockList;
    }

    public static void getHistoryDate(String directory, Stock stock, int year, int quarter) throws IOException {
        List<String[]> list = search(stock.getCode(), year, quarter);
        System.out.println(JsonWrapper.writeValue(stock) + "----" + year + "--" + quarter + "--" + list.size());
        String fileName = stock.getCode() + ".txt";
        String filePath = directory + File.separator + fileName;
        String lineSeparator = System.getProperty("line.separator");
        for (int i = 0; i < list.size(); i++) {
            String[] strings = list.get(i);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < strings.length; j++) {
                sb.append(strings[j] + " ");
            }
            sb.append(lineSeparator);
            FileUtils.writeStringToFile(new File(filePath), sb.toString(), true);
        }
    }

    public static String getCurrentDate(String code, String address) throws IOException {
        String url = "http://hq.sinajs.cn/list=" + address + code;
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(new HttpGet(url));
        int statusCode = response.getStatusLine().getStatusCode();
        String filePath = "E:\\workspace\\code\\src\\main\\java\\com\\stock\\data.txt";
        if(statusCode == 200) {
            StringWriter sw = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), sw, "GBK");
            String string = sw.toString();
            FileUtils.writeStringToFile(new File(filePath), string, true);
            return string;
        }
        return null;
    }

    public static List<String[]> search(String stock, int year, int quarter) {
        InputStream is = null;
        Document doc = null;
        // create URL string
        String url = String.format("http://money.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/%s.phtml?year=%s&jidu=%s",
                stock, year, quarter);
        try {
            // parse html by Jsoup
            doc = Jsoup.parse(new URL(url), 60000);
            //取所有查询结果
            Element element = doc.getElementById("FundHoldSharesTable");
            Elements children = element.select("tr");
            List<String[]> list = new ArrayList<>();
            for (int i = 0; i < children.size(); i++) {
                Element child = children.get(i);
                Elements tds = child.select("td");
                String[] values = new String[tds.size()];
                for (int j = 0; j < tds.size(); j++) {
                    Element td = tds.get(j);
                    String value = td.html();
                    value = value.replaceAll("<.*?>", "").trim();
                    values[j] = value;
                }
                list.add(values);
            }
            return list;
        } catch (Exception e) {
//            log.log(Level.SEVERE, "search column error", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        return Collections.emptyList();
    }
}
