package com.young.wang.utils.jdbc.result;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.young.wang.utils.jdbc.result.processor.BlobProcessor;
import com.young.wang.utils.jdbc.result.processor.ClobProcessor;
import com.young.wang.utils.jdbc.result.processor.DateProcessor;
import com.young.wang.utils.jdbc.result.processor.FieldProcessor;
import com.young.wang.utils.jdbc.result.processor.SimpleTypesProcessor;

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
 * Created by Young Wang on 2015-08-19 20:01.
 */
public class ResultFactory {
	private ResultFactory() {
        processorList.add(new SimpleTypesProcessor());
        processorList.add(new ClobProcessor());
        processorList.add(new BlobProcessor());
        processorList.add(new DateProcessor());
    }
    private static final ResultFactory instance = new ResultFactory();
    public static ResultFactory getInstance(){
        return instance;
    }

    private final List<FieldProcessor> processorList = new ArrayList<FieldProcessor>();

    public List<ResultMap> getResult(ResultSet rs) throws SQLException {
        List<ResultMap> ret = new ArrayList<ResultMap>();

        ResultSetMetaData data = rs.getMetaData();
        String[] fieldNames = new String[data.getColumnCount()];
        for(int i = 0 ; i<fieldNames.length;i++){
            fieldNames[i] = data.getColumnName(i+1);
        }
        while(rs.next()){
            ResultMap map = new ResultMap();
            for (String fieldName : fieldNames) {
                Object o = rs.getObject(fieldName);
                if(o==null){
                    map.put(fieldName,null);
                }else{
                    for (FieldProcessor processor : processorList) {
                        if(processor.isAccess(o.getClass())){
                            map.put(fieldName,processor.processor(o));
                            break;
                        }
                    }
                    if(!map.containsKey(fieldName))throw new RuntimeException("Not FieldProcessor has access!");
                }
            }
            ret.add(map);
        }
        return ret;
    }
}
