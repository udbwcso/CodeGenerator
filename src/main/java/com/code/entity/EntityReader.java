package com.code.entity;

import com.database.util.TableUtil;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;

import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/2.
 */
public class EntityReader {
    public static void main(String[] args) throws Exception {
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

        // 创建word文档,并设置纸张的大小
        Document document = new Document(PageSize.A4);
        RtfWriter2.getInstance(document, new FileOutputStream("E:/word.doc"));
        document.open();
        Table table = new Table(5, map.keySet().size());
        Iterator<String> it = map.keySet().iterator();
//        table.setBackgroundColor();
        while (it.hasNext()){
            String key = it.next();
            table.addCell(map.get(key));
            table.addCell(key);
            table.addCell("");
            table.addCell("");
            table.addCell("");
        }
        document.add(table);
        document.close();
    }

}
