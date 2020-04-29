package com.mapzen.android.lost.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import com.mapzen.android.lost.internal.PidReader;

public final class LocationRequest implements Parcelable {
    public static final Parcelable.Creator<LocationRequest> CREATOR = new Parcelable.Creator<LocationRequest>() {
        /* class com.mapzen.android.lost.api.LocationRequest.AnonymousClass2 */

        public LocationRequest createFromParcel(Parcel source) {
            return new LocationRequest(source);
        }

        public LocationRequest[] newArray(int size) {
            return new LocationRequest[size];
        }
    };
    static final long DEFAULT_FASTEST_INTERVAL_IN_MS = 600000;
    static final long DEFAULT_INTERVAL_IN_MS = 3600000;
    static final float DEFAULT_SMALLEST_DISPLACEMENT_IN_METERS = 0.0f;
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    public static final int PRIORITY_HIGH_ACCURACY = 100;
    public static final int PRIORITY_LOW_POWER = 104;
    public static final int PRIORITY_NO_POWER = 105;
    private long fastestInterval = DEFAULT_FASTEST_INTERVAL_IN_MS;
    private long interval = DEFAULT_INTERVAL_IN_MS;
    long pid;
    private PidReader pidReader = new PidReader() {
        /* class com.mapzen.android.lost.api.LocationRequest.AnonymousClass1 */

        public long getPid() {
            return (long) Process.myPid();
        }
    };
    private int priority = 102;
    private float smallestDisplacement = 0.0f;

    private LocationRequest() {
        commonInit();
    }

    private LocationRequest(PidReader reader) {
        this.pidReader = reader;
        commonInit();
    }

    private void commonInit() {
        this.pid = this.pidReader.getPid();
    }

    public static LocationRequest create() {
        return new LocationRequest();
    }

    public static LocationRequest create(PidReader reader) {
        return new LocationRequest(reader);
    }

    public LocationRequest(LocationRequest incoming) {
        setInterval(incoming.getInterval());
        setFastestInterval(incoming.getFastestInterval());
        setSmallestDisplacement(incoming.getSmallestDisplacement());
        setPriority(incoming.getPriority());
        this.pid = incoming.pid;
    }

    public long getInterval() {
        return this.interval;
    }

    public LocationRequest setInterval(long millis) {
        this.interval = millis;
        if (this.interval < this.fastestInterval) {
            this.fastestInterval = this.interval;
        }
        return this;
    }

    public long getFastestInterval() {
        return this.fastestInterval;
    }

    public LocationRequest setFastestInterval(long millis) {
        this.fastestInterval = millis;
        return this;
    }

    public float getSmallestDisplacement() {
        return this.smallestDisplacement;
    }

    public LocationRequest setSmallestDisplacement(float meters) {
        this.smallestDisplacement = meters;
        return this;
    }

    public int getPriority() {
        return this.priority;
    }

    public LocationRequest setPriority(int priority2) {
        if (priority2 == 100 || priority2 == 102 || priority2 == 104 || priority2 == 105) {
            this.priority = priority2;
            return this;
        }
        throw new IllegalArgumentException("Invalid priority: " + priority2);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationRequest that = (LocationRequest) o;
        if (this.pid != that.pid || this.interval != that.interval || this.fastestInterval != that.fastestInterval || Float.compare(that.smallestDisplacement, this.smallestDisplacement) != 0) {
            return false;
        }
        if (this.priority != that.priority) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((((((int) (this.interval ^ (this.interval >>> 32))) * 31) + ((int) (this.fastestInterval ^ (this.fastestInterval >>> 32)))) * 31) + (this.smallestDisplacement != 0.0f ? Float.floatToIntBits(this.smallestDisplacement) : 0)) * 31) + this.priority) * 31) + ((int) this.pid);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.interval);
        dest.writeLong(this.fastestInterval);
        dest.writeFloat(this.smallestDisplacement);
        dest.writeInt(this.priority);
        dest.writeLong(this.pid);
    }

    protected LocationRequest(Parcel in2) {
        this.interval = in2.readLong();
        this.fastestInterval = in2.readLong();
        this.smallestDisplacement = in2.readFloat();
        this.priority = in2.readInt();
        this.pid = in2.readLong();
    }
}
