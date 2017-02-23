package com.stock.util;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;

/**
 * Created by Administrator on 2017/1/18.
 */
public class HttpStockDateUtil {

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println(getCurrentData("002407", "sz"));
    }


    /**
     *
     * @param code
     * @param listingSpot
     * @return
     * @throws IOException
     */
    public static String getCurrentData(String code, String listingSpot) throws IOException {
        String url = "http://hq.sinajs.cn/list=" + listingSpot + code;
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(new HttpGet(url));
        int statusCode = response.getStatusLine().getStatusCode();
        if(statusCode == 200) {
            StringWriter sw = new StringWriter();
            IOUtils.copy(response.getEntity().getContent(), sw, "GBK");
            String string = sw.toString();
            return string;
        }
        return null;
    }


}
