package com.young.wang.utils.excel.write.sheetRowCell;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

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
 * Created by Young Wang on 2015-06-27 13:50.
 */
public class RangeFactory implements RowAbstractFactory,CellAbstractFactory {
    private CellRangeAddress cellRangeAddress;

    public RangeFactory(int firstRow, int lastRow, int firstCol, int lastCol) {
        cellRangeAddress = new CellRangeAddress(firstRow,lastRow,firstCol,lastCol);
    }

    @Override
    public void produce(Sheet sheet, ExportContext context) {
        sheet.addMergedRegion(cellRangeAddress);
    }

    @Override
    public void produce(Row row, Sheet sheet, ExportContext context) {
        sheet.addMergedRegion(cellRangeAddress);
    }
}
