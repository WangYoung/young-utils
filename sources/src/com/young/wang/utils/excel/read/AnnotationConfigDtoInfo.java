package com.young.wang.utils.excel.read;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.time.DateUtils;

import com.young.wang.utils.DtoUtil;
import com.young.wang.utils.excel.read.annotation.RowField;
import com.young.wang.utils.excel.read.annotation.RowValid;
import com.young.wang.utils.excel.read.annotation.RowValids;
import com.young.wang.utils.excel.read.validator.RowValidator;

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
 * Created by Young Wang on 2015-06-01 14:16.
 */
public class AnnotationConfigDtoInfo {
    private Class<?> clz;
    private List<RowValidator> validatorList;
    private Map<Integer,ConfigFieldInfo> fieldInfoMap;

    public AnnotationConfigDtoInfo(Class<?> clz) {
        this.clz = clz;
        validatorList = this.getClassValidator(clz);
        fieldInfoMap = this.scanFields(clz);
    }

    class ConfigFieldInfo{
        int num;
        boolean notNull;
        int minLength;
        int maxLength;
        List<RowValidator> validatorList;

        Field field;
        Method readMethod;
        Method writeMethod;
        PropertyDescriptor pd;
        Class<?> propertyType;

        @SuppressWarnings({"unchecked","rawtypes"})
        void set(Object o,String val) throws IllegalArgumentException {
            Class<?> clz = pd.getPropertyType();
            Object castVal;

            if(DtoUtil.isSimpleType(clz)){
                try {
                    if(Date.class.isAssignableFrom(clz)){
                        castVal = DateUtils.parseDate(val,new String[]{"yyyy-MM-dd","yyyy-MM-dd HH:mm:ss","yyyy-MM-dd hh:mm:ss"});
                    }else if(clz.isEnum()){
                        Class<? extends Enum> c = (Class<? extends Enum>) clz;
                        castVal = Enum.valueOf(c,val);
                    }else{
                        castVal = ConvertUtils.convert(val, clz);
                    }
                }catch (Exception e){
                    throw new IllegalArgumentException("数据格式错误");
                }
            }else{
                throw new IllegalArgumentException("类型无效");
            }

            try {
                this.writeMethod.invoke(o,castVal);
            }catch (Exception e) {
                throw new RuntimeException("系统错误，错误消息：反射异常");
            }
        }

        Object get(Object o){
            try {
                return this.readMethod.invoke(o);
            } catch (Exception e) {
                throw new RuntimeException("系统错误，错误消息：反射异常");
            }
        }

    }

    private Map<Integer,ConfigFieldInfo> scanFields(Class<?> clz){
        Map<Integer,ConfigFieldInfo> map = new HashMap<Integer, ConfigFieldInfo>();
        Field[] fields = clz.getDeclaredFields();

        for (Field field : fields) {
            if(!field.isAnnotationPresent(RowField.class)){
                continue;
            }

            ConfigFieldInfo info = new ConfigFieldInfo();
            PropertyDescriptor pd;
            try {
                pd = new PropertyDescriptor(field.getName(),clz);
            } catch (IntrospectionException e) {
                throw new RuntimeException("系统错误，错误消息：反射异常");
            }

            RowField rowField = field.getAnnotation(RowField.class);
            info.num = rowField.cellNum();
            info.notNull = rowField.notNull();
            info.maxLength = rowField.maxLength();
            info.minLength = rowField.minLength();
            info.validatorList = this.getFieldValidator(field);

            info.field = field;
            info.pd = pd;
            info.writeMethod = pd.getWriteMethod();
            info.readMethod = pd.getReadMethod();
            info.propertyType = pd.getPropertyType();

            if(map.containsKey(info.num)) throw new RuntimeException("字段映射列重复");
            if(info.num < 1)throw new RuntimeException("cellNum必须大于0");
            map.put(info.num,info);
        }
        return map;
    }

    private List<RowValidator> getClassValidator(Class<?> clz){
        List<RowValidator> list = new ArrayList<RowValidator>();
        if(clz.isAnnotationPresent(RowValids.class)){
            list.addAll(this.getValidatorList(clz.getAnnotation(RowValids.class)));
        }
        if(clz.isAnnotationPresent(RowValid.class)){
            list.add(this.getValidator(clz.getAnnotation(RowValid.class)));
        }
        return list;
    }

    private List<RowValidator> getFieldValidator(Field field){
        List<RowValidator> list = new ArrayList<RowValidator>();
        if(field.isAnnotationPresent(RowValids.class)){
            list.addAll(this.getValidatorList(field.getAnnotation(RowValids.class)));
        }
        if(field.isAnnotationPresent(RowValid.class)){
            list.add(this.getValidator(field.getAnnotation(RowValid.class)));
        }
        return list;
    }

    private List<RowValidator> getValidatorList(RowValids rowValids){
        List<RowValidator> list = new ArrayList<RowValidator>();
        RowValid[] rowValidArr = rowValids.value();
        for (RowValid rowValid : rowValidArr) {
            list.add(this.getValidator(rowValid));
        }
        return list;
    }

    private RowValidator getValidator(RowValid rowValid){
        Class<? extends RowValidator> clz = rowValid.value();
        Object[] arguments = rowValid.arguments();
        try {
            if(arguments==null || arguments.length==0) return clz.newInstance();
            else{
                Class<?>[] c = new Class[arguments.length];
                for (int i = 0; i < arguments.length; i++)c[i] = String.class;

                Constructor<? extends RowValidator> constructor = null;
                try{
                    constructor = clz.getConstructor(c);
                    return constructor.newInstance(arguments);
                }catch (NoSuchMethodException e){
                	constructor = clz.getConstructor(String[].class);
                    Object[] p = new Object[1];
                    p[0] = arguments;
                    return constructor.newInstance(p);
                }
            }
        }catch (Exception e){
            throw new RuntimeException("系统错误，错误消息：反射异常");
        }
    }


    public Class<?> getClz() {
        return clz;
    }

    public List<RowValidator> getValidatorList() {
        return validatorList;
    }

    public Map<Integer, ConfigFieldInfo> getFieldInfoMap() {
        return fieldInfoMap;
    }
}
