package com.young.wang.utils.jdbc.statement;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.young.wang.utils.jdbc.statement.processor.ProcessorKeyResult;
import com.young.wang.utils.jdbc.statement.processor.ProcessorResult;
import com.young.wang.utils.jdbc.statement.processor.ProcessorValResult;
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
 * Created by Young Wang on 2015-08-18 19:38.
 */
public class NamingPrepareStatement {
	private static final Pattern breakPattern = Pattern.compile(":(\\w+)");

    private String sourceSql;
    private List<BreakSqlStatement> syntaxList = new ArrayList<BreakSqlStatement>();

    private List<Object> cache = new ArrayList<Object>();

    public NamingPrepareStatement(String sql,SqlProcessor defaultProcessor,List<SqlProcessor> processorList) {
        if(sql==null || sql.isEmpty() || sql.trim().isEmpty()){
            throw new RuntimeException("SQL not allow null");
        }
        this.sourceSql = sql;

        analyzeSql(defaultProcessor, processorList);
    }

    private void analyzeSql(SqlProcessor defaultProcessor,List<SqlProcessor> processorList){
        Matcher matcher = breakPattern.matcher(this.sourceSql);
        int lastEnd = 0;
        while (matcher.find()){
            int matcherStart = matcher.start();
            int matcherEnd = matcher.end();

            String beforeStr = this.sourceSql.substring(lastEnd, matcherStart);
            String key = matcher.group(1);
            String afterStr = this.sourceSql.substring(matcherEnd);
            syntaxList.add(new BreakSqlStatement(beforeStr));

            BreakSqlStatement sqlStatement=null;
            for (SqlProcessor processor : processorList) {
                if(processor.isAccess(key,beforeStr,afterStr)){
                    sqlStatement = new BreakSqlStatement(key,processor);
                    break;
                }
            }
            if(sqlStatement==null)sqlStatement = new BreakSqlStatement(key,defaultProcessor);
            syntaxList.add(sqlStatement);

            lastEnd = matcherEnd;
        }
        syntaxList.add(new BreakSqlStatement(this.sourceSql.substring(lastEnd)));
    }

    private ProcessorValResult compileStatement(Map<String,Object> params){
        StringBuilder sql = new StringBuilder();
        List<Object> values = new ArrayList<Object>();

        if(this.cache.isEmpty()){
            try {
                for (BreakSqlStatement sqlStatement : syntaxList) {
                    ProcessorResult result = sqlStatement.compile(params);
                    sql.append(result.getSqlStatement());
                    if(result instanceof ProcessorKeyResult){
                        List<String> keys = ((ProcessorKeyResult) result).getKeys();
                        for (String key : keys) {
                            if(!params.containsKey(key))throw new NullPointerException("param "+key+" has not found!");
                            values.add(params.get(key));
                        }
                    }else if (result instanceof ProcessorValResult){
                        values.addAll(((ProcessorValResult) result).getValues());
                    }

                    if(!sqlStatement.repeatProcessor() && result instanceof ProcessorKeyResult){
                        if(this.cache.isEmpty()){
                            List<String> keys = new ArrayList<String>();
                            keys.addAll(((ProcessorKeyResult) result).getKeys());
                            this.cache.add(new ProcessorKeyResult(result.getSqlStatement(),keys));
                        }else{
                            Object lastElement = this.cache.get(this.cache.size()-1);
                            if(lastElement instanceof ProcessorKeyResult){
                                ProcessorKeyResult lastResult = (ProcessorKeyResult) lastElement;
                                List<String> keys = new ArrayList<String>();
                                keys.addAll(lastResult.getKeys());
                                keys.addAll(((ProcessorKeyResult) result).getKeys());
                                this.cache.set(this.cache.size()-1,new ProcessorKeyResult(lastResult.getSqlStatement() + result.getSqlStatement(),keys));
                            }else {
                                List<String> keys = new ArrayList<String>();
                                keys.addAll(((ProcessorKeyResult) result).getKeys());
                                this.cache.add(new ProcessorKeyResult(result.getSqlStatement(),keys));
                            }
                        }
                    }else{
                        this.cache.add(sqlStatement);
                    }
                }
            }catch (RuntimeException e){
                this.cache.clear();
                throw e;
            }
        }else{
            for (Object o : cache) {
                if(o instanceof ProcessorKeyResult){
                    ProcessorKeyResult keyResult = (ProcessorKeyResult) o;
                    sql.append(keyResult.getSqlStatement());
                    List<String> keys = keyResult.getKeys();
                    for (String key : keys) {
                        if(!params.containsKey(key))throw new NullPointerException("param "+key+" has not found!");
                        values.add(params.get(key));
                    }
                }else if(o instanceof BreakSqlStatement){
                    BreakSqlStatement statement = (BreakSqlStatement) o;
                    ProcessorResult result = statement.compile(params);
                    sql.append(result.getSqlStatement());
                    if(result instanceof ProcessorKeyResult){
                        List<String> keys = ((ProcessorKeyResult) result).getKeys();
                        for (String key : keys) {
                            if(!params.containsKey(key))throw new NullPointerException("param "+key+" has not found!");
                            values.add(params.get(key));
                        }
                    }else if (result instanceof ProcessorValResult){
                        values.addAll(((ProcessorValResult) result).getValues());
                    }
                }
            }
        }

        return new ProcessorValResult(sql.toString(),values);
    }

    public PreparedStatement builderStatement(Map<String,Object> params,Connection connection) throws SQLException {
        ProcessorValResult result = this.compileStatement(params);
        PreparedStatement ps = connection.prepareStatement(result.getSqlStatement());
        List<Object> values = result.getValues();
        for (int i = 0; i < values.size(); i++) {
            Object o = values.get(i);
            if(o!=null){
                if(o.getClass().isEnum()){
                    o = o.toString();
                }else if(o instanceof Date){
                    o = new Timestamp(((Date) o).getTime());
                }
            }
            ps.setObject(i+1,o);
        }
        return ps;
    }

}
