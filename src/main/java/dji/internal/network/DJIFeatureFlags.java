package dji.internal.network;

import com.google.gson.annotations.SerializedName;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.DJIAnalyticsSharedPrefs;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EXClassNullAway
public class DJIFeatureFlags implements Serializable {
    public static final boolean DEFAULT_ANALYTICS_ENABLED = true;
    public static final boolean DEFAULT_BOOLEAN_VALUE = true;
    public static final int DEFAULT_EVENT_BLOCK_SIZE = 20;
    public static final int DEFAULT_EVENT_UPLOAD_MAXIMUM_FREQUENCY = 2;
    public static final int DEFAULT_INT_VALUE = 0;
    public static final int DEFAULT_MAXIMUM_CACHE_SIZE = 2000000;
    public static final boolean DEFAULT_SDKLOG_ENABLED = true;
    public static final int DEFAULT_SESSION_TIMEOUT = 300;
    private static final String EVENT_COUNT_PER_UPLOAD_KEY = "event_count_per_upload";
    private static final String EVENT_UPLOAD_MAXIMUM_FREQUENCY_KEY = "event_upload_maximum_frequency";
    private static final String IS_ANALYTICS_ENABLED_KEY = "is_analytics_enable";
    private static final String IS_COLLECTION_ENABLED_KEY = "is_collection_enabled";
    private static final String MAXIMUM_CACHE_SIZE_KEY = "maximum_cache_size";
    private static final String SESSION_TIME_INTERVAL = "session_reconnection_grace_period_time_interval";
    private static final String THROTTLING_PATH_KEY = "throttling_path_";
    private static final String THROTTLING_VALUE_KEY = "throttling_value_";
    private static final long serialVersionUID = -8534844170998963067L;
    @SerializedName("Analytics")
    private AnalyticsFlags analyticFlags;
    @SerializedName("SDKLogs")
    private SDKLogsFlags sdkLogsFlags;

    public class SDKLogsFlags implements Serializable {
        /* access modifiers changed from: private */
        public List<String> cacheBlackList;
        /* access modifiers changed from: private */
        public List<String> cacheWhiteList;
        /* access modifiers changed from: private */
        public boolean isCollectionOfDelegateCallbackEnabled = true;
        /* access modifiers changed from: private */
        public boolean isCollectionOfPackListenerEnabled = true;
        /* access modifiers changed from: private */
        public boolean isCollectionOfPackReceptionEnabled = true;
        /* access modifiers changed from: private */
        public boolean isCollectionOfPackSendEnabled = true;
        /* access modifiers changed from: private */
        public boolean isCollectionOfPublicAPIEnabled = true;
        /* access modifiers changed from: private */
        public boolean isCollectionOfSDKCacheEnabled = true;
        /* access modifiers changed from: private */
        public boolean isEnabled = true;
        /* access modifiers changed from: private */
        public int numberOfBookEntriesBeforeSave = 0;
        /* access modifiers changed from: private */
        public List<String> packBlackList;
        /* access modifiers changed from: private */
        public List<String> packWhiteList;

        public SDKLogsFlags() {
        }

        public SDKLogsFlags(int numberOfBookEntriesBeforeSave2, boolean isEnabled2, boolean isCollectionOfPublicAPIEnabled2, boolean isCollectionOfDelegateCallbackEnabled2, boolean isCollectionOfSDKCacheEnabled2, boolean isCollectionOfPackListenerEnabled2, boolean isCollectionOfPackSendEnabled2, boolean isCollectionOfPackReceptionEnabled2, List<String> cacheBlackList2, List<String> cacheWhiteList2, List<String> packBlackList2, List<String> packWhiteList2) {
            this.numberOfBookEntriesBeforeSave = numberOfBookEntriesBeforeSave2;
            this.isEnabled = isEnabled2;
            this.isCollectionOfPublicAPIEnabled = isCollectionOfPublicAPIEnabled2;
            this.isCollectionOfDelegateCallbackEnabled = isCollectionOfDelegateCallbackEnabled2;
            this.isCollectionOfSDKCacheEnabled = isCollectionOfSDKCacheEnabled2;
            this.isCollectionOfPackListenerEnabled = isCollectionOfPackListenerEnabled2;
            this.isCollectionOfPackSendEnabled = isCollectionOfPackSendEnabled2;
            this.isCollectionOfPackReceptionEnabled = isCollectionOfPackReceptionEnabled2;
            this.cacheBlackList = cacheBlackList2;
            this.cacheWhiteList = cacheWhiteList2;
            this.packBlackList = packBlackList2;
            this.packWhiteList = packWhiteList2;
        }
    }

    public boolean isSDKLogsEnabled() {
        if (this.sdkLogsFlags != null) {
            return this.sdkLogsFlags.isEnabled;
        }
        return true;
    }

    public int getNumberOfBookEntriesBeforeSave() {
        if (this.sdkLogsFlags != null) {
            return this.sdkLogsFlags.numberOfBookEntriesBeforeSave;
        }
        return 0;
    }

    public boolean isCollectionOfPublicAPIEnabled() {
        if (this.sdkLogsFlags != null) {
            return this.sdkLogsFlags.isCollectionOfPublicAPIEnabled;
        }
        return true;
    }

    public boolean isCollectionOfDelegateCallbackEnabled() {
        if (this.sdkLogsFlags != null) {
            return this.sdkLogsFlags.isCollectionOfDelegateCallbackEnabled;
        }
        return true;
    }

    public boolean isCollectionOfSDKCacheEnabled() {
        if (this.sdkLogsFlags != null) {
            return this.sdkLogsFlags.isCollectionOfSDKCacheEnabled;
        }
        return true;
    }

    public boolean isCollectionOfPackListenerEnabled() {
        if (this.sdkLogsFlags != null) {
            return this.sdkLogsFlags.isCollectionOfPackListenerEnabled;
        }
        return true;
    }

    public boolean isCollectionOfPackSendEnabled() {
        if (this.sdkLogsFlags != null) {
            return this.sdkLogsFlags.isCollectionOfPackSendEnabled;
        }
        return true;
    }

    public boolean isCollectionOfPackReceptionEnabled() {
        if (this.sdkLogsFlags != null) {
            return this.sdkLogsFlags.isCollectionOfPackReceptionEnabled;
        }
        return true;
    }

    public List<String> getCacheBlackList() {
        if (this.sdkLogsFlags == null || this.sdkLogsFlags.cacheBlackList == null) {
            return new ArrayList();
        }
        return this.sdkLogsFlags.cacheBlackList;
    }

    public List<String> getCacheWhiteList() {
        if (this.sdkLogsFlags == null || this.sdkLogsFlags.cacheWhiteList == null) {
            return new ArrayList();
        }
        return this.sdkLogsFlags.cacheWhiteList;
    }

    public List<String> getPackBlackList() {
        if (this.sdkLogsFlags == null || this.sdkLogsFlags.packBlackList == null) {
            return new ArrayList();
        }
        return this.sdkLogsFlags.packBlackList;
    }

    public List<String> getPackWhiteList() {
        if (this.sdkLogsFlags == null || this.sdkLogsFlags.packWhiteList == null) {
            return new ArrayList();
        }
        return this.sdkLogsFlags.packWhiteList;
    }

    public class AnalyticsFlags implements Serializable {
        /* access modifiers changed from: private */
        public int eventCountPerUpload = 20;
        /* access modifiers changed from: private */
        public int eventUploadMaximumFrequency = 2;
        /* access modifiers changed from: private */
        public boolean isDiskPersistenceEnabled = true;
        /* access modifiers changed from: private */
        public boolean isEnabled = true;
        /* access modifiers changed from: private */
        public boolean isFleetManagementEnabled = true;
        /* access modifiers changed from: private */
        public boolean isNetworkingEnabled = true;
        /* access modifiers changed from: private */
        public int maximumCacheSize = DJIFeatureFlags.DEFAULT_MAXIMUM_CACHE_SIZE;
        /* access modifiers changed from: private */
        public int periodicCacheFlushTimerInterval = 0;
        /* access modifiers changed from: private */
        public int sessionReconnectionGracePeriodTimeInterval = 300;
        /* access modifiers changed from: private */
        public Map<String, Double> throttledKeypaths;

        public AnalyticsFlags() {
        }

        public AnalyticsFlags(boolean isEnabled2, boolean isFleetManagementEnabled2, boolean isNetworkingEnabled2, boolean isDiskPersistenceEnabled2, int eventCountPerUpload2, int periodicCacheFlushTimerInterval2, int eventUploadMaximumFrequency2, int maximumCacheSize2, int sessionReconnectionGracePeriodTimeInterval2, Map<String, Double> throttledKeypaths2) {
            this.isEnabled = isEnabled2;
            this.isFleetManagementEnabled = isFleetManagementEnabled2;
            this.isNetworkingEnabled = isNetworkingEnabled2;
            this.isDiskPersistenceEnabled = isDiskPersistenceEnabled2;
            this.eventCountPerUpload = eventCountPerUpload2;
            this.periodicCacheFlushTimerInterval = periodicCacheFlushTimerInterval2;
            this.eventUploadMaximumFrequency = eventUploadMaximumFrequency2;
            this.maximumCacheSize = maximumCacheSize2;
            this.sessionReconnectionGracePeriodTimeInterval = sessionReconnectionGracePeriodTimeInterval2;
            this.throttledKeypaths = throttledKeypaths2;
        }
    }

    public boolean isAnalyticsEnabled() {
        if (this.analyticFlags != null) {
            return this.analyticFlags.isEnabled;
        }
        return true;
    }

    public boolean isFleetManagementEnabled() {
        if (this.analyticFlags != null) {
            return this.analyticFlags.isFleetManagementEnabled;
        }
        return true;
    }

    public boolean isNetworkingEnabled() {
        if (this.analyticFlags != null) {
            return this.analyticFlags.isNetworkingEnabled;
        }
        return true;
    }

    public boolean isDiskPersistenceEnabled() {
        if (this.analyticFlags != null) {
            return this.analyticFlags.isDiskPersistenceEnabled;
        }
        return true;
    }

    public int getEventCountPerUpload() {
        if (this.analyticFlags != null) {
            return this.analyticFlags.eventCountPerUpload;
        }
        return 20;
    }

    public int getPeriodicCacheFlushTimerInterval() {
        if (this.analyticFlags != null) {
            return this.analyticFlags.periodicCacheFlushTimerInterval;
        }
        return 0;
    }

    public int getEventUploadMaximumFrequency() {
        if (this.analyticFlags != null) {
            return this.analyticFlags.eventUploadMaximumFrequency;
        }
        return 2;
    }

    public int getMaximumCacheSize() {
        if (this.analyticFlags != null) {
            return this.analyticFlags.maximumCacheSize;
        }
        return DEFAULT_MAXIMUM_CACHE_SIZE;
    }

    public Map<String, Double> getThrottling() {
        if (this.analyticFlags == null || this.analyticFlags.throttledKeypaths == null) {
            return new HashMap();
        }
        return this.analyticFlags.throttledKeypaths;
    }

    public int getSessionReconnectionGracePeriodTimeInterval() {
        if (this.analyticFlags != null) {
            return this.analyticFlags.sessionReconnectionGracePeriodTimeInterval;
        }
        return 300;
    }

    public DJIFeatureFlags() {
        this.sdkLogsFlags = new SDKLogsFlags();
        this.analyticFlags = new AnalyticsFlags();
    }

    public DJIFeatureFlags(SDKLogsFlags sdkLogsFlags2, AnalyticsFlags analyticFlags2) {
        this.sdkLogsFlags = sdkLogsFlags2;
        this.analyticFlags = analyticFlags2;
    }

    public boolean hasFlags() {
        return (this.sdkLogsFlags == null && this.analyticFlags == null) ? false : true;
    }

    public void saveToLocal() {
        if (this.analyticFlags != null) {
            DJIAnalyticsSharedPrefs.setBooleanPref(IS_ANALYTICS_ENABLED_KEY, this.analyticFlags.isEnabled);
            DJIAnalyticsSharedPrefs.setIntegerPref(EVENT_COUNT_PER_UPLOAD_KEY, this.analyticFlags.eventCountPerUpload);
            DJIAnalyticsSharedPrefs.setBooleanPref(IS_COLLECTION_ENABLED_KEY, this.sdkLogsFlags.isEnabled);
            DJIAnalyticsSharedPrefs.setIntegerPref(EVENT_UPLOAD_MAXIMUM_FREQUENCY_KEY, this.analyticFlags.eventUploadMaximumFrequency);
            DJIAnalyticsSharedPrefs.setIntegerPref(MAXIMUM_CACHE_SIZE_KEY, this.analyticFlags.maximumCacheSize);
            DJIAnalyticsSharedPrefs.setIntegerPref(SESSION_TIME_INTERVAL, this.analyticFlags.sessionReconnectionGracePeriodTimeInterval);
            Map<String, Double> throtting = getThrottling();
            if (throtting != null) {
                int index = 0;
                for (Map.Entry<String, Double> entry : throtting.entrySet()) {
                    Object value = entry.getValue();
                    DJIAnalyticsSharedPrefs.setStringPref(THROTTLING_PATH_KEY + index, (String) entry.getKey());
                    DJIAnalyticsSharedPrefs.setStringPref(THROTTLING_VALUE_KEY + index, value.toString());
                    index++;
                }
            }
        }
    }

    public static DJIFeatureFlags loadFromLocal() {
        DJIFeatureFlags flag = new DJIFeatureFlags();
        if (DJIAnalyticsSharedPrefs.containsKey(IS_ANALYTICS_ENABLED_KEY)) {
            boolean unused = flag.analyticFlags.isEnabled = DJIAnalyticsSharedPrefs.getBooleanPref(IS_ANALYTICS_ENABLED_KEY);
        } else {
            boolean unused2 = flag.analyticFlags.isEnabled = true;
        }
        if (DJIAnalyticsSharedPrefs.containsKey(IS_COLLECTION_ENABLED_KEY)) {
            boolean unused3 = flag.sdkLogsFlags.isEnabled = DJIAnalyticsSharedPrefs.getBooleanPref(IS_COLLECTION_ENABLED_KEY);
        } else {
            boolean unused4 = flag.sdkLogsFlags.isEnabled = true;
        }
        if (DJIAnalyticsSharedPrefs.containsKey(EVENT_COUNT_PER_UPLOAD_KEY)) {
            int unused5 = flag.analyticFlags.eventCountPerUpload = DJIAnalyticsSharedPrefs.getIntegerPref(EVENT_COUNT_PER_UPLOAD_KEY);
        } else {
            int unused6 = flag.analyticFlags.eventCountPerUpload = 20;
        }
        if (DJIAnalyticsSharedPrefs.containsKey(EVENT_UPLOAD_MAXIMUM_FREQUENCY_KEY)) {
            int unused7 = flag.analyticFlags.eventUploadMaximumFrequency = DJIAnalyticsSharedPrefs.getIntegerPref(EVENT_UPLOAD_MAXIMUM_FREQUENCY_KEY);
        } else {
            int unused8 = flag.analyticFlags.eventUploadMaximumFrequency = 2;
        }
        if (DJIAnalyticsSharedPrefs.containsKey(MAXIMUM_CACHE_SIZE_KEY)) {
            int unused9 = flag.analyticFlags.maximumCacheSize = DJIAnalyticsSharedPrefs.getIntegerPref(MAXIMUM_CACHE_SIZE_KEY);
        } else {
            int unused10 = flag.analyticFlags.maximumCacheSize = DEFAULT_MAXIMUM_CACHE_SIZE;
        }
        if (DJIAnalyticsSharedPrefs.containsKey(SESSION_TIME_INTERVAL)) {
            int unused11 = flag.analyticFlags.sessionReconnectionGracePeriodTimeInterval = DJIAnalyticsSharedPrefs.getIntegerPref(SESSION_TIME_INTERVAL);
        } else {
            int unused12 = flag.analyticFlags.sessionReconnectionGracePeriodTimeInterval = 300;
        }
        for (int index = 0; DJIAnalyticsSharedPrefs.containsKey(THROTTLING_PATH_KEY + index); index++) {
            if (flag.analyticFlags.throttledKeypaths == null) {
                Map unused13 = flag.analyticFlags.throttledKeypaths = new HashMap();
            }
            flag.analyticFlags.throttledKeypaths.put(DJIAnalyticsSharedPrefs.getStringPref(THROTTLING_PATH_KEY + index), Double.valueOf(DJIAnalyticsSharedPrefs.getStringPref(THROTTLING_VALUE_KEY + index)));
        }
        return flag;
    }
}
