package com.young.wang.utils.jdbc.statement;


import java.util.Map;

import com.young.wang.utils.jdbc.statement.processor.ProcessorKeyResult;
import com.young.wang.utils.jdbc.statement.processor.ProcessorResult;
import com.young.wang.utils.jdbc.statement.processor.SqlProcessor;

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
 * Created by Young Wang on 2015-08-19 10:35.
 */
public class BreakSqlStatement {
	private SyntaxType type;
    private String statement;
    private SqlProcessor processor;

    public BreakSqlStatement(String statement) {
        this.statement = statement;
        this.type = SyntaxType.TEXT;
    }

    public BreakSqlStatement(String placeholder, SqlProcessor processor) {
        this.statement = placeholder;
        this.processor = processor;
        this.type = SyntaxType.PLACEHOLDER;
    }

    public ProcessorResult compile(Map<String,Object> params){
        if(type==SyntaxType.TEXT){
            return new ProcessorKeyResult(statement);
        }else{
            return this.processor.processorSql(statement,params);
        }
    }

    public boolean repeatProcessor() {
        return type != SyntaxType.TEXT && this.processor.repeatProcessor();
    }

}
