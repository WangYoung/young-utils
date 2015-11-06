package com.young.wang.utils.excel.write;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.young.wang.utils.excel.write.sheetRowCell.CellFactory;
import com.young.wang.utils.excel.write.sheetRowCell.ExcelStyle;
import com.young.wang.utils.excel.write.sheetRowCell.RangeFactory;
import com.young.wang.utils.excel.write.sheetRowCell.RowAbstractFactory;
import com.young.wang.utils.excel.write.sheetRowCell.RowFactory;
import com.young.wang.utils.excel.write.sheetRowCell.SheetConfig;

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
 * Created by Young Wang on 2015-06-27 13:20.
 */
public class DefaultExportExcelBuilder extends AbstractExportExcelBuilder {

    private DataConfigAbstractFactory factory;
    private ExportDataConfig dataConfig;

    public DefaultExportExcelBuilder(DataConfigAbstractFactory factory) {
        this.factory = factory;
    }

    @Override
    protected void init(Object params) {
        dataConfig = factory.produce(params);
    }

    @Override
    protected String getTitle() {
        return dataConfig.getTitle();
    }

    @Override
    public boolean isEmptyExcel() {
        return dataConfig.getSheetList().isEmpty();
    }

    @Override
    public String[] getSheetNames() {
        String[] names = new String[dataConfig.getSheetList().size()];
        for(int i = 0, count = names.length; i<count;i++){
            names[i] = dataConfig.getSheetList().get(i).getSheetName();
        }
        return names;
    }

    @Override
    public void builderSheetStart(int index, String name, Sheet sheet, ExportContext context) {
        SheetConfig sheetConfig = dataConfig.getSheetList().get(index);
        List<RowAbstractFactory> rowList = sheetConfig.getRowList();
        if(rowList.isEmpty()){
            RowFactory row = new RowFactory(0,40);
            row.addCellFactory(new CellFactory(0,"没有数据", ExcelStyle.H1));
            sheetConfig.addRow(row);
            sheetConfig.addRow(new RangeFactory(0,0,0,8));
        }
    }

    @Override
    public void builderSheetContent(int index, String name, Sheet sheet, ExportContext context) {
        SheetConfig sheetConfig = dataConfig.getSheetList().get(index);
        List<RowAbstractFactory> rowList = sheetConfig.getRowList();
        for (RowAbstractFactory row : rowList) {
            row.produce(sheet,context);
        }
    }

    @Override
    public void builderSheetEnd(int index, String name, Sheet sheet, ExportContext context) {
        SheetConfig sheetConfig = dataConfig.getSheetList().get(index);
        Map<Integer,Integer> columnWidthMap = sheetConfig.getColumnWidthMap();
        for (Map.Entry<Integer, Integer> entry : columnWidthMap.entrySet()) {
            sheet.setColumnWidth(entry.getKey(),entry.getValue());
        }
    }
}
