package com.mapbox.mapboxsdk.offline;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;

public class OfflineTilePyramidRegionDefinition implements OfflineRegionDefinition {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        /* class com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition.AnonymousClass1 */

        public OfflineTilePyramidRegionDefinition createFromParcel(@NonNull Parcel in2) {
            return new OfflineTilePyramidRegionDefinition(in2);
        }

        public OfflineTilePyramidRegionDefinition[] newArray(int size) {
            return new OfflineTilePyramidRegionDefinition[size];
        }
    };
    @Keep
    private LatLngBounds bounds;
    @Keep
    private boolean includeIdeographs;
    @Keep
    private double maxZoom;
    @Keep
    private double minZoom;
    @Keep
    private float pixelRatio;
    @Keep
    private String styleURL;

    @Keep
    public OfflineTilePyramidRegionDefinition(String styleURL2, LatLngBounds bounds2, double minZoom2, double maxZoom2, float pixelRatio2) {
        this(styleURL2, bounds2, minZoom2, maxZoom2, pixelRatio2, false);
    }

    @Keep
    public OfflineTilePyramidRegionDefinition(String styleURL2, LatLngBounds bounds2, double minZoom2, double maxZoom2, float pixelRatio2, boolean includeIdeographs2) {
        this.styleURL = styleURL2;
        this.bounds = bounds2;
        this.minZoom = minZoom2;
        this.maxZoom = maxZoom2;
        this.pixelRatio = pixelRatio2;
        this.includeIdeographs = includeIdeographs2;
    }

    public OfflineTilePyramidRegionDefinition(Parcel parcel) {
        this.styleURL = parcel.readString();
        this.bounds = new LatLngBounds.Builder().include(new LatLng(parcel.readDouble(), parcel.readDouble())).include(new LatLng(parcel.readDouble(), parcel.readDouble())).build();
        this.minZoom = parcel.readDouble();
        this.maxZoom = parcel.readDouble();
        this.pixelRatio = parcel.readFloat();
        this.includeIdeographs = parcel.readByte() != 0;
    }

    public String getStyleURL() {
        return this.styleURL;
    }

    public LatLngBounds getBounds() {
        return this.bounds;
    }

    public double getMinZoom() {
        return this.minZoom;
    }

    public double getMaxZoom() {
        return this.maxZoom;
    }

    public float getPixelRatio() {
        return this.pixelRatio;
    }

    public boolean getIncludeIdeographs() {
        return this.includeIdeographs;
    }

    @NonNull
    public String getType() {
        return "tileregion";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.styleURL);
        dest.writeDouble(this.bounds.getLatNorth());
        dest.writeDouble(this.bounds.getLonEast());
        dest.writeDouble(this.bounds.getLatSouth());
        dest.writeDouble(this.bounds.getLonWest());
        dest.writeDouble(this.minZoom);
        dest.writeDouble(this.maxZoom);
        dest.writeFloat(this.pixelRatio);
        dest.writeByte((byte) (this.includeIdeographs ? 1 : 0));
    }
}
