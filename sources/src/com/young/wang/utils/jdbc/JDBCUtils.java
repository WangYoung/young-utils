package com.young.wang.utils.jdbc;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.young.wang.utils.jdbc.result.ResultFactory;
import com.young.wang.utils.jdbc.result.ResultMap;
import com.young.wang.utils.jdbc.statement.StatementFactory;

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
 * Created by Young Wang on 2015-07-02 13:16.
 */
public final class JDBCUtils {
    private final static JDBCUtils jdbcUtils = new JDBCUtils();

    private JDBCUtils() {
    }

    public static JDBCUtils getInstance(){
        return jdbcUtils;
    }

    public List<ResultMap> executeQuery(String sql,Map<String,Object> params,Connection connection) throws SQLException {
        if(sql==null)throw new NullPointerException();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = StatementFactory.getInstance().getPreparedStatement(sql, params, connection);
            rs = ps.executeQuery();
            return ResultFactory.getInstance().getResult(rs);
        } finally{
            DbUtils.closeQuietly(ps,rs);
        }
    }

    public <T> List<T> executeQuery(String sql,Map<String,Object> params,Connection connection,Class<T> clz) throws SQLException {
        if(sql==null)throw new NullPointerException();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = StatementFactory.getInstance().getPreparedStatement(sql, params, connection);
            rs = ps.executeQuery();
            List<ResultMap> list = ResultFactory.getInstance().getResult(rs);
            List<T> ret = new ArrayList<T>();
            for (ResultMap map : list) {
                ret.add(map.toDto(clz));
            }
            return ret;
        } finally{
            DbUtils.closeQuietly(ps,rs);
        }
    }

    public ResultMap executeUniqueQuery(String sql,Map<String,Object> params,Connection connection)throws SQLException{
        List<ResultMap> list = executeQuery(sql,params,connection);
        if(list==null || list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    public <T> T executeUniqueQuery(String sql,Map<String,Object> params,Connection connection,Class<T> clz)throws SQLException{
        List<ResultMap> list = executeQuery(sql,params,connection);
        if(list==null || list.isEmpty()){
            return null;
        }else{
            ResultMap map = list.get(0);
            return map.toDto(clz);
        }
    }

    public int executeUpdate(String sql,Map<String,Object> params,Connection connection)throws SQLException{
        if(sql==null)return 0;
        PreparedStatement ps = null;
        try {
            ps = StatementFactory.getInstance().getPreparedStatement(sql, params, connection);
            return ps.executeUpdate();
        }finally{
            DbUtils.closeQuietly(ps);
        }
    }

}
