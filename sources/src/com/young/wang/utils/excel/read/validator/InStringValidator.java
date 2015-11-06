package com.young.wang.utils.excel.read.validator;

import java.util.Arrays;

import com.young.wang.utils.Utils;

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
 * Created by Young Wang on 2015-06-01 16:46.
 */
public class InStringValidator implements RowValidator {
    private String[] strings;

    public InStringValidator(String[] strings) {
        this.strings = strings;
    }

    @Override
    public boolean validate(Object o) {
        String str = (String) o;
        for (String s : strings) {
            if(s.equals(str))return true;
        }
        return false;
    }

    @Override
    public String getErrorMessage() {
        return "只能是 "+ Utils.collectionToStr(Arrays.asList(strings),"，","\"","\"")+" 中的一个";
    }
}
