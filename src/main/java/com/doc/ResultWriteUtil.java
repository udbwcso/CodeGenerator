package com.doc;

import com.common.util.PropertiesUtil;
import com.database.util.TableUtil;
import org.apache.poi.hssf.usermodel.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by Administrator on 2016/9/6.
 */
public class ResultWriteUtil {
    private static int jsonLevel = 0;

    public static void main(String[] args) throws Exception {
        String json = Template.INSTANCE.getTemplate("BACK_ORDER_DETAIL");
        jsonLevel = getMaxLevel(json);
        Map<String, String> table = new HashMap<>();
        table.put("main", "np_back_order");
        table.put("itemList", "np_back_order_item");
        table.put("productDetail", "np_goods_info");

        write(new JSONObject(json), table);
    }

    public static int getMaxLevel(String json) {
        char[] chars = json.toCharArray();
        int maxLevel = 0;
        int level = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '[' || chars[i] == '{') {
                ++level;
            } else if (chars[i] == ']' || chars[i] == '}') {
                --level;
            }
            maxLevel = level > maxLevel ? level : maxLevel;
        }
        return maxLevel;
    }

    public static void write(JSONObject jsonObject, Map<String, String> table) throws Exception {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("data");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        // 创建一个居中格式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        write(jsonObject, sheet, 0, 0, table, "main");
        // 第六步，将文件存到指定位置
        FileOutputStream fos = new FileOutputStream("E:/t.xls");
        wb.write(fos);
        fos.close();

    }

    private static void write(JSONObject object, HSSFSheet sheet, int rowIndex, int column, Map<String, String>
            table, String tableKey) throws Exception {
        Iterator<String> it = object.keySet().iterator();
        List<String> objKeyList = new ArrayList<>();
        List<String> columnKeyList = new ArrayList<>();
        while (it.hasNext()) {
            String key = it.next();
            Object value = object.get(key);
            if (value instanceof JSONObject
                    || value instanceof JSONArray) {
                objKeyList.add(key);
            } else {
                columnKeyList.add(key);
            }
        }
        String tableName = table.get(tableKey);
        List<Map<String, Object>> columnList = TableUtil.getColumns(null, null, tableName, null);



        Properties typeProp = PropertiesUtil.load("/properties/mysql/type_doc.properties");
        for (String s : columnKeyList) {
            HSSFRow row = sheet.createRow(rowIndex);
            rowIndex++;
            HSSFCell cell = row.createCell(column);
            cell.setCellValue(s);
            for (int i = 0; i < columnList.size(); i++) {
                Map<String, Object> columnInfo = columnList.get(i);
                String name = String.valueOf(columnInfo.get("COLUMN_NAME")).replaceAll("_", "");
                if(s.equalsIgnoreCase(name)){
                    cell = row.createCell(jsonLevel - 1);
                    String type = String.valueOf(columnInfo.get("TYPE_NAME"));
                    cell.setCellValue(typeProp.getProperty(type));
                    cell = row.createCell(jsonLevel);
                    cell.setCellValue(String.valueOf(columnInfo.get("REMARKS")));
                }
            }
        }


        for (int i = 0; i < objKeyList.size(); i++) {
            Object value = object.get(objKeyList.get(i));
            HSSFRow row = sheet.createRow(rowIndex);
            rowIndex++;
            HSSFCell cell = row.createCell(column);
            cell.setCellValue(objKeyList.get(i));
            if (value instanceof JSONObject) {
                write((JSONObject) value, sheet, rowIndex, column + 1, table, objKeyList.get(i));
            } else if (value instanceof JSONArray) {
                write(((JSONArray) value).getJSONObject(0), sheet, rowIndex, column + 1, table, objKeyList.get(i));
            }
        }

    }

}
