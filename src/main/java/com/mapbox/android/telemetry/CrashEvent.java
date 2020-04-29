package com.mapbox.android.telemetry;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.text.TextUtils;
import com.mapbox.android.telemetry.Event;

@SuppressLint({"ParcelCreator"})
public class CrashEvent extends Event {
    private String appId;
    private String appVersion;
    private final String created;
    private String device;
    private final String event;
    private String isSilent;
    private String model;
    private String osVersion;
    private String sdkIdentifier;
    private String sdkVersion;
    private String stackTrace;
    private String stackTraceHash;
    private String threadDetails;

    public CrashEvent(String event2, String created2) {
        this.event = event2;
        this.created = created2;
    }

    /* access modifiers changed from: package-private */
    public Event.Type obtainType() {
        return Event.Type.CRASH;
    }

    public String getHash() {
        return this.stackTraceHash;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(this.event) && !TextUtils.isEmpty(this.created) && !TextUtils.isEmpty(this.stackTraceHash);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }
}
