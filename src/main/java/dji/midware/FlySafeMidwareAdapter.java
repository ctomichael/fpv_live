package dji.midware;

import android.location.Location;
import dji.midware.usb.P3.DJIUsbAccessoryReceiver;

public class FlySafeMidwareAdapter {
    private boolean mMockUseLicenseUnlock;
    private double mockLatitude;
    private double mockLongitude;
    private boolean mockNetworkStatus;

    private static class LazyHolder {
        /* access modifiers changed from: private */
        public static final FlySafeMidwareAdapter INSTANCE = new FlySafeMidwareAdapter();

        private LazyHolder() {
        }
    }

    public static FlySafeMidwareAdapter getInstance() {
        return LazyHolder.INSTANCE;
    }

    public boolean getMockNetWorkStatus() {
        return this.mockNetworkStatus;
    }

    public void setMockNetWorkStatus(boolean status) {
        this.mockNetworkStatus = status;
    }

    public FlySafeMidwareAdapter setMockUseLicenseUnlock(boolean mockUseLicenseUnlock) {
        this.mMockUseLicenseUnlock = mockUseLicenseUnlock;
        return this;
    }

    public boolean isMockUseLicenseUnlock() {
        return this.mMockUseLicenseUnlock;
    }

    public void setMockLocation(double latitude, double longitude) {
        this.mockLatitude = latitude;
        this.mockLongitude = longitude;
    }

    public Location getMockLocation(Location location) {
        location.setLatitude(this.mockLatitude);
        location.setLongitude(this.mockLongitude);
        return new Location(location);
    }

    public Location getMockLocation() {
        Location location = new Location(DJIUsbAccessoryReceiver.myFacturer);
        location.setLatitude(this.mockLatitude);
        location.setLongitude(this.mockLongitude);
        return location;
    }
}
