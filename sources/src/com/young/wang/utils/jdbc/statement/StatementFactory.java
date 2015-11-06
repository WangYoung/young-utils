package com.young.wang.utils.jdbc.statement;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.young.wang.utils.jdbc.statement.processor.DefaultSqlProcessor;
import com.young.wang.utils.jdbc.statement.processor.InSqlProcessor;
import com.young.wang.utils.jdbc.statement.processor.SqlProcessor;
import com.young.wang.utils.jdbc.statement.processor.TopSqlProcessor;

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
 * Created by Young Wang on 2015-07-06 19:19.
 */
public class StatementFactory {

	private StatementFactory() {
        processorList.add(new TopSqlProcessor());
        processorList.add(new InSqlProcessor());
    }
    private final static StatementFactory instance = new StatementFactory();
    public static StatementFactory getInstance(){
        return instance;
    }

    private final Map<String,NamingPrepareStatement> statementCache = new HashMap<String, NamingPrepareStatement>();
    private final static int maxCacheNum = 10000;
    private final static int clearNum = 1000;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private SqlProcessor defaultProcessor = new DefaultSqlProcessor();
    private List<SqlProcessor> processorList = new ArrayList<SqlProcessor>();

    public void registerProcessor(SqlProcessor processor){
        if(processor==null)throw new NullPointerException("参数无效");
        processorList.add(processor);
    }

    public void unRegister(SqlProcessor processor){
        if(processor==null)throw new NullPointerException("参数无效");
        if(processorList.contains(processor)){
            processorList.remove(processor);
        }else{
            throw new IllegalArgumentException("未注册处理器");
        }
    }

    public PreparedStatement getPreparedStatement(String sql,Map<String,Object> params,Connection connection) throws SQLException {
        if(sql==null || sql.isEmpty())throw new NullPointerException("SQL not allow null");
        NamingPrepareStatement statement = null;
        Lock readLock = readWriteLock.readLock();
        readLock.lock();
        try{
            statement = this.statementCache.get(sql);
        }finally {
            readLock.unlock();
        }

        if(statement == null) {
            Lock writeLock = readWriteLock.writeLock();
            writeLock.lock();
            try{
                statement = this.statementCache.get(sql);
                if(statement == null) {
                    if(this.statementCache.size()>maxCacheNum){
                        //清除缓存
                        int removeCount = 0;
                        for (String s : statementCache.keySet()) {
                            if(removeCount<clearNum){
                                statementCache.remove(s);
                                removeCount++;
                            }else {
                                break;
                            }
                        }
                    }
                    statement = new NamingPrepareStatement(sql,defaultProcessor,processorList);
                    this.statementCache.put(sql, statement);
                }
            }finally {
                writeLock.unlock();
            }
        }

        return statement.builderStatement(params, connection);
    }

}
