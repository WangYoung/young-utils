package com.young.wang.utils.excel.write.sheetRowCell;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
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
 * Created by Young Wang on 2015-06-27 14:04.
 */
public class CellFactory implements CellAbstractFactory {

    private int index;
    private String value;
    private ExcelStyle style;

    public CellFactory(int index,String value, ExcelStyle style) {
        this.value = value;
        this.style = style;
        this.index = index;
    }

    @Override
    public void produce(Row row, Sheet sheet, ExportContext context) {
        Cell cell = row.createCell(index);
        cell.setCellStyle(context.getNamedStyle(style));
        cell.setCellValue(new HSSFRichTextString(replaceIllegality(value)));
    }

    private String replaceIllegality(String str){
        if(str==null || str.isEmpty())return "";
        if(str.contains("*"))str = str.replace("*", "x");
        if(str.contains("["))str = str.replace("[", "{");
        if(str.contains("]"))str = str.replace("]", "}");
        return str;
    }
}
