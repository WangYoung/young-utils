package com.young.wang.utils.jdbc.result;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import com.young.wang.utils.DtoUtil;


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
 * Created by Young Wang on 2015-08-19 18:55.
 */
public class ResultMap {
    private Map<String,Object> map = new HashMap<String, Object>();

    public void put(String key,Object val){
        map.put(key,val);
    }

    public boolean containsKey(String key){
        return map.containsKey(key);
    }

    public Object getObject(String key){
        return map.get(key);
    }

    public String getString(String key){
        Object obj = this.getObject(key);
        if(obj==null)return null;
        if(obj instanceof String){
            return (String) obj;
        }else if(obj instanceof Date){
        	return DateFormatUtils.format((Date) obj, "yyyy-MM-dd HH:mm:ss");
        }else if(obj instanceof byte[]){
            try {
                return new String(Base64.encodeBase64((byte[]) obj),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new ClassCastException(e.getMessage());
            }
        }else if(DtoUtil.isSimpleType(obj.getClass())){
            return String.valueOf(obj);
        }else{
            return obj.toString();
        }
    }

    public Long getLong(String key){
        Object obj = this.getObject(key);
        if(obj==null)return null;
        if(obj instanceof Long){
            return (Long) obj;
        }else if(obj instanceof Date){
            return ((Date) obj).getTime();
        }else if(DtoUtil.isSimpleType(obj.getClass())){
        	return (Long) ConvertUtils.convert(String.valueOf(obj),Long.class);
        }else{
            return Long.parseLong(obj.toString());
        }
    }

    public Integer getInteger(String key){
        Object obj = this.getObject(key);
        if(obj==null)return null;
        if(obj instanceof Integer){
            return (Integer) obj;
        }if(DtoUtil.isSimpleType(obj.getClass())){
            return (Integer) ConvertUtils.convert(String.valueOf(obj),Integer.class);
        }else{
            return Integer.parseInt(obj.toString());
        }
    }

    public Double getDouble(String key){
        Object obj = this.getObject(key);
        if(obj==null)return null;
        if(obj instanceof Double){
            return (Double) obj;
        }if(DtoUtil.isSimpleType(obj.getClass())){
            return (Double) ConvertUtils.convert(String.valueOf(obj),Double.class);
        }else{
            return Double.parseDouble(obj.toString());
        }
    }

    public Float getFloat(String key){
        Object obj = this.getObject(key);
        if(obj==null)return null;
        if(obj instanceof Float){
            return (Float) obj;
        }if(DtoUtil.isSimpleType(obj.getClass())){
            return (Float) ConvertUtils.convert(String.valueOf(obj),Float.class);
        }else{
            return Float.parseFloat(obj.toString());
        }
    }

    public Boolean getBoolean(String key){
        Object obj = this.getObject(key);
        if(obj==null)return null;
        if(obj instanceof Boolean){
            return (Boolean) obj;
        }if(DtoUtil.isSimpleType(obj.getClass())){
            return (Boolean) ConvertUtils.convert(String.valueOf(obj),Boolean.class);
        }else{
            return Boolean.parseBoolean(obj.toString());
        }
    }

    public Date getDate(String key){
        Object obj = this.getObject(key);
        if(obj==null)return null;
        if(obj instanceof Date){
            return (Date) obj;
        }else if(obj instanceof String){
            try {
				return DateUtils.parseDate((String)obj, new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"});
			} catch (ParseException e) {
				e.printStackTrace();
				throw new ClassCastException();
			}
        }else if(obj.getClass()==Long.class || obj.getClass()==long.class){
            return new Date((Long) obj);
        }else{
            throw new ClassCastException();
        }
    }

    public byte[] getBytes(String key){
        Object obj = this.getObject(key);
        if(obj==null)return null;
        if(obj instanceof byte[]){
            return (byte[]) obj;
        }else{
            throw new ClassCastException();
        }
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject object = new JSONObject();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object val = entry.getValue();
            if(val==null)continue;
            if(val instanceof Date || val instanceof byte[]){
                object.put(entry.getKey(),this.getString(entry.getKey()));
            }else{
                object.put(entry.getKey(),entry.getValue());
            }
        }
        return object;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T toDto(Class<T> clz){
        try {
            if(DtoUtil.isSimpleType(clz)){
                if(map.size()!=1)throw new RuntimeException("The result is not unique");
                Object o = null;
                for (String s : map.keySet()) {
                    o = ConvertUtils.convert(this.getString(s),clz);
                }
                return (T) o;
            }else{
                T t = clz.newInstance();
                List<PropertyDescriptor> descriptors = DtoUtil.scanClassProperty(clz);
                for (PropertyDescriptor descriptor : descriptors) {
                    String fieldName = descriptor.getName();
                    if(this.map.containsKey(fieldName)){
                        Object o = null;
                        Class<?> propertyType = descriptor.getPropertyType();
                        if(Date.class.isAssignableFrom(propertyType)){
                            o = this.getDate(fieldName);
                        }else if(propertyType == byte[].class){
                            o = this.getBytes(fieldName);
                        }else if(DtoUtil.isSimpleType(propertyType)){
                            o = ConvertUtils.convert(this.getString(fieldName),propertyType);
                        }else if(propertyType.isEnum()){
                            Class<? extends Enum> et = (Class<? extends Enum>) propertyType;
                            o = Enum.valueOf(et,this.getString(fieldName));
                        }
                        descriptor.getWriteMethod().invoke(t,o);
                    }
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    public int size(){
        return this.map.size();
    }

    public Set<String> keySet(){
        return this.map.keySet();
    }

    public Collection<Object> values(){
        return this.map.values();
    }

    public Set<Map.Entry<String,Object>> entrySet(){
        return this.map.entrySet();
    }


}
