package com.young.wang.utils.excel.write.sheetRowCell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Created by Young Wang on 2015-06-29 09:54.
 */
public class SheetConfig {
    private String sheetName;
    private List<RowAbstractFactory> rowList = new ArrayList<RowAbstractFactory>();
    private Map<Integer,Integer> columnWidthMap = new HashMap<Integer, Integer>();

    public SheetConfig(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<RowAbstractFactory> getRowList() {
        return rowList;
    }

    public void addRow(RowAbstractFactory rc) {
        this.rowList.add(rc);
    }

    public Map<Integer, Integer> getColumnWidthMap() {
        return columnWidthMap;
    }

    public void putColumnWidth(int columnIndex,int width) {
        this.columnWidthMap.put(columnIndex, width);
    }
}
