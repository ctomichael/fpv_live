package dji.sdksharedlib.keycatalog;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheUpdateType;
import dji.sdksharedlib.hardware.abstractions.battery.NonSmartA3BatteryAbstraction;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalRoninMXAbstraction;
import dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCSparkAbstraction;
import dji.sdksharedlib.keycatalog.extension.ComplexKey;
import dji.sdksharedlib.keycatalog.extension.Key;
import dji.sdksharedlib.keycatalog.extension.Utils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EXClassNullAway
public class DJISDKCacheKeys {
    @Key(accessType = 4, type = Boolean.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String CONNECTION = "Connection";
    @Key(accessType = 1, excludedAbstractions = {DJIGimbalRoninMXAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String FIRMWARE_VERSION = "FirmwareVersion";
    public static final String NONE = "None";
    @Key(accessType = 1, excludedAbstractions = {NonSmartA3BatteryAbstraction.class, DJIRCSparkAbstraction.class}, type = String.class, updateType = DJISDKCacheUpdateType.USER_DRIVEN)
    public static final String SERIAL_NUMBER = "SerialNumber";
    private static final String TAG = "DJISDKCacheKeys";
    private static Map<Class<? extends DJISDKCacheKeys>, Map<String, KeyInfo>> keyInfoMap;
    private final String name;

    public DJISDKCacheKeys(String name2) {
        this.name = name2;
    }

    public String getKeyName() {
        return this.name;
    }

    /* access modifiers changed from: protected */
    public String getDefaultAbstractionName() {
        return null;
    }

    public static class KeyInfo {
        private Map<Class, Key> classKeyMap = new HashMap();
        private ComplexKey complexKeyAnnotation;
        private Key defaultKey;
        private Key keyAnnotation;
        private Set<Class> unsupport = new HashSet();

        public KeyInfo(String paramKey, ComplexKey complexKeyAnnotation2, Key keyAnnotation2) {
            this.complexKeyAnnotation = complexKeyAnnotation2;
            this.keyAnnotation = keyAnnotation2;
            Key[] keys = Utils.parseAllKey(complexKeyAnnotation2, keyAnnotation2);
            if (keys != null && keys.length > 0) {
                for (Key key : keys) {
                    Class[] excludedAbstractions = Utils.parseAllClass(key.excludedAbstractions());
                    Class[] includedAbstractions = Utils.parseAllClass(key.includedAbstractions());
                    if (excludedAbstractions != null) {
                        for (Class clazz : excludedAbstractions) {
                            if (!this.unsupport.contains(clazz)) {
                                this.unsupport.add(clazz);
                            }
                        }
                    }
                    if (includedAbstractions == null) {
                        this.defaultKey = key;
                    }
                    if (includedAbstractions != null) {
                        for (Class clazz2 : includedAbstractions) {
                            if (!this.classKeyMap.containsKey(clazz2)) {
                                this.classKeyMap.put(clazz2, key);
                            } else {
                                DJILog.e(DJISDKCacheKeys.TAG, "repeat include key, please check your code : " + clazz2.toString() + ", key : " + key, new Object[0]);
                            }
                        }
                    }
                }
            }
            for (Class clazz3 : this.unsupport) {
                if (this.classKeyMap.containsKey(clazz3)) {
                    this.classKeyMap.remove(clazz3);
                }
            }
        }

        public boolean support(Class<? extends DJISDKCacheHWAbstraction> abstractionClazz) {
            if (abstractionClazz == null || this.unsupport.contains(abstractionClazz)) {
                return false;
            }
            if (this.defaultKey != null) {
                return true;
            }
            if (this.classKeyMap == null) {
                return false;
            }
            for (Class clazz : this.classKeyMap.keySet()) {
                if (clazz.isAssignableFrom(abstractionClazz)) {
                    return true;
                }
            }
            return false;
        }

        public Key getKey(Class<? extends DJISDKCacheHWAbstraction> clazz) {
            if (clazz == null) {
                return null;
            }
            Key key = this.defaultKey;
            if (this.classKeyMap == null) {
                return key;
            }
            if (this.classKeyMap.containsKey(clazz)) {
                return this.classKeyMap.get(clazz);
            }
            for (Class cla : this.classKeyMap.keySet()) {
                if (cla.isAssignableFrom(clazz)) {
                    return this.classKeyMap.get(cla);
                }
            }
            return key;
        }
    }

    public static Map<String, KeyInfo> getKeyInfoMap(Class<? extends DJISDKCacheKeys> clazz) {
        if (clazz == null) {
            return null;
        }
        if (keyInfoMap == null) {
            keyInfoMap = new HashMap();
        }
        if (!keyInfoMap.containsKey(clazz)) {
            Map<String, KeyInfo> paramInfoMap = new HashMap<>();
            Field[] fields = clazz.getFields();
            for (Field field : fields) {
                if (field.getType() == String.class && isStatic(field.getModifiers()) && (field.isAnnotationPresent(ComplexKey.class) || field.isAnnotationPresent(Key.class))) {
                    try {
                        String paramKey = (String) field.get(null);
                        paramInfoMap.put(paramKey, new KeyInfo(paramKey, (ComplexKey) field.getAnnotation(ComplexKey.class), (Key) field.getAnnotation(Key.class)));
                    } catch (Exception e) {
                        DJILog.e(TAG, DJILog.exceptionToString(e), new Object[0]);
                    }
                }
            }
            keyInfoMap.put(clazz, paramInfoMap);
        }
        return keyInfoMap.get(clazz);
    }

    public static boolean isStatic(int modifiers) {
        return (modifiers & 8) != 0;
    }
}
