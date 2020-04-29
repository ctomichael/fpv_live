package com.mapbox.android.telemetry;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.mapbox.android.telemetry.Event;
import com.mapbox.mapboxsdk.style.layers.Property;
import dji.publics.protocol.ResponseBase;

public class LocationEvent extends Event implements Parcelable {
    public static final Parcelable.Creator<LocationEvent> CREATOR = new Parcelable.Creator<LocationEvent>() {
        /* class com.mapbox.android.telemetry.LocationEvent.AnonymousClass1 */

        public LocationEvent createFromParcel(Parcel in2) {
            return new LocationEvent(in2);
        }

        public LocationEvent[] newArray(int size) {
            return new LocationEvent[size];
        }
    };
    private static final String LOCATION = "location";
    private static final String OPERATING_SYSTEM = ("Android - " + Build.VERSION.RELEASE);
    private static final String SOURCE_MAPBOX = "mapbox";
    @SerializedName("horizontalAccuracy")
    private Float accuracy;
    @SerializedName("altitude")
    private Double altitude;
    @SerializedName("applicationState")
    private String applicationState;
    @SerializedName("created")
    private final String created;
    @SerializedName("event")
    private final String event;
    @SerializedName(ResponseBase.STRING_LAT)
    private final double latitude;
    @SerializedName(ResponseBase.STRING_LNG)
    private final double longitude;
    @SerializedName("operatingSystem")
    private String operatingSystem;
    @SerializedName("sessionId")
    private final String sessionId;
    @SerializedName(Property.SYMBOL_Z_ORDER_SOURCE)
    private String source;

    public LocationEvent(String sessionId2, double latitude2, double longitude2, String applicationState2) {
        this.altitude = null;
        this.accuracy = null;
        this.event = "location";
        this.created = TelemetryUtils.obtainCurrentDate();
        this.source = SOURCE_MAPBOX;
        this.sessionId = sessionId2;
        this.latitude = latitude2;
        this.longitude = longitude2;
        this.operatingSystem = OPERATING_SYSTEM;
        this.applicationState = applicationState2;
    }

    /* access modifiers changed from: package-private */
    public Event.Type obtainType() {
        return Event.Type.LOCATION;
    }

    /* access modifiers changed from: package-private */
    public String getEvent() {
        return this.event;
    }

    /* access modifiers changed from: package-private */
    public String getSource() {
        return this.source;
    }

    /* access modifiers changed from: package-private */
    public double getLatitude() {
        return this.latitude;
    }

    /* access modifiers changed from: package-private */
    public double getLongitude() {
        return this.longitude;
    }

    /* access modifiers changed from: package-private */
    public Double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(Double altitude2) {
        this.altitude = altitude2;
    }

    /* access modifiers changed from: package-private */
    public String getOperatingSystem() {
        return this.operatingSystem;
    }

    /* access modifiers changed from: package-private */
    public Float getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(Float accuracy2) {
        this.accuracy = accuracy2;
    }

    private LocationEvent(Parcel in2) {
        Float f = null;
        this.altitude = null;
        this.accuracy = null;
        this.event = in2.readString();
        this.created = in2.readString();
        this.source = in2.readString();
        this.sessionId = in2.readString();
        this.latitude = in2.readDouble();
        this.longitude = in2.readDouble();
        this.altitude = in2.readByte() == 0 ? null : Double.valueOf(in2.readDouble());
        this.operatingSystem = in2.readString();
        this.applicationState = in2.readString();
        this.accuracy = in2.readByte() != 0 ? Float.valueOf(in2.readFloat()) : f;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.event);
        dest.writeString(this.created);
        dest.writeString(this.source);
        dest.writeString(this.sessionId);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        if (this.altitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(this.altitude.doubleValue());
        }
        dest.writeString(this.operatingSystem);
        dest.writeString(this.applicationState);
        if (this.accuracy == null) {
            dest.writeByte((byte) 0);
            return;
        }
        dest.writeByte((byte) 1);
        dest.writeFloat(this.accuracy.floatValue());
    }
}
