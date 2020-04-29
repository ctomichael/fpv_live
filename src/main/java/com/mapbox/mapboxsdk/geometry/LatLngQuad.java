package com.mapbox.mapboxsdk.geometry;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;

public class LatLngQuad implements Parcelable {
    public static final Parcelable.Creator<LatLngQuad> CREATOR = new Parcelable.Creator<LatLngQuad>() {
        /* class com.mapbox.mapboxsdk.geometry.LatLngQuad.AnonymousClass1 */

        public LatLngQuad createFromParcel(@NonNull Parcel in2) {
            return LatLngQuad.readFromParcel(in2);
        }

        public LatLngQuad[] newArray(int size) {
            return new LatLngQuad[size];
        }
    };
    @Keep
    private final LatLng bottomLeft;
    @Keep
    private final LatLng bottomRight;
    @Keep
    private final LatLng topLeft;
    @Keep
    private final LatLng topRight;

    @Keep
    public LatLngQuad(LatLng topLeft2, LatLng topRight2, LatLng bottomRight2, LatLng bottomLeft2) {
        this.topLeft = topLeft2;
        this.topRight = topRight2;
        this.bottomRight = bottomRight2;
        this.bottomLeft = bottomLeft2;
    }

    public LatLng getTopLeft() {
        return this.topLeft;
    }

    public LatLng getTopRight() {
        return this.topRight;
    }

    public LatLng getBottomRight() {
        return this.bottomRight;
    }

    public LatLng getBottomLeft() {
        return this.bottomLeft;
    }

    public int hashCode() {
        int code = this.topLeft.hashCode();
        int code2 = ((code >>> 31) ^ code) + this.topRight.hashCode();
        int code3 = ((code2 >>> 31) ^ code2) + this.bottomRight.hashCode();
        return ((code3 >>> 31) ^ code3) + this.bottomLeft.hashCode();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(@NonNull Parcel out, int arg1) {
        this.topLeft.writeToParcel(out, arg1);
        this.topRight.writeToParcel(out, arg1);
        this.bottomRight.writeToParcel(out, arg1);
        this.bottomLeft.writeToParcel(out, arg1);
    }

    /* access modifiers changed from: private */
    public static LatLngQuad readFromParcel(@NonNull Parcel in2) {
        return new LatLngQuad(new LatLng(in2), new LatLng(in2), new LatLng(in2), new LatLng(in2));
    }
}
