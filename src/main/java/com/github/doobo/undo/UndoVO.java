package com.github.doobo.undo;

import com.alibaba.fastjson.JSONPath;
import com.github.doobo.config.HandleType;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;

import static com.github.doobo.config.ClassUtils.*;

/**
 * 数据反脱敏对象
 */
@Data
@Slf4j
@Accessors(chain = true)
public class UndoVO {

    /**
     * 待填充对象的名称
     */
    private String argName;

    /**
     * 脱敏标签,方便自定义识别
     */
    private String type;

    /**
     * 组件里面的入参
     */
    private String[] fields;

    /**
     * 正则处理后的属性名
     */
    private List<String> regFields;

    /**
     * 字段匹配方式
     */
    private HandleType mode;

    /**
     * 待脱敏对象
     */
    private Object obj;

    /**
     * 替换对应的值
     *
     * @param value
     */
    public boolean undo(Object value) {
        if (fields != null && fields.length == 1 && fields[0].isEmpty()) {
            //字符串类型值替换
            if (obj instanceof String) {
                setStringValue(obj, value);
                return true;
            }
            //基本类型值替换
            if(isWrapClass(obj.getClass())){
                swapBaseType(obj, value);
                return true;
            }
            BeanUtils.copyProperties(value, obj);
            return true;
        }
        if (regFields == null || regFields.isEmpty()) {
            return false;
        }
        //如果是基本数据类型,没有引用地址,不支持值替换,需使用封装对象:Long...
        if (isBaseType(obj.getClass())) {
            return false;
        }
        //字符串常量,不支持替换,请使用封装对象
        if (obj instanceof String) {
            return false;
        }
        for (String reg : regFields) {
            if (JSONPath.contains(obj, reg)) {
                try {
                    JSONPath.set(obj, reg, value);
                } catch (Exception e) {
                    log.error("undoError", e);
                }
            }
        }
        return true;
    }

}
