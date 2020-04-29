package dji.sdksharedlib.hardware.abstractions;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJISDKCacheKeyCharacteristics {
    public static final int DEFAULT_EXPIRATION_DURATION = 100;
    public static final int DEFAULT_INTERVAL = 1000;
    public static final int DEFAULT_SETTER_VAL_PROTECTION_DURATION = 500;
    private int accessTypeMask;
    public int autoGetInterval = 1000;
    private int expirationDuration;
    public String key;
    public Class[] paramTypes;
    private int protectDuration;
    public DJISDKCacheUpdateType updateType;
    public Class valueType;

    public static class AccessType {
        public static final int ACTION = 8;
        public static final int GET = 1;
        public static final int PUSH = 4;
        public static final int SET = 2;
        public static final int SUBSCRIBE = 16;
    }

    public DJISDKCacheKeyCharacteristics() {
    }

    public DJISDKCacheKeyCharacteristics(String key2, int accessTypeMask2, DJISDKCacheUpdateType updateType2, Class... types) {
        init(key2, accessTypeMask2, updateType2, -1, -1, 1000, types);
    }

    public DJISDKCacheKeyCharacteristics(String key2, int accessTypeMask2, DJISDKCacheUpdateType updateType2, int protectDuration2, int expirationDuration2, int autoGetInterval2, Class... types) {
        init(key2, accessTypeMask2, updateType2, protectDuration2, expirationDuration2, autoGetInterval2, types);
    }

    private void init(String key2, int accessTypeMask2, DJISDKCacheUpdateType updateType2, int protectDuration2, int expirationDuration2, int autoGetInterval2, Class... types) {
        this.key = key2;
        this.accessTypeMask = accessTypeMask2;
        this.updateType = updateType2;
        this.protectDuration = protectDuration2;
        this.expirationDuration = expirationDuration2;
        if (types == null || types.length <= 1) {
            if ((accessTypeMask2 & 4) == 4) {
                this.autoGetInterval = 0;
            } else {
                this.autoGetInterval = autoGetInterval2;
            }
            if (types == null || types.length == 0) {
                this.valueType = null;
            } else {
                this.valueType = types[0];
            }
        } else {
            this.paramTypes = types;
        }
    }

    public boolean isGettable() {
        return (this.accessTypeMask & 1) == 1;
    }

    public boolean isSettable() {
        return (this.accessTypeMask & 2) == 2;
    }

    public boolean isPushAccessType() {
        return (this.accessTypeMask & 4) == 4;
    }

    public boolean isParamTypeCorrect(Object... params) {
        if (this.paramTypes != null) {
            return checkParamTypes(params);
        }
        if (params.length == 0) {
            return checkValueType(null);
        }
        return checkValueType(params[0]);
    }

    private boolean checkValueType(Object value) {
        if (value != null) {
            return this.valueType.equals(value.getClass());
        }
        if (this.valueType != null && this.valueType.isPrimitive()) {
            return false;
        }
        return true;
    }

    private boolean checkParamTypes(Object... params) {
        if (params == null) {
            if (this.paramTypes == null) {
                return true;
            }
            return false;
        } else if (params.length != this.paramTypes.length) {
            return false;
        } else {
            boolean isCorrect = true;
            int i = 0;
            while (true) {
                if (i >= params.length) {
                    break;
                }
                if (params[i] == null) {
                    if (this.paramTypes[i].isPrimitive()) {
                        isCorrect = false;
                        break;
                    }
                } else if (params[i].getClass() != this.paramTypes[i]) {
                    isCorrect = false;
                    break;
                }
                i++;
            }
            return isCorrect;
        }
    }

    public boolean isUserDrivenUpdateType() {
        return this.updateType == DJISDKCacheUpdateType.USER_DRIVEN;
    }

    public boolean isEventUpdateType() {
        return this.updateType == DJISDKCacheUpdateType.EVENT;
    }

    public int getAutoGetInterval() {
        return this.autoGetInterval;
    }

    public void setAutoGetInterval(int autoGetInterval2) {
        this.autoGetInterval = autoGetInterval2;
    }

    public int getExpirationDuation() {
        if (this.updateType == DJISDKCacheUpdateType.DYNAMIC) {
            return this.expirationDuration;
        }
        return -1;
    }

    public int getProtectDuration() {
        return this.protectDuration;
    }
}
