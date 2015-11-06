/**
 * 
 */
package com.young.wang.utils.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
 * 
 * Created by Young Wang on 2015-07-02 13:16.
 *
 */
public final class DbUtils {
	private DbUtils() {
    }

    public static void closeQuietly(Connection con, Statement statement, ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            } catch (Exception var6) {
                ;
            }
        }

        if(statement != null) {
            try {
                statement.close();
            } catch (Exception var5) {
                ;
            }
        }

        if(con != null) {
            try {
                con.close();
            } catch (Exception var4) {
                ;
            }
        }

    }

    public static void closeQuietly(Statement statement, ResultSet rs) {
        closeQuietly((Connection)null, statement, rs);
    }

    public static void closeQuietly(ResultSet rs) {
        closeQuietly((Connection)null, (Statement)null, rs);
    }

    public static void closeQuietly(Connection con, Statement statement) {
        closeQuietly(con, statement, (ResultSet)null);
    }

    public static void closeQuietly(Connection con) {
        closeQuietly(con, (Statement)null, (ResultSet)null);
    }

    public static void closeQuietly(Connection con, ResultSet rs) {
        closeQuietly(con, (Statement)null, rs);
    }

    public static void closeQuietly(Statement statement) {
        closeQuietly((Connection)null, statement, (ResultSet)null);
    }
}
