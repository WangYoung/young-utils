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
 * Created by Young Wang on 2015-08-19 15:02.
 */
public class ProcessorKeyResult implements ProcessorResult{
	private String sqlStatement;
    private List<String> keys;

    public ProcessorKeyResult(String sqlStatement) {
        this.sqlStatement = sqlStatement;
        keys = new ArrayList<String>();
    }

    public ProcessorKeyResult(String sqlStatement, List<String> keys) {
        this.sqlStatement = sqlStatement;
        this.keys = keys;
    }

    @Override
    public String getSqlStatement() {
        return sqlStatement;
    }

    public List<String> getKeys() {
        return keys;
    }
}
