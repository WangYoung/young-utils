package com.young.wang.utils.jdbc.statement.processor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
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
 * Created by Young Wang on 2015-07-07 10:34.
 */
public class InSqlProcessor implements SqlProcessor{
	@Override
    public boolean isAccess(String placeholder, String beforeStr, String afterStr) {
        String trimBefore = beforeStr.trim();
        String trimAfter = afterStr.trim();
        return trimBefore.endsWith("(") && trimBefore.substring(0,trimBefore.length()-1).trim().endsWith(" in") && trimAfter.startsWith(")");
    }

    @SuppressWarnings("rawtypes")
	@Override
    public ProcessorResult processorSql(String placeholder, Map<String, Object> params) {
        if(!params.containsKey(placeholder))throw new IllegalArgumentException("参数中缺少"+placeholder);
        StringBuilder sqlStatement = new StringBuilder();
        List<Object> values = new ArrayList<Object>();

        Object val = params.get(placeholder);
        if(val!=null){
            if(val instanceof Collection){
                Collection c = (Collection) val;
                for (Object o : c) {
                    sqlStatement.append("?,");
                    values.add(o);
                }
                if(sqlStatement.length()>0) sqlStatement.deleteCharAt(sqlStatement.length()-1);
            }else if(val.getClass().isArray()){
                int size = Array.getLength(val);
                for (int i = 0;i<size; i++){
                    sqlStatement.append("?,");
                    values.add(Array.get(val,i));
                }
                if(sqlStatement.length()>0) sqlStatement.deleteCharAt(sqlStatement.length()-1);
            }else{
                sqlStatement.append("?");
                values.add(val);
            }
        }

        return new ProcessorValResult(sqlStatement.toString(),values);
    }

    @Override
    public boolean repeatProcessor() {
        return true;
    }
}
