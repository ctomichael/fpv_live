package com.mapbox.mapboxsdk.module.telemetry;

import android.annotation.SuppressLint;
import android.os.Build;
import com.mapbox.mapboxsdk.BuildConfig;

@SuppressLint({"ParcelCreator"})
class MapLoadEvent extends MapBaseEvent {
    private static final String EVENT_NAME = "map.load";
    private final float accessibilityFontScale;
    private final int batteryLevel;
    private final String carrier;
    private final String cellularNetworkType;
    private final String model = Build.MODEL;
    private final String operatingSystem = ("Android - " + Build.VERSION.RELEASE);
    private final String orientation;
    private final boolean pluggedIn;
    private final float resolution;
    private final String sdkIdentifier = BuildConfig.MAPBOX_SDK_IDENTIFIER;
    private final String sdkVersion = BuildConfig.MAPBOX_SDK_VERSION;
    private final String userId;
    private final boolean wifi;

    MapLoadEvent(String userId2, PhoneState phoneState) {
        super(phoneState);
        this.userId = userId2;
        this.batteryLevel = phoneState.getBatteryLevel();
        this.pluggedIn = phoneState.isPluggedIn();
        this.cellularNetworkType = phoneState.getCellularNetworkType();
        this.carrier = phoneState.getCarrier();
        this.resolution = phoneState.getResolution();
        this.accessibilityFontScale = phoneState.getAccessibilityFontScale();
        this.wifi = phoneState.isWifi();
        this.orientation = phoneState.getOrientation();
    }

    /* access modifiers changed from: package-private */
    public String getEventName() {
        return EVENT_NAME;
    }

    /* access modifiers changed from: package-private */
    public String getOperatingSystem() {
        return this.operatingSystem;
    }

    /* access modifiers changed from: package-private */
    public String getSdkIdentifier() {
        return BuildConfig.MAPBOX_SDK_IDENTIFIER;
    }

    /* access modifiers changed from: package-private */
    public String getSdkVersion() {
        return BuildConfig.MAPBOX_SDK_VERSION;
    }

    /* access modifiers changed from: package-private */
    public String getModel() {
        return this.model;
    }

    /* access modifiers changed from: package-private */
    public String getUserId() {
        return this.userId;
    }

    /* access modifiers changed from: package-private */
    public String getCarrier() {
        return this.carrier;
    }

    /* access modifiers changed from: package-private */
    public String getCellularNetworkType() {
        return this.cellularNetworkType;
    }

    /* access modifiers changed from: package-private */
    public String getOrientation() {
        return this.orientation;
    }

    /* access modifiers changed from: package-private */
    public float getResolution() {
        return this.resolution;
    }

    /* access modifiers changed from: package-private */
    public float getAccessibilityFontScale() {
        return this.accessibilityFontScale;
    }

    /* access modifiers changed from: package-private */
    public int getBatteryLevel() {
        return this.batteryLevel;
    }

    /* access modifiers changed from: package-private */
    public boolean isPluggedIn() {
        return this.pluggedIn;
    }

    /* access modifiers changed from: package-private */
    public boolean isWifi() {
        return this.wifi;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MapLoadEvent that = (MapLoadEvent) o;
        if (Float.compare(that.resolution, this.resolution) != 0 || Float.compare(that.accessibilityFontScale, this.accessibilityFontScale) != 0 || this.batteryLevel != that.batteryLevel || this.pluggedIn != that.pluggedIn || this.wifi != that.wifi || !this.operatingSystem.equals(that.operatingSystem)) {
            return false;
        }
        if (this.model != null) {
            if (!this.model.equals(that.model)) {
                return false;
            }
        } else if (that.model != null) {
            return false;
        }
        if (this.userId != null) {
            if (!this.userId.equals(that.userId)) {
                return false;
            }
        } else if (that.userId != null) {
            return false;
        }
        if (this.carrier != null) {
            if (!this.carrier.equals(that.carrier)) {
                return false;
            }
        } else if (that.carrier != null) {
            return false;
        }
        if (this.cellularNetworkType != null) {
            if (!this.cellularNetworkType.equals(that.cellularNetworkType)) {
                return false;
            }
        } else if (that.cellularNetworkType != null) {
            return false;
        }
        if (this.orientation != null) {
            z = this.orientation.equals(that.orientation);
        } else if (that.orientation != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9 = 1;
        if (this.operatingSystem != null) {
            result = this.operatingSystem.hashCode();
        } else {
            result = 0;
        }
        int hashCode = ((((result * 31) + BuildConfig.MAPBOX_SDK_IDENTIFIER.hashCode()) * 31) + BuildConfig.MAPBOX_SDK_VERSION.hashCode()) * 31;
        if (this.model != null) {
            i = this.model.hashCode();
        } else {
            i = 0;
        }
        int i10 = (hashCode + i) * 31;
        if (this.userId != null) {
            i2 = this.userId.hashCode();
        } else {
            i2 = 0;
        }
        int i11 = (i10 + i2) * 31;
        if (this.carrier != null) {
            i3 = this.carrier.hashCode();
        } else {
            i3 = 0;
        }
        int i12 = (i11 + i3) * 31;
        if (this.cellularNetworkType != null) {
            i4 = this.cellularNetworkType.hashCode();
        } else {
            i4 = 0;
        }
        int i13 = (i12 + i4) * 31;
        if (this.orientation != null) {
            i5 = this.orientation.hashCode();
        } else {
            i5 = 0;
        }
        int i14 = (i13 + i5) * 31;
        if (this.resolution != 0.0f) {
            i6 = Float.floatToIntBits(this.resolution);
        } else {
            i6 = 0;
        }
        int i15 = (i14 + i6) * 31;
        if (this.accessibilityFontScale != 0.0f) {
            i7 = Float.floatToIntBits(this.accessibilityFontScale);
        } else {
            i7 = 0;
        }
        int i16 = (((i15 + i7) * 31) + this.batteryLevel) * 31;
        if (this.pluggedIn) {
            i8 = 1;
        } else {
            i8 = 0;
        }
        int i17 = (i16 + i8) * 31;
        if (!this.wifi) {
            i9 = 0;
        }
        return i17 + i9;
    }

    public String toString() {
        return "MapLoadEvent{, operatingSystem='" + this.operatingSystem + '\'' + ", sdkIdentifier='" + BuildConfig.MAPBOX_SDK_IDENTIFIER + '\'' + ", sdkVersion='" + BuildConfig.MAPBOX_SDK_VERSION + '\'' + ", model='" + this.model + '\'' + ", userId='" + this.userId + '\'' + ", carrier='" + this.carrier + '\'' + ", cellularNetworkType='" + this.cellularNetworkType + '\'' + ", orientation='" + this.orientation + '\'' + ", resolution=" + this.resolution + ", accessibilityFontScale=" + this.accessibilityFontScale + ", batteryLevel=" + this.batteryLevel + ", pluggedIn=" + this.pluggedIn + ", wifi=" + this.wifi + '}';
    }
}
