package com.young.wang.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

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
 * Created by Young Wang on 2015年11月5日 上午10:52:49.
 *
 */
public final class DtoUtil {
	private final static Set<Class<?>> SIMPLE_TYPES = new HashSet<Class<?>>();
	static {
		SIMPLE_TYPES.add(Boolean.class);
		SIMPLE_TYPES.add(Double.class);
		SIMPLE_TYPES.add(String.class);
		SIMPLE_TYPES.add(Character.class);
		SIMPLE_TYPES.add(Integer.class);
		SIMPLE_TYPES.add(Float.class);
		SIMPLE_TYPES.add(Byte.class);
		SIMPLE_TYPES.add(Short.class);
		SIMPLE_TYPES.add(Long.class);
		SIMPLE_TYPES.add(boolean.class);
		SIMPLE_TYPES.add(double.class);
		SIMPLE_TYPES.add(char.class);
		SIMPLE_TYPES.add(int.class);
		SIMPLE_TYPES.add(float.class);
		SIMPLE_TYPES.add(byte.class);
		SIMPLE_TYPES.add(short.class);
		SIMPLE_TYPES.add(long.class);
		SIMPLE_TYPES.add(java.util.Date.class);
	}
	private DtoUtil() {}
	
	public static boolean isSimpleType(Class<?> t) {
		return SIMPLE_TYPES.contains(t);
	}

	/**
	 * 简单复制
	 */
	public static void copy(Object dest, Object src){
		try {
			PropertyDescriptor[] destPds = PropertyUtils.getPropertyDescriptors(dest);
			PropertyDescriptor[] srcPds = PropertyUtils.getPropertyDescriptors(src);
			Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();
			for(PropertyDescriptor pd : destPds) {
				map.put(pd.getName(), pd);
			}
			
			for(PropertyDescriptor pd : srcPds) {
				if(pd.getReadMethod() == null) continue;
				if(!map.containsKey(pd.getName())) continue;
				PropertyDescriptor destPd = map.get(pd.getName());
				if(destPd.getWriteMethod() == null) continue;
				if(!destPd.getPropertyType().equals(pd.getPropertyType())) continue;
				if(!(SIMPLE_TYPES.contains(pd.getPropertyType()) || pd.getPropertyType().isArray())) continue;
				if(pd.getPropertyType().isArray()) {
					if(!SIMPLE_TYPES.contains(pd.getPropertyType().getComponentType())) continue;
				}
				Method readMethod = pd.getReadMethod(); 
				Object v = readMethod.invoke(src);
				Method writeMethod = destPd.getWriteMethod();
				writeMethod.invoke(dest, v);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 扫描字段
	 */
	public static List<PropertyDescriptor> scanClassProperty(Class<?> clz){
		try {
			List<PropertyDescriptor> list = new ArrayList<PropertyDescriptor>();
	        if(clz==Object.class)return list;
	        Field[] fields = clz.getDeclaredFields();
	        for (Field field : fields) {
	            list.add(new PropertyDescriptor(field.getName(),clz));
	        }
	        list.addAll(scanClassProperty(clz.getSuperclass()));
	        return list;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
        
    }
}
