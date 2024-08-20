package com.src.shengfeng.secure;

import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.*;

public class FieldSensitiveUtil {
    public FieldSensitiveUtil() {
    }

    private static void dealList(Object o) throws IllegalAccessException {
        List<Object> list = (List)o;
        Iterator var3 = list.iterator();

        while(var3.hasNext()) {
            Object obj = var3.next();
            dealNode(obj);
        }

    }

    private static void dealMap(Object o) throws IllegalAccessException {
        Map map = (Map)o;
        Set<Map.Entry> entries = map.entrySet();
        Iterator var4 = entries.iterator();

        while(var4.hasNext()) {
            Map.Entry entry = (Map.Entry)var4.next();
            if (entry.getValue() instanceof List) {
                dealList(entry.getValue());
            } else {
                dealNode(entry.getValue());
            }
        }

    }

    public static void dealNode(Object o) throws IllegalAccessException {

        List<Field> fields= Lists.newArrayList(o.getClass().getDeclaredFields());
        Class currCLs=o.getClass();
        while(currCLs.getSuperclass()!=null){
            currCLs=currCLs.getSuperclass();
            fields.addAll(Lists.newArrayList(currCLs.getDeclaredFields()));
        }
        for(Field field:fields){
            DesensitizationProp desensitizationProp =field.getAnnotation(DesensitizationProp.class);
            if (desensitizationProp != null) {
                field.setAccessible(true);
                Object fieldValueObj = field.get(o);
                if(!Objects.isNull(fieldValueObj)){
                    String v = MosDesensitizedUtil.desensitizeData(fieldValueObj,desensitizationProp);
                    field.set(o, v);
                }
            }
        }
    }
}