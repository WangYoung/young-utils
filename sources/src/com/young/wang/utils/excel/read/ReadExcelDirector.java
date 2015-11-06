package com.young.wang.utils.excel.read;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * |
 * |                       _oo0oo_
 * |                      o8888888o
 * |                      88" . "88
 * |                      (| -_- |)
 * |                      0\  =  /0
 * |                    ___/`---'\___
 * |                  .' \\|  南  |// '.
 * |                 / \\|||  无  |||// \
 * |                / _||||| -阿- |||||- \
 * |               |   | \\\  弥  /// |   |
 * |               | \_|  ''\-陀-/''  |_/ |
 * |               \  .-\__  '佛'  ___/-. /
 * |             ___'. .'  /--.--\  `. .'___
 * |          ."" '<  `.___\_<|>_/___.' >' "".
 * |         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * |         \  \ `_.   \_ __\ /__ _/   .-` /  /
 * |     =====`-.____`.___ \_____/___.-`___.-'=====
 * |                       `=---='
 * |
 * |     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * |
 * |			佛祖保佑	 永无BUG	 永不修改
 * |
 * Created by Young Wang on 2015-05-29 11:10.
 */
public class ReadExcelDirector {

    public static <T> T construct(File file,ReadExcelBuilder<T> builder){
        InputStream is = null;
        try{
            Workbook workbook;
            try {
                is = new BufferedInputStream(new FileInputStream(file));
                workbook = new XSSFWorkbook(is);
            } catch (Exception ex) {
                is = new BufferedInputStream(new FileInputStream(file));
                workbook = new HSSFWorkbook(is);
            }
            builder.startWorkbook(workbook);

            int sheetNumber = workbook.getNumberOfSheets();

            for(int i=0; i<sheetNumber; i++){
                Sheet sheet = workbook.getSheetAt(i);
                if(builder.startSheet(sheet,i)){
                    Iterator<Row> itRow = sheet.rowIterator();
                    while (itRow.hasNext()) {
                        Row row = itRow.next();
                        if(builder.startRow(row,row.getRowNum())){
                            Iterator<Cell> itCell = row.cellIterator();
                            while (itCell.hasNext()) {
                                Cell cell = itCell.next();
                                builder.builderCell(cell,cell.getColumnIndex());
                            }
                        }
                        builder.endRow(row,row.getRowNum());
                    }
                }
                builder.endSheet(sheet, i);
            }
            builder.endWorkbook(workbook);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("临时文件不存在，请重新上传");
        }catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("系统错误，错误消息：文件流读取异常，请联系客服");
        } finally {
            try {
                if(is!=null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.getResult();
    }

}
