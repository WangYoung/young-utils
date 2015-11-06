package com.young.wang.utils.excel.write;

import org.apache.poi.ss.usermodel.Sheet;

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
 * Created by Young Wang on 2015-06-26 20:11.
 */
public interface ExportExcelBuilder<T> {
    void builderStart(Object params);
    ExportContext builderWorkbook();
    boolean isEmptyExcel();
    void builderEmptyExcel(ExportContext context);
    String[] getSheetNames();
    void builderSheetStart(int index,String name,Sheet sheet,ExportContext context);
    void builderSheetContent(int index,String name,Sheet sheet,ExportContext context);
    void builderSheetEnd(int index,String name,Sheet sheet,ExportContext context);
    void builderEnd(ExportContext context);

    T getResult();

}
