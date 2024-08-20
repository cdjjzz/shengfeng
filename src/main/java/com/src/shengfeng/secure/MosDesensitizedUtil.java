package com.src.shengfeng.secure;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;

public class MosDesensitizedUtil {

    public MosDesensitizedUtil() {
    }

    public static String desensitizeData(Object obj, DesensitizationProp desensitizationProp) {
        if (obj != null && desensitizationProp != null) {
            return !(obj instanceof String) ? String.valueOf(obj) : formatValue(obj.toString(), desensitizationProp);
        } else {
            return null;
        }
    }

    private static String formatValue(String value, DesensitizationProp desensitizationProp) {
        switch (desensitizationProp.value()) {
            case ADDRESS:
                value = DesensitizedUtil.address(value, Math.min(value.length(), 3));
                break;
            case ID_CARD:
                if(value.length()>10) {
                    value = DesensitizedUtil.idCardNum(value, 6, 4);
                }
                break;
            case MOBILE_PHONE:
                value = DesensitizedUtil.mobilePhone(value);
                break;
            case EMAIL:
                value = DesensitizedUtil.email(value);
                break;
            case BANK_CARD:
                value = DesensitizedUtil.bankCard(value);
                break;
            case PASSWORD:
                value = DesensitizedUtil.password(value);
                break;
            case CHINESE_NAME:
                value = DesensitizedUtil.chineseName(value);
                break;
            case CUSTOM:
                value = StrUtil.hide(value, desensitizationProp.preLength(), Math.min(value.length(), desensitizationProp.sufLength()));
        }

        return value;
    }
}