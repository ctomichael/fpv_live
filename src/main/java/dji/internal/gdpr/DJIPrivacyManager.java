package dji.internal.gdpr;

public class DJIPrivacyManager {
    private boolean hasAnalyticsEventPermission;
    private boolean hasDeviceInfoPermission;

    private DJIPrivacyManager() {
    }

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static DJIPrivacyManager mInstance = new DJIPrivacyManager();

        private LazyHolder() {
        }
    }

    public static DJIPrivacyManager getInstance() {
        return LazyHolder.mInstance;
    }

    public boolean hasDeviceInfoPermission() {
        return this.hasDeviceInfoPermission;
    }

    public void setDeviceInfoPermission(boolean hasDeviceInfoPermission2) {
        this.hasDeviceInfoPermission = hasDeviceInfoPermission2;
    }

    public boolean hasAnalyticsEventPermission() {
        return this.hasAnalyticsEventPermission;
    }

    public void setAnalyticsEventPermission(boolean hasAnalyticsEventPermission2) {
        this.hasAnalyticsEventPermission = hasAnalyticsEventPermission2;
    }
}
