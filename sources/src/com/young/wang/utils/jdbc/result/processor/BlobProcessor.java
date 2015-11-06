package com.young.wang.utils.jdbc.result.processor;

import java.sql.Blob;
import java.sql.SQLException;

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
 * Created by Young Wang on 2015-08-19 19:55.
 */
public class BlobProcessor implements FieldProcessor {
	@Override
    public boolean isAccess(Class<?> clz) {
        return Blob.class.isAssignableFrom(clz);
    }

    @Override
    public Object processor(Object o) {
        byte[] b = new byte[0];
        try {
            b = ((Blob) o).getBytes(1, (int) ((Blob) o).length());
        } catch (SQLException e) {
            throw new ClassCastException();
        }
        return b;
    }
}
