package com.young.wang.utils.jdbc.result.processor;

import java.io.BufferedReader;
import java.sql.Clob;

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
 * Created by Young Wang on 2015-08-19 19:58.
 */
public class ClobProcessor implements FieldProcessor {
	@Override
    public boolean isAccess(Class<?> clz) {
        return Clob.class.isAssignableFrom(clz);
    }

    @Override
    public Object processor(Object o) {
        try{
            BufferedReader br = new BufferedReader(((Clob) o).getCharacterStream());
            StringBuilder sb = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            return sb.toString();
        }catch (Exception e){
            throw new ClassCastException();
        }
    }
}
