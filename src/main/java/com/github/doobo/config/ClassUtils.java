package com.github.doobo.config;

import lombok.experimental.PackagePrivate;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
@PackagePrivate
public class ClassUtils {

    /**
     * 设置字符串值
     * @param value
     */
    public static void setStringValue(Object str, Object value) {
        char[] chars = String.valueOf(value).toCharArray();
        try {
            //获取string 类中的value字段
            Field valueField = String.class.getDeclaredField("value");
            //设置private字段可以被修改
            valueField.setAccessible(true);
            //把chars设置到value字段的内容
            valueField.set(str, chars);
        } catch (Exception e) {
            log.error("setStringValueError", e);
        }
    }


    /**
     * 基本类型值切换
     * @param i
     * @param j
     * @param <T>
     */
    public static <T> void swapBaseType(T i, Object j) {
        try {
            Field field = i.getClass().getDeclaredField("value");
            field.setAccessible(true);
            field.set(i, j);
        } catch (Exception e) {
            log.error("swapBaseTypeError", e);
        }
    }

    /**
     * 判断是基本封装类
     * .isPrimitive()是用来判断是否是基本类型的：void.isPrimitive() //true;
     * @param clz
     */
    public static boolean isWrapClass(Class<?> clz) {
        try {
            return ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断object是否为基本类型
     * @param cls
     */
    public static boolean isBaseType(Class<?> cls) {
        return cls.equals(int.class) ||
                cls.equals(byte.class) ||
                cls.equals(long.class) ||
                cls.equals(double.class) ||
                cls.equals(float.class) ||
                cls.equals(char.class) ||
                cls.equals(short.class) ||
                cls.equals(boolean.class);
    }
}
