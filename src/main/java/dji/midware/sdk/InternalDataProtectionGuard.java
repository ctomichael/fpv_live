package dji.midware.sdk;

public class InternalDataProtectionGuard {
    public static final int NO_ERROR = Integer.MAX_VALUE;
    private boolean isDJIDeviceGPSInfoAuthorized;
    private boolean isHardwareInfoAuthorized;
    private boolean isMobileDeviceGPSInfoAuthorized;
    private final boolean isObfuscatedGPSLocationAuthorized;

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final InternalDataProtectionGuard INSTANCE = new InternalDataProtectionGuard();

        private LazyHolder() {
        }
    }

    public static InternalDataProtectionGuard getInstance() {
        return LazyHolder.INSTANCE;
    }

    private InternalDataProtectionGuard() {
        this.isMobileDeviceGPSInfoAuthorized = false;
        this.isDJIDeviceGPSInfoAuthorized = false;
        this.isHardwareInfoAuthorized = false;
        this.isObfuscatedGPSLocationAuthorized = true;
    }

    public int setHardwareInfoAuthorized(boolean hardwareInfoAuthorized) {
        this.isHardwareInfoAuthorized = hardwareInfoAuthorized;
        return Integer.MAX_VALUE;
    }

    public boolean isHardwareInfoAuthorized() {
        return this.isHardwareInfoAuthorized;
    }

    public boolean isObfuscatedLocationAuthorized() {
        return true;
    }
}
