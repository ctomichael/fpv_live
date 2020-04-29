package com.mapbox.mapboxsdk.geometry;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.FloatRange;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.mapboxsdk.exceptions.InvalidLatLngBoundsException;
import java.util.ArrayList;
import java.util.List;

public class LatLngBounds implements Parcelable {
    public static final Parcelable.Creator<LatLngBounds> CREATOR = new Parcelable.Creator<LatLngBounds>() {
        /* class com.mapbox.mapboxsdk.geometry.LatLngBounds.AnonymousClass1 */

        public LatLngBounds createFromParcel(@NonNull Parcel in2) {
            return LatLngBounds.readFromParcel(in2);
        }

        public LatLngBounds[] newArray(int size) {
            return new LatLngBounds[size];
        }
    };
    @Keep
    private final double latitudeNorth;
    @Keep
    private final double latitudeSouth;
    @Keep
    private final double longitudeEast;
    @Keep
    private final double longitudeWest;

    @Keep
    LatLngBounds(double northLatitude, double eastLongitude, double southLatitude, double westLongitude) {
        this.latitudeNorth = northLatitude;
        this.longitudeEast = eastLongitude;
        this.latitudeSouth = southLatitude;
        this.longitudeWest = westLongitude;
    }

    public static LatLngBounds world() {
        return from(90.0d, 180.0d, -90.0d, -180.0d);
    }

    @NonNull
    public LatLng getCenter() {
        return new LatLng((this.latitudeNorth + this.latitudeSouth) / 2.0d, (this.longitudeEast + this.longitudeWest) / 2.0d);
    }

    public double getLatNorth() {
        return this.latitudeNorth;
    }

    public double getLatSouth() {
        return this.latitudeSouth;
    }

    public double getLonEast() {
        return this.longitudeEast;
    }

    public double getLonWest() {
        return this.longitudeWest;
    }

    @NonNull
    public LatLng getSouthWest() {
        return new LatLng(this.latitudeSouth, this.longitudeWest);
    }

    @NonNull
    public LatLng getNorthEast() {
        return new LatLng(this.latitudeNorth, this.longitudeEast);
    }

    @NonNull
    public LatLng getSouthEast() {
        return new LatLng(this.latitudeSouth, this.longitudeEast);
    }

    @NonNull
    public LatLng getNorthWest() {
        return new LatLng(this.latitudeNorth, this.longitudeWest);
    }

    @NonNull
    public LatLngSpan getSpan() {
        return new LatLngSpan(getLatitudeSpan(), getLongitudeSpan());
    }

    public double getLatitudeSpan() {
        return Math.abs(this.latitudeNorth - this.latitudeSouth);
    }

    public double getLongitudeSpan() {
        return Math.abs(this.longitudeEast - this.longitudeWest);
    }

    public boolean isEmptySpan() {
        return getLongitudeSpan() == 0.0d || getLatitudeSpan() == 0.0d;
    }

    @NonNull
    public String toString() {
        return "N:" + this.latitudeNorth + "; E:" + this.longitudeEast + "; S:" + this.latitudeSouth + "; W:" + this.longitudeWest;
    }

    static LatLngBounds fromLatLngs(List<? extends LatLng> latLngs) {
        double minLat = 90.0d;
        double minLon = Double.MAX_VALUE;
        double maxLat = -90.0d;
        double maxLon = -1.7976931348623157E308d;
        for (LatLng gp : latLngs) {
            double latitude = gp.getLatitude();
            double longitude = gp.getLongitude();
            minLat = Math.min(minLat, latitude);
            minLon = Math.min(minLon, longitude);
            maxLat = Math.max(maxLat, latitude);
            maxLon = Math.max(maxLon, longitude);
        }
        return new LatLngBounds(maxLat, maxLon, minLat, minLon);
    }

    @NonNull
    public LatLng[] toLatLngs() {
        return new LatLng[]{getNorthEast(), getSouthWest()};
    }

    public static LatLngBounds from(@FloatRange(from = -90.0d, to = 90.0d) double latNorth, double lonEast, @FloatRange(from = -90.0d, to = 90.0d) double latSouth, double lonWest) {
        checkParams(latNorth, lonEast, latSouth, lonWest);
        return new LatLngBounds(latNorth, lonEast, latSouth, lonWest);
    }

    private static void checkParams(@FloatRange(from = -90.0d, to = 90.0d) double latNorth, double lonEast, @FloatRange(from = -90.0d, to = 90.0d) double latSouth, double lonWest) {
        if (Double.isNaN(latNorth) || Double.isNaN(latSouth)) {
            throw new IllegalArgumentException("latitude must not be NaN");
        } else if (Double.isNaN(lonEast) || Double.isNaN(lonWest)) {
            throw new IllegalArgumentException("longitude must not be NaN");
        } else if (Double.isInfinite(lonEast) || Double.isInfinite(lonWest)) {
            throw new IllegalArgumentException("longitude must not be infinite");
        } else if (latNorth > 90.0d || latNorth < -90.0d || latSouth > 90.0d || latSouth < -90.0d) {
            throw new IllegalArgumentException("latitude must be between -90 and 90");
        } else if (latNorth < latSouth) {
            throw new IllegalArgumentException("latNorth cannot be less than latSouth");
        } else if (lonEast < lonWest) {
            throw new IllegalArgumentException("lonEast cannot be less than lonWest");
        }
    }

    private static double lat_(int z, int y) {
        double n = 3.141592653589793d - ((6.283185307179586d * ((double) y)) / Math.pow(2.0d, (double) z));
        return Math.toDegrees(Math.atan(0.5d * (Math.exp(n) - Math.exp(-n))));
    }

    private static double lon_(int z, int x) {
        return ((((double) x) / Math.pow(2.0d, (double) z)) * 360.0d) - 180.0d;
    }

    public static LatLngBounds from(int z, int x, int y) {
        return new LatLngBounds(lat_(z, y), lon_(z, x + 1), lat_(z, y + 1), lon_(z, x));
    }

    @NonNull
    public LatLngBounds include(@NonNull LatLng latLng) {
        return new Builder().include(getNorthEast()).include(getSouthWest()).include(latLng).build();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LatLngBounds)) {
            return false;
        }
        LatLngBounds other = (LatLngBounds) o;
        if (this.latitudeNorth == other.getLatNorth() && this.latitudeSouth == other.getLatSouth() && this.longitudeEast == other.getLonEast() && this.longitudeWest == other.getLonWest()) {
            return true;
        }
        return false;
    }

    private boolean containsLatitude(double latitude) {
        return latitude <= this.latitudeNorth && latitude >= this.latitudeSouth;
    }

    private boolean containsLongitude(double longitude) {
        return longitude <= this.longitudeEast && longitude >= this.longitudeWest;
    }

    public boolean contains(@NonNull LatLng latLng) {
        return containsLatitude(latLng.getLatitude()) && containsLongitude(latLng.getLongitude());
    }

    public boolean contains(@NonNull LatLngBounds other) {
        return contains(other.getNorthEast()) && contains(other.getSouthWest());
    }

    @NonNull
    public LatLngBounds union(@NonNull LatLngBounds bounds) {
        return unionNoParamCheck(bounds.getLatNorth(), bounds.getLonEast(), bounds.getLatSouth(), bounds.getLonWest());
    }

    @NonNull
    public LatLngBounds union(double northLat, double eastLon, double southLat, double westLon) {
        checkParams(northLat, eastLon, southLat, westLon);
        return unionNoParamCheck(northLat, eastLon, southLat, westLon);
    }

    private LatLngBounds unionNoParamCheck(double northLat, double eastLon, double southLat, double westLon) {
        return new LatLngBounds(this.latitudeNorth < northLat ? northLat : this.latitudeNorth, this.longitudeEast < eastLon ? eastLon : this.longitudeEast, this.latitudeSouth > southLat ? southLat : this.latitudeSouth, this.longitudeWest > westLon ? westLon : this.longitudeWest);
    }

    @Nullable
    public LatLngBounds intersect(@NonNull LatLngBounds box) {
        return intersectNoParamCheck(box.getLatNorth(), box.getLonEast(), box.getLatSouth(), box.getLonWest());
    }

    @NonNull
    public LatLngBounds intersect(double northLat, double eastLon, double southLat, double westLon) {
        checkParams(northLat, eastLon, southLat, westLon);
        return intersectNoParamCheck(northLat, eastLon, southLat, westLon);
    }

    private LatLngBounds intersectNoParamCheck(double northLat, double eastLon, double southLat, double westLon) {
        double minLonWest = Math.max(this.longitudeWest, westLon);
        double maxLonEast = Math.min(this.longitudeEast, eastLon);
        if (maxLonEast >= minLonWest) {
            double minLatSouth = Math.max(this.latitudeSouth, southLat);
            double maxLatNorth = Math.min(this.latitudeNorth, northLat);
            if (maxLatNorth >= minLatSouth) {
                return new LatLngBounds(maxLatNorth, maxLonEast, minLatSouth, minLonWest);
            }
        }
        return null;
    }

    public int hashCode() {
        return (int) (this.latitudeNorth + 90.0d + ((this.latitudeSouth + 90.0d) * 1000.0d) + ((this.longitudeEast + 180.0d) * 1000000.0d) + ((this.longitudeWest + 180.0d) * 1.0E9d));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(@NonNull Parcel out, int flags) {
        out.writeDouble(this.latitudeNorth);
        out.writeDouble(this.longitudeEast);
        out.writeDouble(this.latitudeSouth);
        out.writeDouble(this.longitudeWest);
    }

    /* access modifiers changed from: private */
    public static LatLngBounds readFromParcel(Parcel in2) {
        return new LatLngBounds(in2.readDouble(), in2.readDouble(), in2.readDouble(), in2.readDouble());
    }

    public static final class Builder {
        private final List<LatLng> latLngList = new ArrayList();

        public LatLngBounds build() {
            if (this.latLngList.size() >= 2) {
                return LatLngBounds.fromLatLngs(this.latLngList);
            }
            throw new InvalidLatLngBoundsException(this.latLngList.size());
        }

        @NonNull
        public Builder includes(@NonNull List<LatLng> latLngs) {
            this.latLngList.addAll(latLngs);
            return this;
        }

        @NonNull
        public Builder include(@NonNull LatLng latLng) {
            this.latLngList.add(latLng);
            return this;
        }
    }
}
