package com.young.wang.utils.excel.write;

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
 * Created by Young Wang on 2015-06-26 20:01.
 */
public class ExportExcelDirector {

    public static ExportExcelDirector getInstance(){
        return new ExportExcelDirector();
    }

    public <T> T construct(ExportExcelBuilder<T> builder,Object params){
        builder.builderStart(params);
        ExportContext context = builder.builderWorkbook();

        if(builder.isEmptyExcel()){
            builder.builderEmptyExcel(context);
        }else{
            String[] sheetNames = builder.getSheetNames();
            for(int i = 0;i<sheetNames.length;i++){
                String sheetName = sheetNames[i];
                Sheet sheet = createSheet(context.getWorkbook(),sheetName,0);
                builder.builderSheetStart(i, sheetName, sheet, context);
                builder.builderSheetContent(i, sheetName, sheet, context);
                builder.builderSheetEnd(i, sheetName, sheet, context);
            }
        }
        builder.builderEnd(context);
        return builder.getResult();
    }

    private Sheet createSheet(Workbook workbook,String name,int i){
        try {
            if(i==0){
                return workbook.createSheet(name);
            }else {
                return workbook.createSheet(name+"-"+i);
            }
        }catch (IllegalArgumentException e){
            return createSheet(workbook,name,++i);
        }
    }


}
