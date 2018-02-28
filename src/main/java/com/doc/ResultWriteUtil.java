package com.doc;

import com.common.util.PropertiesUtil;
import com.database.util.TableUtil;
import com.doc.bean.Field;
import com.doc.bean.ResultBean;
import com.doc.json.JsonWrapper;
import com.ymt.wxshop.integral.domain.IntegralChangeFlows;
import com.ymt.wxshop.integral.domain.IntegralFlowsDetail;
import org.apache.commons.lang.StringUtils;
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
    private static Map<String, String> table = new HashMap<>();
    private static Map<String, ResultBean> resultBeanMap;

    public static void main(String[] args) throws Exception {
        IntegralChangeFlows integralChangeFlows = new IntegralChangeFlows();
        List<IntegralFlowsDetail> detailList = new ArrayList<>();
        IntegralFlowsDetail detail = new IntegralFlowsDetail();
        detailList.add(detail);
        integralChangeFlows.setDetails(detailList);
        System.out.println();


//        String json = Template.INSTANCE.getTemplate("BACK_ORDER_DETAIL");
        String json = JsonWrapper.writeValue(integralChangeFlows);
        jsonLevel = getMaxLevel(json);
        table.put("list", "integral_change_flows");
        table.put("details", "np_goods_info");
//        table.put("itemList", "np_back_order_item");
//        table.put("productDetail", "np_goods_info");

        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder("doc/IntegralChangeFlowsMapper.xml");
        resultBeanMap = xmlConfigBuilder.build();

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

        write(jsonObject, sheet, 0, 0, "list", 1);
        // 第六步，将文件存到指定位置
        FileOutputStream fos = new FileOutputStream("E:/t.xls");
        wb.write(fos);
        fos.close();

    }

    private static void write(JSONObject object, HSSFSheet sheet, int rowIndex, int column, String tableKey, int level) throws Exception {
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

        for (String s : columnKeyList) {
//            if(jsonLevel - level - 1 >= 1){
//                sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, column, column + jsonLevel - level - 1));
//            }
            HSSFRow row = sheet.createRow(rowIndex);
            rowIndex++;
//            HSSFCell cell = row.createCell(column);
            HSSFCell cell = row.createCell(2);
            if (StringUtils.isNotEmpty(tableKey)) {
                cell.setCellValue(tableKey + "[" + s + "]");
            } else {
                cell.setCellValue(s);
            }

            ResultBean resultBean = resultBeanMap.get(table.get(tableKey));
            List<Field> fieldList = resultBean.getFieldList();
            Field field = null;
            for (int i = 0; i < fieldList.size(); i++) {
                if (s.equals(fieldList.get(i).getProperty())) {
                    field = fieldList.get(i);
                    break;
                }
            }
            if (field == null) {
                continue;
            }

            String tableName = table.get(tableKey);
            List<Map<String, Object>> columnList = TableUtil.getColumns(null, null, tableName, null);
            Properties typeProp = PropertiesUtil.load("/properties/mysql/type_doc.properties");
            for (int i = 0; i < columnList.size(); i++) {
                Map<String, Object> columnInfo = columnList.get(i);
                String name = String.valueOf(columnInfo.get("COLUMN_NAME"));
                if (name.equals(field.getColumn())) {
                    cell = row.createCell(3);
                    cell.setCellValue(typeProp.getProperty(field.getJdbcType()));
                    cell = row.createCell(1);
                    cell.setCellValue(String.valueOf(columnInfo.get("REMARKS")));
                    break;
                }
            }
        }


        for (int i = 0; i < objKeyList.size(); i++) {
            Object value = object.get(objKeyList.get(i));
//            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, column, column + jsonLevel - level - 1));
//            HSSFRow row = sheet.createRow(rowIndex);
//            rowIndex++;
//            HSSFCell cell = row.createCell(column);
//            cell.setCellValue(objKeyList.get(i));
            if (value instanceof JSONObject) {
                write((JSONObject) value, sheet, rowIndex, column + 1, objKeyList.get(i), level + 1);
            } else if (value instanceof JSONArray) {
                write(((JSONArray) value).getJSONObject(0), sheet, rowIndex, column + 1, objKeyList.get(i),
                        level + 1);
            }
        }

    }

}
