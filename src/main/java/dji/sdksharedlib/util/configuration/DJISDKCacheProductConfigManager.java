package dji.sdksharedlib.util.configuration;

import dji.common.error.DJIError;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DJIFlycParamInfoManager;
import dji.midware.data.model.P3.DataFlycGetParamInfoByHash;
import dji.midware.data.model.P3.DataFlycGetParams;
import dji.midware.data.model.P3.DataFlycResetParamNew;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.data.params.P3.ParamInfoBean;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.FlightControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import dji.sdksharedlib.store.DJISDKCacheParamValue;
import dji.sdksharedlib.util.configuration.keymaps.FlightControllerConfigsKeyMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

@EXClassNullAway
public class DJISDKCacheProductConfigManager {
    private static DJISDKCacheProductConfigManager instance;
    /* access modifiers changed from: private */
    public int currentProductVersion = 0;
    private HashMap<String, DJISDKCacheProductConfigsKey> keyHashMap = FlightControllerConfigsKeyMap.getFlightControllerConfigsKeyMap();

    public static synchronized DJISDKCacheProductConfigManager getInstance() {
        DJISDKCacheProductConfigManager dJISDKCacheProductConfigManager;
        synchronized (DJISDKCacheProductConfigManager.class) {
            if (instance == null) {
                instance = new DJISDKCacheProductConfigManager();
            }
            dJISDKCacheProductConfigManager = instance;
        }
        return dJISDKCacheProductConfigManager;
    }

    public class ConfigRange {
        public Number defaultValue;
        public Number maxValue;
        public Number minValue;

        public ConfigRange(Number minValue2, Number maxValue2, Number defaultValue2) {
            this.minValue = minValue2;
            this.maxValue = maxValue2;
            this.defaultValue = defaultValue2;
        }
    }

    public ParamInfo getParamInfo(String key) {
        if (!isConfigSupported(key)) {
            return null;
        }
        String configsHashKey = getSupportedConfigHashKey(key);
        DJIFlycParamInfoManager.getInstance();
        return DJIFlycParamInfoManager.read(configsHashKey);
    }

    private int getSupportedVersion(String key, int version) {
        DJISDKCacheProductConfigsKey configKey;
        if (!isConfigSupported(key) || (configKey = this.keyHashMap.get(key)) == null) {
            return 0;
        }
        if (configKey.minVersion == -1 && configKey.maxVersion >= this.currentProductVersion) {
            return version + 1;
        }
        if (configKey.maxVersion == -1 && configKey.minVersion <= this.currentProductVersion) {
            return version + 1;
        }
        if (configKey.minVersion <= this.currentProductVersion && configKey.maxVersion >= this.currentProductVersion) {
            return version + 1;
        }
        if (configKey.backupKey != null) {
            return getSupportedVersion(configKey.backupKey, version + 1);
        }
        return 0;
    }

    private DJISDKCacheProductConfigManager() {
        loadKeyHashMapToParamInfoManager();
        DJISDKCache.getInstance().startListeningForUpdates(KeyHelper.getFlightControllerKey(FlightControllerKeys.INTERNAL_FLIGHT_CONTROLLER_VERSION), new DJIParamAccessListener() {
            /* class dji.sdksharedlib.util.configuration.DJISDKCacheProductConfigManager.AnonymousClass1 */

            public void onValueChange(DJISDKCacheKey key, DJISDKCacheParamValue oldValue, DJISDKCacheParamValue newValue) {
                int unused = DJISDKCacheProductConfigManager.this.currentProductVersion = CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.INTERNAL_FLIGHT_CONTROLLER_VERSION));
            }
        }, false);
        this.currentProductVersion = CacheHelper.toInt(CacheHelper.getFlightController(FlightControllerKeys.INTERNAL_FLIGHT_CONTROLLER_VERSION));
    }

    public boolean isConfigSupported(String key) {
        DJISDKCacheProductConfigsKey configKey;
        if (!this.keyHashMap.containsKey(key) || (configKey = this.keyHashMap.get(key)) == null) {
            return false;
        }
        if (configKey.minVersion == -1 && configKey.maxVersion >= this.currentProductVersion) {
            return true;
        }
        if (configKey.maxVersion == -1 && configKey.minVersion <= this.currentProductVersion) {
            return true;
        }
        if (configKey.minVersion <= this.currentProductVersion && configKey.maxVersion >= this.currentProductVersion) {
            return true;
        }
        if (configKey.backupKey != null) {
            return isConfigSupported(configKey.backupKey);
        }
        return false;
    }

    public boolean isConfigValueValid(String key, Number value) {
        DJISDKCacheProductConfigsKey configKey;
        if (!isConfigSupported(key) || (configKey = this.keyHashMap.get(key)) == null) {
            return false;
        }
        if (configKey.maxValue != null && configKey.minValue != null && configKey.maxValue.doubleValue() > value.doubleValue() && configKey.minValue.doubleValue() < value.doubleValue()) {
            return true;
        }
        if (configKey.backupKey != null) {
            return isConfigValueValid(configKey.backupKey, value);
        }
        return false;
    }

    public boolean isReadable(String key) {
        DJISDKCacheProductConfigsKey configsKey;
        if (!isConfigSupported(key) || (configsKey = this.keyHashMap.get(key)) == null) {
            return false;
        }
        if ((configsKey.accessMask & 1) == 1) {
            return true;
        }
        if (configsKey.backupKey != null) {
            return isSubscribable(configsKey.backupKey);
        }
        return false;
    }

    public boolean isWritable(String key) {
        DJISDKCacheProductConfigsKey configsKey;
        if (!isConfigSupported(key) || (configsKey = this.keyHashMap.get(key)) == null) {
            return false;
        }
        if ((configsKey.accessMask & 2) == 2) {
            return true;
        }
        if (configsKey.backupKey != null) {
            return isSubscribable(configsKey.backupKey);
        }
        return false;
    }

    public boolean isSubscribable(String key) {
        DJISDKCacheProductConfigsKey configsKey;
        if (!isConfigSupported(key) || (configsKey = this.keyHashMap.get(key)) == null) {
            return false;
        }
        if ((configsKey.accessMask & 16) == 16) {
            return true;
        }
        if (configsKey.backupKey != null) {
            return isSubscribable(configsKey.backupKey);
        }
        return false;
    }

    public String getSupportedConfigHashKey(String key) {
        DJISDKCacheProductConfigsKey configKey;
        if (!isConfigSupported(key) || (configKey = this.keyHashMap.get(key)) == null) {
            return null;
        }
        if (configKey.minVersion == -1 && configKey.maxVersion >= this.currentProductVersion) {
            return configKey.keyHashName;
        }
        if (configKey.maxVersion == -1 && configKey.minVersion <= this.currentProductVersion) {
            return configKey.keyHashName;
        }
        if (configKey.minVersion <= this.currentProductVersion && configKey.maxVersion >= this.currentProductVersion) {
            return configKey.keyHashName;
        }
        if (configKey.backupKey != null) {
            return getSupportedConfigHashKey(configKey.backupKey);
        }
        return null;
    }

    public void readConfig(String key, DJISDKCacheHWAbstraction.InnerCallback callback) {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(key);
        readConfig(keys, callback);
    }

    public void readConfig(ArrayList<String> keys, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final String[] configHashKeys = getSupportedConfigHashKeys(keys, null, true).keys;
        new DataFlycGetParams().setInfos(configHashKeys).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.util.configuration.DJISDKCacheProductConfigManager.AnonymousClass2 */

            public void onSuccess(Object model) {
                ArrayList<ParamInfo> paramInfos = new ArrayList<>();
                boolean hasResult = false;
                String[] strArr = configHashKeys;
                for (String hashKey : strArr) {
                    DJIFlycParamInfoManager.getInstance();
                    ParamInfo paramInfo = DJIFlycParamInfoManager.read(hashKey);
                    if (paramInfo != null) {
                        paramInfos.add(paramInfo);
                        hasResult = true;
                    }
                }
                if (hasResult) {
                    CallbackUtils.onSuccess(callback, paramInfos);
                } else {
                    CallbackUtils.onFailure(callback, DJIError.COMMON_UNKNOWN);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void writeConfig(ArrayList<String> keys, ArrayList<Number> values, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ConfigValueStructure configStructures = getSupportedConfigHashKeys(keys, values, false);
        final String[] configHashKeys = configStructures.keys;
        Number[] configHashValues = configStructures.values;
        DataFlycSetParams params = new DataFlycSetParams();
        params.setIndexs(configHashKeys);
        params.setValues(configHashValues);
        params.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.util.configuration.DJISDKCacheProductConfigManager.AnonymousClass3 */

            public void onSuccess(Object model) {
                ArrayList<ParamInfo> paramInfos = new ArrayList<>();
                String[] strArr = configHashKeys;
                for (String hashKey : strArr) {
                    DJIFlycParamInfoManager.getInstance();
                    ParamInfo paramInfo = DJIFlycParamInfoManager.read(hashKey);
                    if (paramInfo != null) {
                        paramInfos.add(paramInfo);
                    }
                }
                callback.onSuccess(paramInfos);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void writeConfig(String key, Number value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(key);
        ArrayList<Number> number = new ArrayList<>();
        number.add(value);
        writeConfig(tmp, number, callback);
    }

    public void readConfigRange(String key, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        String hashKey = getSupportedConfigHashKey(key);
        final DataFlycGetParamInfoByHash data = new DataFlycGetParamInfoByHash();
        data.setIndex(hashKey).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.util.configuration.DJISDKCacheProductConfigManager.AnonymousClass4 */

            public void onSuccess(Object model) {
                ParamInfo paramInfo = data.getParamInfo();
                CallbackUtils.onSuccess(callback, new ConfigRange(paramInfo.range.minValue, paramInfo.range.maxValue, paramInfo.range.defaultValue));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void resetConfig(ArrayList<String> keys, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataFlycResetParamNew().setInfos(getSupportedConfigHashKeys(keys, null, false).keys).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.util.configuration.DJISDKCacheProductConfigManager.AnonymousClass5 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    private void loadKeyHashMapToParamInfoManager() {
        for (DJISDKCacheProductConfigsKey key : this.keyHashMap.values()) {
            ParamInfoBean paramInfoBean = new ParamInfoBean();
            paramInfoBean.index = -1;
            paramInfoBean.attribute = 11;
            paramInfoBean.maxValue = key.maxValue.toString();
            paramInfoBean.minValue = key.minValue.toString();
            paramInfoBean.defaultValue = key.defaultValue.toString();
            paramInfoBean.name = key.keyHashName;
            paramInfoBean.size = key.dataSize;
            paramInfoBean.typeID = getTypeIdFromClassType(key.dataType);
            DJIFlycParamInfoManager.writeNewParamInfo(key.keyHashName, paramInfoBean.getParamInfo());
        }
    }

    private int getTypeIdFromClassType(Class dataType) {
        if (dataType.equals(Double.class)) {
            return 9;
        }
        if (dataType.equals(Integer.class)) {
            return 5;
        }
        if (dataType.equals(Float.class)) {
            return 8;
        }
        if (dataType.equals(Byte.class)) {
            return 10;
        }
        if (dataType.equals(Long.class)) {
            return 7;
        }
        if (dataType.equals(Short.class)) {
            return 4;
        }
        return 8;
    }

    private ConfigValueStructure getSupportedConfigHashKeys(ArrayList<String> keys, ArrayList<Number> values, boolean toReadConfigs) {
        Number[] hashValuesArray;
        boolean readOrWriteFlag;
        String hashKey;
        ArrayList<String> hashKeys = new ArrayList<>();
        ArrayList<Number> hashValues = new ArrayList<>();
        Iterator<String> it2 = keys.iterator();
        while (it2.hasNext()) {
            String key = it2.next();
            if (toReadConfigs) {
                readOrWriteFlag = isReadable(key);
            } else {
                readOrWriteFlag = isWritable(key);
            }
            if (isConfigSupported(key) && readOrWriteFlag && (hashKey = getSupportedConfigHashKey(key)) != null) {
                hashKeys.add(hashKey);
                if (values != null) {
                    hashValues.add(values.get(keys.indexOf(key)));
                }
            }
        }
        String[] hashKeysArray = (String[]) hashKeys.toArray(new String[hashKeys.size()]);
        if (values != null) {
            hashValuesArray = (Number[]) hashValues.toArray(new Number[hashValues.size()]);
        } else {
            hashValuesArray = null;
        }
        return new ConfigValueStructure(hashKeysArray, hashValuesArray);
    }

    private class ConfigValueStructure {
        public String[] keys;
        public Number[] values;

        public ConfigValueStructure(String[] keys2, Number[] values2) {
            this.keys = keys2;
            this.values = values2;
        }
    }
}
