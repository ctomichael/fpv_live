package com.mapbox.mapboxsdk.module.telemetry;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.mapbox.android.telemetry.TelemetryUtils;

class PhoneState {
    private static final String NO_CARRIER = "EMPTY_CARRIER";
    private static final int NO_NETWORK = -1;
    private float accessibilityFontScale;
    private int batteryLevel;
    private String carrier;
    private String cellularNetworkType;
    private String created;
    private Orientation orientation;
    private boolean pluggedIn;
    private float resolution;
    private boolean wifi;

    PhoneState() {
    }

    PhoneState(@NonNull Context context) {
        this.created = TelemetryUtils.obtainCurrentDate();
        this.batteryLevel = TelemetryUtils.obtainBatteryLevel(context);
        this.pluggedIn = TelemetryUtils.isPluggedIn(context);
        this.cellularNetworkType = TelemetryUtils.obtainCellularNetworkType(context);
        this.orientation = Orientation.getOrientation(context.getResources().getConfiguration().orientation);
        this.accessibilityFontScale = context.getResources().getConfiguration().fontScale;
        this.carrier = obtainCellularCarrier(context);
        this.resolution = obtainDisplayDensity(context);
        this.wifi = isConnectedToWifi(context);
    }

    private String obtainCellularCarrier(@NonNull Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService("phone");
        if (manager == null) {
            return NO_CARRIER;
        }
        String carrierName = manager.getNetworkOperatorName();
        if (TextUtils.isEmpty(carrierName)) {
            return NO_CARRIER;
        }
        return carrierName;
    }

    private float obtainDisplayDensity(@NonNull Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.density;
    }

    private boolean isConnectedToWifi(@NonNull Context context) {
        try {
            WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService("wifi");
            if (wifiMgr == null) {
                return false;
            }
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            if (!wifiMgr.isWifiEnabled() || wifiInfo.getNetworkId() == -1) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public String getCreated() {
        return this.created;
    }

    /* access modifiers changed from: package-private */
    public void setCreated(String created2) {
        this.created = created2;
    }

    /* access modifiers changed from: package-private */
    public int getBatteryLevel() {
        return this.batteryLevel;
    }

    /* access modifiers changed from: package-private */
    public void setBatteryLevel(int batteryLevel2) {
        this.batteryLevel = batteryLevel2;
    }

    /* access modifiers changed from: package-private */
    public boolean isPluggedIn() {
        return this.pluggedIn;
    }

    /* access modifiers changed from: package-private */
    public void setPluggedIn(boolean pluggedIn2) {
        this.pluggedIn = pluggedIn2;
    }

    /* access modifiers changed from: package-private */
    public String getCellularNetworkType() {
        return this.cellularNetworkType;
    }

    /* access modifiers changed from: package-private */
    public void setCellularNetworkType(String cellularNetworkType2) {
        this.cellularNetworkType = cellularNetworkType2;
    }

    /* access modifiers changed from: package-private */
    public String getOrientation() {
        return this.orientation.getOrientation();
    }

    /* access modifiers changed from: package-private */
    public void setOrientation(Orientation orientation2) {
        this.orientation = orientation2;
    }

    /* access modifiers changed from: package-private */
    public String getCarrier() {
        return this.carrier;
    }

    /* access modifiers changed from: package-private */
    public void setCarrier(String carrier2) {
        this.carrier = carrier2;
    }

    /* access modifiers changed from: package-private */
    public boolean isWifi() {
        return this.wifi;
    }

    /* access modifiers changed from: package-private */
    public void setWifi(boolean wifi2) {
        this.wifi = wifi2;
    }

    /* access modifiers changed from: package-private */
    public float getAccessibilityFontScale() {
        return this.accessibilityFontScale;
    }

    /* access modifiers changed from: package-private */
    public void setAccessibilityFontScale(float accessibilityFontScale2) {
        this.accessibilityFontScale = accessibilityFontScale2;
    }

    /* access modifiers changed from: package-private */
    public float getResolution() {
        return this.resolution;
    }

    /* access modifiers changed from: package-private */
    public void setResolution(float resolution2) {
        this.resolution = resolution2;
    }

    enum Orientation {
        ORIENTATION_PORTRAIT("Portrait"),
        ORIENTATION_LANDSCAPE("Landscape");
        
        private String orientation;

        private Orientation(String orientation2) {
            this.orientation = orientation2;
        }

        public static Orientation getOrientation(int index) {
            if (1 == index) {
                return ORIENTATION_PORTRAIT;
            }
            return ORIENTATION_LANDSCAPE;
        }

        public String getOrientation() {
            return this.orientation;
        }
    }
}
