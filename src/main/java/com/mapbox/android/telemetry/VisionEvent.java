package com.mapbox.android.telemetry;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.mapbox.android.telemetry.Event;
import java.util.HashMap;

public class VisionEvent extends Event implements Parcelable {
    public static final Parcelable.Creator<VisionEvent> CREATOR = new Parcelable.Creator<VisionEvent>() {
        /* class com.mapbox.android.telemetry.VisionEvent.AnonymousClass1 */

        public VisionEvent createFromParcel(Parcel in2) {
            return new VisionEvent(in2);
        }

        public VisionEvent[] newArray(int size) {
            return new VisionEvent[size];
        }
    };
    private static final String VIS_GENERAL = "vision.general";
    @SerializedName("contents")
    private HashMap<String, Object> contents;
    @SerializedName("event")
    private final String event;
    @SerializedName("name")
    private String name;

    VisionEvent() {
        this.name = "";
        this.contents = new HashMap<>();
        this.event = VIS_GENERAL;
    }

    /* access modifiers changed from: package-private */
    public Event.Type obtainType() {
        return Event.Type.VIS_GENERAL;
    }

    public void setContents(HashMap<String, Object> contents2) {
        this.contents = contents2;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public HashMap<String, Object> getContents() {
        return this.contents;
    }

    private VisionEvent(Parcel in2) {
        this.name = "";
        this.contents = new HashMap<>();
        this.event = in2.readString();
        this.name = in2.readString();
        this.contents = (HashMap) in2.readSerializable();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.event);
        dest.writeString(this.name);
        dest.writeSerializable(this.contents);
    }
}
