package com.mapbox.android.telemetry;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import com.mapbox.android.telemetry.Event;

public class AppUserTurnstile extends Event implements Parcelable {
    private static final String APPLICATION_CONTEXT_CANT_BE_NULL = "Create a MapboxTelemetry instance before calling this method.";
    private static final String APP_USER_TURNSTILE = "appUserTurnstile";
    public static final Parcelable.Creator<AppUserTurnstile> CREATOR = new Parcelable.Creator<AppUserTurnstile>() {
        /* class com.mapbox.android.telemetry.AppUserTurnstile.AnonymousClass1 */

        public AppUserTurnstile createFromParcel(Parcel in2) {
            return new AppUserTurnstile(in2);
        }

        public AppUserTurnstile[] newArray(int size) {
            return new AppUserTurnstile[size];
        }
    };
    private static final String OPERATING_SYSTEM = ("Android - " + Build.VERSION.RELEASE);
    private final String created;
    private final String device;
    @SerializedName("enabled.telemetry")
    private final boolean enabledTelemetry;
    private final String event;
    private final String model;
    private final String operatingSystem;
    private final String sdkIdentifier;
    private final String sdkVersion;
    private String skuId;
    private final String userId;

    public AppUserTurnstile(String sdkIdentifier2, String sdkVersion2) {
        this(sdkIdentifier2, sdkVersion2, true);
    }

    AppUserTurnstile(String sdkIdentifier2, String sdkVersion2, boolean isFromPreferences) {
        checkApplicationContext();
        this.event = APP_USER_TURNSTILE;
        this.created = TelemetryUtils.obtainCurrentDate();
        this.userId = TelemetryUtils.retrieveVendorId();
        this.enabledTelemetry = TelemetryEnabler.TELEMETRY_STATES.get(new TelemetryEnabler(isFromPreferences).obtainTelemetryState()).booleanValue();
        this.device = Build.DEVICE;
        this.sdkIdentifier = sdkIdentifier2;
        this.sdkVersion = sdkVersion2;
        this.model = Build.MODEL;
        this.operatingSystem = OPERATING_SYSTEM;
    }

    @Nullable
    public String getSkuId() {
        return this.skuId;
    }

    public void setSkuId(@NonNull String skuId2) {
        if (skuId2 != null && skuId2.length() != 0) {
            this.skuId = skuId2;
        }
    }

    /* access modifiers changed from: package-private */
    public Event.Type obtainType() {
        return Event.Type.TURNSTILE;
    }

    private AppUserTurnstile(Parcel in2) {
        this.event = in2.readString();
        this.created = in2.readString();
        this.userId = in2.readString();
        this.enabledTelemetry = in2.readByte() != 0;
        this.device = in2.readString();
        this.sdkIdentifier = in2.readString();
        this.sdkVersion = in2.readString();
        this.model = in2.readString();
        this.operatingSystem = in2.readString();
        this.skuId = in2.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.event);
        dest.writeString(this.created);
        dest.writeString(this.userId);
        dest.writeByte((byte) (this.enabledTelemetry ? 1 : 0));
        dest.writeString(this.device);
        dest.writeString(this.sdkIdentifier);
        dest.writeString(this.sdkVersion);
        dest.writeString(this.model);
        dest.writeString(this.operatingSystem);
        dest.writeString(this.skuId);
    }

    private void checkApplicationContext() {
        if (MapboxTelemetry.applicationContext == null) {
            throw new IllegalStateException(APPLICATION_CONTEXT_CANT_BE_NULL);
        }
    }
}
