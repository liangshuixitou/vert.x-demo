package org.nit.dqmserver.util;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.poi.hssf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author eda
 * @date 2018/9/4
 */

public class ExcelUtil {
    /**
     * convert json to excel
     * @param data
     * @param sheetName
     * @return
     */
    public static ByteArrayOutputStream getExcelFile(JsonArray data, String sheetName){
        if(data==null||data.size()==0){
            return null;
        }
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet(sheetName);
        HSSFRow tableHead = sheet.createRow(0);
        HSSFCellStyle style = workBook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        JsonObject item = data.getJsonObject(0);
        ArrayList<String> keyList = new ArrayList<>();
        int index = 0;
        for(String key:item.fieldNames()){
            tableHead.createCell(index,HSSFCell.CELL_TYPE_STRING).setCellValue(key);
            keyList.add(index, key);
            index++;
        }
        for(int i=0;i<data.size();i++){
            JsonObject jsonData = data.getJsonObject(i);
            HSSFRow row = sheet.createRow(i+1);
            for(int j=0;j<keyList.size();j++){
                String key = keyList.get(j);
                row.createCell(j,HSSFCell.CELL_TYPE_STRING)
                        .setCellValue(jsonData.getValue(key,"").toString());
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workBook.write(bos);
        } catch (IOException e) {
            throw new RuntimeException("export excel error");
        }
        return bos;
    }
}
