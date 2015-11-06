package com.young.wang.utils.excel.write;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
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
 * Created by Young Wang on 2015-06-27 10:13.
 */
public abstract class AbstractExportExcelBuilder implements ExportExcelBuilder<ExcelDataDto> {

    protected Object params;
    protected Workbook workbook;
    protected ExportContext context;

    protected abstract void init(Object params);

    @Override
    public void builderStart(Object params) {
        this.params = params;
        init(this.params);
    }

    @Override
    public ExportContext builderWorkbook() {
        workbook = new HSSFWorkbook();
        context = new ExportContext(workbook);
        for (ExcelStyle style : ExcelStyle.values()) {
            context.createNamedStyle(style);
        }
        return context;
    }

    @Override
    public void builderEmptyExcel(ExportContext context) {
        Sheet sheet = workbook.createSheet("第一页");
        Row row = sheet.createRow(0);
        row.setHeightInPoints(30);
        Cell cell = row.createCell(0);
        cell.setCellStyle(ExcelStyle.LARGE.getStyle(workbook));
        cell.setCellValue(new HSSFRichTextString("没有任何数据"));
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));
    }

    @Override
    public void builderEnd(ExportContext context) {

    }

    @Override
    public ExcelDataDto getResult() {
        byte[] data = null;
        ByteOutputStream bos = null;
        try {
            bos= new ByteOutputStream();
            workbook.write(bos);
            data = bos.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("流异常");
        }finally{
            if(bos!=null)
                bos.close();
        }
        ExcelDataDto dataDto = new ExcelDataDto();
        dataDto.setData(data);
        dataDto.setFileName(getTitle()+"--管信.xls");
        dataDto.setTitle(getTitle());
        return dataDto;
    }

    protected abstract String getTitle();
}
