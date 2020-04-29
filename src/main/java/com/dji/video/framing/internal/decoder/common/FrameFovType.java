package com.dji.video.framing.internal.decoder.common;

public enum FrameFovType {
    Wide(1),
    Narrow(2),
    NoGdc(3),
    Unknown(255);
    
    private static volatile FrameFovType[] values = null;
    private int value;

    private FrameFovType(int value2) {
        this.value = value2;
    }

    public int getValue() {
        return this.value;
    }

    public static FrameFovType find(int value2) {
        if (values == null) {
            values = values();
        }
        FrameFovType[] frameFovTypeArr = values;
        for (FrameFovType type : frameFovTypeArr) {
            if (type.value == value2) {
                return type;
            }
        }
        return Unknown;
    }
}
