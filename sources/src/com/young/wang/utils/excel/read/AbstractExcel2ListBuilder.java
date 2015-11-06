package com.young.wang.utils.excel.read;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

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
 * Created by Young Wang on 2015-05-29 11:42.
 */
public abstract class AbstractExcel2ListBuilder<T>
    implements ReadExcelBuilder <List<T>>{

    private List<T> result = null;

    private Map<Integer,String> rowData = null;

    protected abstract boolean isNeedAdd(Map<Integer,String> rowData,int rowIndex);
    protected abstract T createNewObject(Map<Integer,String> rowData,int rowIndex);

    @Override
    public void startWorkbook(Workbook workbook) {
        result = new ArrayList<T>();
    }

    @Override
    public void endWorkbook(Workbook workbook) {
    }

    @Override
    public boolean startSheet(Sheet sheet, int index) {
        return true;
    }

    @Override
    public void endSheet(Sheet sheet, int index) {
    }

    @Override
    public boolean startRow(Row row, int rowIndex) {
        rowData = new HashMap<Integer, String>();
        return true;
    }

    @Override
    public void endRow(Row row, int rowIndex) {
        if(isNeedAdd(rowData,rowIndex)) result.add(this.createNewObject(rowData, rowIndex));
    }

    @Override
    public void builderCell(Cell cell, int cellIndex) {
        String val ="";
        if(cell!=null){
            cell.setCellType(Cell.CELL_TYPE_STRING);
            val = cell.getStringCellValue();
        }
        if(val==null)val="";
        val = val.trim().replace("\n","").replace("\t","").replace("\r","");
        rowData.put(cellIndex+1,val);
    }

    @Override
    public List<T> getResult() {
        return result;
    }
}
