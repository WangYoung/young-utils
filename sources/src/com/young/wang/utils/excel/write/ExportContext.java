package com.young.wang.utils.excel.write;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import com.young.wang.utils.excel.write.sheetRowCell.ExcelStyle;

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
 * Created by Young Wang on 2015-06-29 19:56.
 */
public class ExportContext {

    private Workbook workbook;
    private Map<ExcelStyle, CellStyle> namedStyles = new HashMap<ExcelStyle, CellStyle>();


    public ExportContext(Workbook workbook) {
        this.workbook = workbook;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public CellStyle getNamedStyle(ExcelStyle style) {
        return namedStyles.get(style);
    }

    public CellStyle createNamedStyle(ExcelStyle style) {
        CellStyle cellStyle = style.getStyle(workbook);
        namedStyles.put(style, cellStyle);
        return cellStyle;
    }
}
