package com.mapzen.android.lost.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.mapzen.android.lost.api.Geofence;

public class ParcelableGeofence implements Geofence, Parcelable {
    public static final Parcelable.Creator<ParcelableGeofence> CREATOR = new Parcelable.Creator<ParcelableGeofence>() {
        /* class com.mapzen.android.lost.internal.ParcelableGeofence.AnonymousClass1 */

        public ParcelableGeofence createFromParcel(Parcel in2) {
            return new ParcelableGeofence(in2);
        }

        public ParcelableGeofence[] newArray(int size) {
            return new ParcelableGeofence[size];
        }
    };
    private long durationMillis = -1;
    private double latitude;
    private int loiteringDelayMs;
    private double longitude;
    private float radius;
    private final String requestId;
    private int transitionTypes;

    public ParcelableGeofence(String requestId2, double latitude2, double longitude2, float radius2, long durationMillis2, int transitionTypes2, int loiteringDelayMs2) {
        this.requestId = requestId2;
        this.latitude = latitude2;
        this.longitude = longitude2;
        this.radius = radius2;
        if (durationMillis2 < 0) {
            this.durationMillis = -1;
        } else {
            this.durationMillis = durationMillis2;
        }
        this.transitionTypes = transitionTypes2;
        this.loiteringDelayMs = loiteringDelayMs2;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public float getRadius() {
        return this.radius;
    }

    public long getDuration() {
        return this.durationMillis;
    }

    public int getTransitionTypes() {
        return this.transitionTypes;
    }

    public int getLoiteringDelayMs() {
        return this.loiteringDelayMs;
    }

    protected ParcelableGeofence(Parcel in2) {
        this.requestId = in2.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.requestId);
    }

    public int describeContents() {
        return 0;
    }
}
