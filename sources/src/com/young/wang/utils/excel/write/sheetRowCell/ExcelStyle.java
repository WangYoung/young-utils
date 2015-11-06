package com.young.wang.utils.excel.write.sheetRowCell;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
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
 * Created by Young Wang on 2015-06-29 10:17.
 */
public enum ExcelStyle {
    LARGE{
        @Override
        public CellStyle getStyle(Workbook workbook){
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 20);// 设置字体大小
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);//加粗
            style.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上线居中
            style.setFont(font);
            return style;
        }
    },
    H1{
        @Override
        public CellStyle getStyle(Workbook workbook){
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 16);// 设置字体大小
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);//加粗
            style.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上线居中
            style.setFont(font);
            return style;
        }
    },
    H2{
        @Override
        public CellStyle getStyle(Workbook workbook){
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 15);// 设置字体大小
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);//加粗
            style.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上线居中
            style.setFont(font);
            return style;
        }
    },
    H3{
        @Override
        public CellStyle getStyle(Workbook workbook){
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 14);// 设置字体大小
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);//加粗
            style.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上线居中
            style.setFont(font);
            return style;
        }
    },
    LABEL{
        @Override
        public CellStyle getStyle(Workbook workbook){
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 13);// 设置字体大小
            font.setBoldweight(Font.BOLDWEIGHT_BOLD);//加粗
            style.setAlignment(CellStyle.ALIGN_CENTER);// 左右居中
            style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 上下居中
            style.setWrapText(false);
            style.setFont(font);
            return style;
        }
    },
    TEXT_LEFT{
        @Override
        public CellStyle getStyle(Workbook workbook){
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setFontName("Arial");
            style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
            style.setWrapText(true);
            style.setFont(font);
            return style;
        }
    },
    TEXT_CENTER{
        @Override
        public CellStyle getStyle(Workbook workbook){
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setFontHeightInPoints((short) 12);
            font.setFontName("Arial");
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
            style.setWrapText(true);
            style.setFont(font);
            return style;
        }
    };
    public abstract CellStyle getStyle(Workbook workbook);
}
