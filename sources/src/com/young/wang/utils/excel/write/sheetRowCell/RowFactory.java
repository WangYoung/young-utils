package com.young.wang.utils.excel.write.sheetRowCell;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.young.wang.utils.excel.write.ExportContext;

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
 * Created by Young Wang on 2015-06-27 13:43.
 */
public class RowFactory implements RowAbstractFactory{
    private int index;
    private float height;
    private List<CellAbstractFactory> cellList = new ArrayList<CellAbstractFactory>();

    public RowFactory(int index, float height) {
        this.index = index;
        this.height = height;
    }

    public void addCellFactory(CellAbstractFactory cellFactory){
        cellList.add(cellFactory);
    }

    @Override
    public void produce(Sheet sheet, ExportContext context) {
        Row row = sheet.createRow(index);
        if(height>0){
            row.setHeightInPoints(height);
        }
        for (CellAbstractFactory factory : cellList) {
            factory.produce(row,sheet,context);
        }
    }
}
