package com.young.wang.utils.jdbc.statement.processor;

import java.util.ArrayList;
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
 * Created by Young Wang on 2015-07-07 10:14.
 */
public class DefaultSqlProcessor implements SqlProcessor {
	@Override
    public boolean isAccess(String placeholder, String beforeStr, String afterStr) {
        return true;
    }

    @Override
    public ProcessorResult processorSql(String placeholder, Map<String, Object> params) {
        String sqlStatement = "?";
        List<String> key = new ArrayList<String>();
        key.add(placeholder);
        return new ProcessorKeyResult(sqlStatement,key);
    }

    @Override
    public boolean repeatProcessor() {
        return false;
    }
}
