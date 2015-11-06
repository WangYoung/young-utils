package com.young.wang.utils.excel.read;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

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
 * Created by Young Wang on 2015-06-01 11:51.
 */
public class DefaultExcel2ListBuilder<T extends AbstractExcelConfig> extends AbstractExcel2ListBuilder<T> {

    private Class<T> clz;
    private AnnotationConfigDtoInfo configDtoInfo;

    private Map<Integer,String> headRowMap = new HashMap<Integer, String>();

    public DefaultExcel2ListBuilder(Class<T> clz) {
        this.clz = clz;
        this.configDtoInfo = new AnnotationConfigDtoInfo(clz);
    }

    @Override
    public void startWorkbook(Workbook workbook) {
        super.startWorkbook(workbook);
        int totalRowNum = 0;
        int sheetNumber = workbook.getNumberOfSheets();
        for(int i=0; i<sheetNumber; i++){
            totalRowNum +=workbook.getSheetAt(i).getLastRowNum();
        }
        if(totalRowNum>1001){
            throw new RuntimeException("一次最多导入1000条数据");
        }
    }

    @Override
    protected boolean isNeedAdd(Map<Integer,String> rowData, int rowIndex) {
        if(rowIndex==0){
            Map<Integer,String> bufMap = new HashMap<Integer, String>();
            for(Integer i : this.configDtoInfo.getFieldInfoMap().keySet()){
                if(!rowData.containsKey(i)){
                    throw new RuntimeException("标题行列数错误");
                }else{
                    if(StringUtils.isEmpty(rowData.get(i))){
                        throw new RuntimeException("标题行不能有空列");
                    }
                    bufMap.put(i,rowData.get(i));
                }
            }
            this.headRowMap = bufMap;
            return false;
        }
        for (String o : rowData.values()) {
            if(!StringUtils.isEmpty(o))return true;
        }
        return false;
    }

    @Override
    protected T createNewObject(Map<Integer,String> rowData, int rowIndex) {
        try {
            T t = this.clz.newInstance();
            t.setNum(rowIndex+1);
            for(Integer i : this.configDtoInfo.getFieldInfoMap().keySet()){
                String val = "";
                if(rowData.containsKey(i)){
                    val = rowData.get(i);
                }
                try{
                    AnnotationConfigDtoInfo.ConfigFieldInfo fieldInfo = this.configDtoInfo.getFieldInfoMap().get(i);

                    if(StringUtils.isEmpty(val) && fieldInfo.notNull){
                        t.setSuccess(false);
                        t.setMessage(headRowMap.get(i)+" 不允许空！");
                    }else if(val.length()>fieldInfo.maxLength){
                        t.setSuccess(false);
                        t.setMessage(headRowMap.get(i)+" 的长度要少于 "+fieldInfo.maxLength+"个字符！");
                    }else if(val.length()<fieldInfo.minLength){
                        t.setSuccess(false);
                        t.setMessage(headRowMap.get(i)+" 的长度要多于 "+fieldInfo.minLength+"个字符！");
                    }else{
                        fieldInfo.set(t,val);
                        if(t.isSuccess()){
                            if(!StringUtils.isEmpty(val)){
                                for(RowValidator validator : fieldInfo.validatorList){
                                    if(!validator.validate(fieldInfo.get(t))){
                                        t.setSuccess(false);
                                        t.setMessage(headRowMap.get(i)+" "+validator.getErrorMessage());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }catch (IllegalArgumentException e){
                    if(t.isSuccess()){
                        t.setSuccess(false);
                        t.setMessage(headRowMap.get(i)+" 数据格式错误！");
                    }
                }
            }
            if(t.isSuccess()){
                for(RowValidator validator : this.configDtoInfo.getValidatorList()){
                    if(!validator.validate(t)){
                        t.setSuccess(false);
                        t.setMessage(validator.getErrorMessage());
                        break;
                    }
                }
            }
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("系统错误，错误消息：反射异常");
        }
    }


}
