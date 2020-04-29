package dji.sdksharedlib.hardware.abstractions;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum DJISDKCacheUpdateType {
    DYNAMIC(0),
    USER_DRIVEN(1),
    EVENT(2),
    Unknown(255);
    
    private int value;

    private DJISDKCacheUpdateType(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static DJISDKCacheUpdateType find(int value2) {
        DJISDKCacheUpdateType result = Unknown;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}
