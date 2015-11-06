package com.young.wang.utils.jdbc.statement.processor;

import java.util.ArrayList;
import java.util.List;

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
 * Created by Young Wang on 2015-08-19 15:06.
 */
public class ProcessorValResult implements ProcessorResult {
	private String sqlStatement;
    private List<Object> values;

    public ProcessorValResult(String sqlStatement) {
        this.sqlStatement = sqlStatement;
        values = new ArrayList<Object>();
    }

    public ProcessorValResult(String sqlStatement, List<Object> values) {
        this.sqlStatement = sqlStatement;
        this.values = values;
    }

    @Override
    public String getSqlStatement() {
        return sqlStatement;
    }

    public List<Object> getValues() {
        return values;
    }
}
