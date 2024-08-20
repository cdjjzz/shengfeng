package com.src.shengfeng.secure;

public enum SensitiveTypeEnum {
    CHINESE_NAME,
    ID_CARD,
    FIXED_PHONE,
    MOBILE_PHONE,
    ADDRESS,
    EMAIL,
    BANK_CARD,
    PASSWORD,
    CUSTOM;

    private SensitiveTypeEnum() {
    }
}