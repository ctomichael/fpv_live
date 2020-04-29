package com.mapbox.mapboxsdk.camera;

import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.FloatRange;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import com.mapbox.mapboxsdk.R;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.utils.MathUtils;
import java.util.Arrays;

public final class CameraPosition implements Parcelable {
    public static final Parcelable.Creator<CameraPosition> CREATOR = new Parcelable.Creator<CameraPosition>() {
        /* class com.mapbox.mapboxsdk.camera.CameraPosition.AnonymousClass1 */

        public CameraPosition createFromParcel(Parcel in2) {
            double bearing = in2.readDouble();
            double tilt = in2.readDouble();
            double zoom = in2.readDouble();
            double[] padding = new double[4];
            in2.readDoubleArray(padding);
            return new CameraPosition((LatLng) in2.readParcelable(LatLng.class.getClassLoader()), zoom, tilt, bearing, padding);
        }

        public CameraPosition[] newArray(int size) {
            return new CameraPosition[size];
        }
    };
    public static final CameraPosition DEFAULT = new CameraPosition(new LatLng(), 0.0d, 0.0d, 0.0d, new double[]{0.0d, 0.0d, 0.0d, 0.0d});
    @Keep
    public final double bearing;
    @Keep
    public final double[] padding;
    @Keep
    public final LatLng target;
    @Keep
    public final double tilt;
    @Keep
    public final double zoom;

    @Deprecated
    CameraPosition(LatLng target2, double zoom2, double tilt2, double bearing2) {
        this(target2, zoom2, tilt2, bearing2, null);
    }

    @Keep
    CameraPosition(LatLng target2, double zoom2, double tilt2, double bearing2, double[] padding2) {
        this.target = target2;
        this.bearing = bearing2;
        this.tilt = tilt2;
        this.zoom = zoom2;
        this.padding = padding2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(this.bearing);
        out.writeParcelable(this.target, flags);
        out.writeDouble(this.tilt);
        out.writeDouble(this.zoom);
        out.writeDoubleArray(this.padding);
    }

    public String toString() {
        return "Target: " + this.target + ", Zoom:" + this.zoom + ", Bearing:" + this.bearing + ", Tilt:" + this.tilt + ", Padding:" + Arrays.toString(this.padding);
    }

    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CameraPosition cameraPosition = (CameraPosition) o;
        if (this.target != null && !this.target.equals(cameraPosition.target)) {
            return false;
        }
        if (this.zoom != cameraPosition.zoom) {
            return false;
        }
        if (this.tilt != cameraPosition.tilt) {
            return false;
        }
        if (this.bearing != cameraPosition.bearing) {
            return false;
        }
        if (!Arrays.equals(this.padding, cameraPosition.padding)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.bearing);
        int result = (((int) ((temp >>> 32) ^ temp)) * 31) + (this.target != null ? this.target.hashCode() : 0);
        long temp2 = Double.doubleToLongBits(this.tilt);
        int result2 = (result * 31) + ((int) ((temp2 >>> 32) ^ temp2));
        long temp3 = Double.doubleToLongBits(this.zoom);
        return (((result2 * 31) + ((int) ((temp3 >>> 32) ^ temp3))) * 31) + Arrays.hashCode(this.padding);
    }

    public static final class Builder {
        private double bearing = -1.0d;
        private double[] padding = null;
        @Nullable
        private LatLng target = null;
        private double tilt = -1.0d;
        private double zoom = -1.0d;

        public Builder() {
        }

        public Builder(@Nullable CameraPosition previous) {
            if (previous != null) {
                this.bearing = previous.bearing;
                this.target = previous.target;
                this.tilt = previous.tilt;
                this.zoom = previous.zoom;
                this.padding = previous.padding;
            }
        }

        public Builder(@Nullable TypedArray typedArray) {
            if (typedArray != null) {
                this.bearing = (double) typedArray.getFloat(R.styleable.mapbox_MapView_mapbox_cameraBearing, 0.0f);
                this.target = new LatLng((double) typedArray.getFloat(R.styleable.mapbox_MapView_mapbox_cameraTargetLat, 0.0f), (double) typedArray.getFloat(R.styleable.mapbox_MapView_mapbox_cameraTargetLng, 0.0f));
                this.tilt = (double) typedArray.getFloat(R.styleable.mapbox_MapView_mapbox_cameraTilt, 0.0f);
                this.zoom = (double) typedArray.getFloat(R.styleable.mapbox_MapView_mapbox_cameraZoom, 0.0f);
            }
        }

        public Builder(@Nullable CameraUpdateFactory.CameraPositionUpdate update) {
            if (update != null) {
                this.bearing = update.getBearing();
                this.target = update.getTarget();
                this.tilt = update.getTilt();
                this.zoom = update.getZoom();
                this.padding = update.getPadding();
            }
        }

        public Builder(@Nullable CameraUpdateFactory.ZoomUpdate update) {
            if (update != null) {
                this.zoom = update.getZoom();
            }
        }

        @NonNull
        public Builder bearing(double bearing2) {
            double direction = bearing2;
            while (direction >= 360.0d) {
                direction -= 360.0d;
            }
            while (direction < 0.0d) {
                direction += 360.0d;
            }
            this.bearing = direction;
            return this;
        }

        @NonNull
        public Builder target(LatLng location) {
            this.target = location;
            return this;
        }

        @NonNull
        public Builder tilt(@FloatRange(from = 0.0d, to = 60.0d) double tilt2) {
            this.tilt = MathUtils.clamp(tilt2, 0.0d, 60.0d);
            return this;
        }

        @NonNull
        public Builder zoom(@FloatRange(from = 0.0d, to = 25.5d) double zoom2) {
            this.zoom = zoom2;
            return this;
        }

        @NonNull
        public Builder padding(@Size(4) double[] padding2) {
            this.padding = padding2;
            return this;
        }

        @NonNull
        public Builder padding(double left, double top, double right, double bottom) {
            this.padding = new double[]{left, top, right, bottom};
            return this;
        }

        public CameraPosition build() {
            return new CameraPosition(this.target, this.zoom, this.tilt, this.bearing, this.padding);
        }
    }
}
