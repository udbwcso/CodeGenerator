package com.code.entity;

import com.database.util.TableUtil;
import org.apache.poi.hssf.usermodel.*;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/2.
 */
public class T {
    public static void main(String[] args) throws Exception {
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet("data");
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        Map<String, String> map = getData();

        Iterator<String> it = map.keySet().iterator();
        int rowIndex = 0;
        while (it.hasNext()){
            String key = it.next();
            HSSFRow row = sheet.createRow(rowIndex);
            rowIndex++;
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(map.get(key));
            cell.setCellStyle(style);
            cell = row.createCell(1);
            cell.setCellValue(key);
            cell.setCellStyle(style);
            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell = row.createCell(3);
            cell.setCellStyle(style);
            cell = row.createCell(4);
            cell.setCellValue("N");
            cell.setCellStyle(style);
        }
        // 第六步，将文件存到指定位置
        FileOutputStream fout = new FileOutputStream("E:/t.xls");
        wb.write(fout);
        fout.close();

    }

    public static Map<String, String> getData() throws Exception {
        Class cls = Class.forName("com.code.entity.BackOrder");
        Field[] fields = cls.getDeclaredFields();

        String tableName = "np_back_order";
        List<Map<String, Object>> columnList = TableUtil.getColumns(null, null, tableName, null);
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < columnList.size(); i++) {
            Map<String, Object> column = columnList.get(i);
            String columnName = String.valueOf((column.get("COLUMN_NAME")));
            for (Field field : fields) {
                if(columnName.replaceAll("_", "").equalsIgnoreCase(field.getName())){
                    System.out.println(field.getName() + "----" + column.get("REMARKS"));
                    map.put(field.getName(), String.valueOf(column.get("REMARKS")));
                    break;
                }
            }
        }
        return map;
    }
}
