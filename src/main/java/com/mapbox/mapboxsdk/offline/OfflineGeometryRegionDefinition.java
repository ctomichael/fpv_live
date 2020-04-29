package com.mapbox.mapboxsdk.offline;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.turf.TurfMeasurement;

public class OfflineGeometryRegionDefinition implements OfflineRegionDefinition {
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        /* class com.mapbox.mapboxsdk.offline.OfflineGeometryRegionDefinition.AnonymousClass1 */

        public OfflineGeometryRegionDefinition createFromParcel(@NonNull Parcel in2) {
            return new OfflineGeometryRegionDefinition(in2);
        }

        public OfflineGeometryRegionDefinition[] newArray(int size) {
            return new OfflineGeometryRegionDefinition[size];
        }
    };
    @Keep
    @Nullable
    private Geometry geometry;
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
    public OfflineGeometryRegionDefinition(String styleURL2, Geometry geometry2, double minZoom2, double maxZoom2, float pixelRatio2) {
        this(styleURL2, geometry2, minZoom2, maxZoom2, pixelRatio2, false);
    }

    @Keep
    public OfflineGeometryRegionDefinition(String styleURL2, Geometry geometry2, double minZoom2, double maxZoom2, float pixelRatio2, boolean includeIdeographs2) {
        this.styleURL = styleURL2;
        this.geometry = geometry2;
        this.minZoom = minZoom2;
        this.maxZoom = maxZoom2;
        this.pixelRatio = pixelRatio2;
        this.includeIdeographs = includeIdeographs2;
    }

    public OfflineGeometryRegionDefinition(Parcel parcel) {
        this.styleURL = parcel.readString();
        this.geometry = Feature.fromJson(parcel.readString()).geometry();
        this.minZoom = parcel.readDouble();
        this.maxZoom = parcel.readDouble();
        this.pixelRatio = parcel.readFloat();
        this.includeIdeographs = parcel.readByte() != 0;
    }

    public String getStyleURL() {
        return this.styleURL;
    }

    @Nullable
    public Geometry getGeometry() {
        return this.geometry;
    }

    @Nullable
    public LatLngBounds getBounds() {
        if (this.geometry == null) {
            return null;
        }
        double[] bbox = TurfMeasurement.bbox(this.geometry);
        return LatLngBounds.from(bbox[3], bbox[2], bbox[1], bbox[0]);
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
        return "shaperegion";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.styleURL);
        dest.writeString(Feature.fromGeometry(this.geometry).toJson());
        dest.writeDouble(this.minZoom);
        dest.writeDouble(this.maxZoom);
        dest.writeFloat(this.pixelRatio);
        dest.writeByte((byte) (this.includeIdeographs ? 1 : 0));
    }
}
