package dji.internal.logics;

import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.network.DJIFeatureFlags;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@EXClassNullAway
public class ThrottlingManager {
    private Map<String, Double> keyToMinElapsedTimeMap;
    private Map<String, Long> keyToTimestampMap;

    private ThrottlingManager() {
        this.keyToMinElapsedTimeMap = new ConcurrentHashMap();
        this.keyToTimestampMap = new ConcurrentHashMap();
    }

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final ThrottlingManager INSTANCE = new ThrottlingManager();

        private LazyHolder() {
        }
    }

    public static ThrottlingManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void destroy() {
        if (this.keyToTimestampMap != null) {
            this.keyToTimestampMap.clear();
            this.keyToTimestampMap = null;
        }
        if (this.keyToMinElapsedTimeMap != null) {
            this.keyToMinElapsedTimeMap.clear();
            this.keyToMinElapsedTimeMap = null;
        }
    }

    public void initWithFeatureFlags(DJIFeatureFlags featureFlags) {
        if (featureFlags != null && featureFlags.getThrottling() != null) {
            this.keyToMinElapsedTimeMap = featureFlags.getThrottling();
        }
    }

    public boolean shouldTrackThisEventWithKey(String keyOfEvent) {
        if (this.keyToMinElapsedTimeMap == null || !this.keyToMinElapsedTimeMap.containsKey(keyOfEvent)) {
            return true;
        }
        long now = System.currentTimeMillis();
        if (!this.keyToTimestampMap.containsKey(keyOfEvent)) {
            this.keyToTimestampMap.put(keyOfEvent, Long.valueOf(now));
            return true;
        } else if (now - this.keyToTimestampMap.get(keyOfEvent).longValue() < ((long) ((int) (this.keyToMinElapsedTimeMap.get(keyOfEvent).doubleValue() * 1000.0d)))) {
            return false;
        } else {
            this.keyToTimestampMap.put(keyOfEvent, Long.valueOf(now));
            return true;
        }
    }
}
