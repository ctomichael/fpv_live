package com.mapbox.mapboxsdk.module.telemetry;

import android.annotation.SuppressLint;
import android.os.Parcel;
import com.mapbox.android.telemetry.Event;

@SuppressLint({"ParcelCreator"})
abstract class MapBaseEvent extends Event {
    private final String created;
    private final String event = getEventName();

    /* access modifiers changed from: package-private */
    public abstract String getEventName();

    MapBaseEvent(PhoneState phoneState) {
        this.created = phoneState.getCreated();
    }

    /* access modifiers changed from: package-private */
    public String getEvent() {
        return this.event;
    }

    /* access modifiers changed from: package-private */
    public String getCreated() {
        return this.created;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
    }
}
