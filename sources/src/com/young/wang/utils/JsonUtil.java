package com.young.wang.utils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.processors.JsonValueProcessorMatcher;

import org.apache.commons.lang.time.DateFormatUtils;

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
 * Created by Young Wang on 2015年11月5日 下午12:08:30.
 *
 */
public final class JsonUtil {
	private JsonUtil(){}
	
	private static final JsonConfig dtoJsonConfig = new JsonConfig();
    static {
        dtoJsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
            @Override
            public Object processArrayValue(Object o, JsonConfig jsonConfig) {
                return DateFormatUtils.format((Date) o, "yyyy-MM-dd HH:mm:ss");
            }

            @Override
            public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
                return DateFormatUtils.format((Date) o, "yyyy-MM-dd HH:mm:ss");
            }

        });
        dtoJsonConfig.registerJsonValueProcessor(Enum.class, new JsonValueProcessor() {
            @Override
            public Object processArrayValue(Object o, JsonConfig jsonConfig) {
                return o.toString();
            }

            @Override
            public Object processObjectValue(String s, Object o, JsonConfig jsonConfig) {
                return o.toString();
            }

        });

        dtoJsonConfig.setJsonValueProcessorMatcher(new JsonValueProcessorMatcher() {
            @SuppressWarnings("rawtypes")
			@Override
            public Object getMatch(Class aClass, Set set) {
                if(Date.class.isAssignableFrom(aClass)) return Date.class;
                else if(aClass.isEnum()) return Enum.class;
                return aClass;
            }
        });

    }
    
    public static boolean isJsonNull(Object obj) {
        if(obj == null) return true;
        if(obj instanceof JSONObject) {
            JSONObject json = (JSONObject) obj;
            return json.isNullObject();
        }
        return false;
    }
    
    public static JSONObject successMessage(Object message) {
    	Map<String,Object> m = new HashMap<String,Object>();
    	m.put("success", true);
    	if(message != null){
    		m.put("messages", message);
    	}
		return JSONObject.fromObject(m, dtoJsonConfig);
	}
	public static JSONObject faultMessage(String errorMessage) {
		JSONObject obj = new JSONObject();
		obj.put("success", false);
		if(errorMessage != null) {
			obj.put("errors", errorMessage);
		}
		return obj;
	}
	
	public static JSONObject dto2JsonObject(Object dto) {
        return JSONObject.fromObject(dto, dtoJsonConfig);
    }

    public static JSONArray dtos2JsonArray(Collection<?> list) {
        JSONArray arr = new JSONArray();
        for(Object o : list) {
            arr.add(dto2JsonObject(o));
        }
        return arr;
    }

}
