package com.mapbox.android.telemetry;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import com.mapbox.android.telemetry.Event;

public class VisionObjectDetectionEvent extends Event implements Parcelable {
    public static final Parcelable.Creator<VisionObjectDetectionEvent> CREATOR = new Parcelable.Creator<VisionObjectDetectionEvent>() {
        /* class com.mapbox.android.telemetry.VisionObjectDetectionEvent.AnonymousClass1 */

        public VisionObjectDetectionEvent createFromParcel(Parcel in2) {
            return new VisionObjectDetectionEvent(in2);
        }

        public VisionObjectDetectionEvent[] newArray(int size) {
            return new VisionObjectDetectionEvent[size];
        }
    };
    static final String VIS_OBJECT_DETECTION = "vision.objectDetection";
    @SerializedName("class")
    private String clazz;
    @SerializedName("created")
    private final String created;
    @SerializedName("distance_from_camera")
    private Double distance_from_camera;
    @SerializedName("event")
    private final String event;
    @SerializedName("object_lat")
    private Double object_lat;
    @SerializedName("object_lon")
    private Double object_lon;
    @SerializedName("object_pos_height")
    private Double object_pos_height;
    @SerializedName("object_size_height")
    private Double object_size_height;
    @SerializedName("object_size_width")
    private Double object_size_width;
    @SerializedName("sign_value")
    private String sign_value;
    @SerializedName("vehicle_lat")
    private Double vehicle_lat;
    @SerializedName("vehicle_lon")
    private Double vehicle_lon;

    public VisionObjectDetectionEvent(String created2) {
        this.event = VIS_OBJECT_DETECTION;
        this.created = created2;
        this.object_lat = null;
        this.object_lon = null;
        this.vehicle_lat = null;
        this.vehicle_lon = null;
        this.clazz = null;
        this.sign_value = null;
        this.object_size_height = null;
        this.object_size_width = null;
        this.object_pos_height = null;
        this.distance_from_camera = null;
    }

    public String getEvent() {
        return this.event;
    }

    public String getCreated() {
        return this.created;
    }

    public double getObjectLatitude() {
        return this.object_lat.doubleValue();
    }

    public void setObjectLatitude(double latitude) {
        this.object_lat = Double.valueOf(latitude);
    }

    public double getObjectLongitude() {
        return this.object_lon.doubleValue();
    }

    public void setObjectLongitude(double longitude) {
        this.object_lon = Double.valueOf(longitude);
    }

    public double getVehicleLatitude() {
        return this.vehicle_lat.doubleValue();
    }

    public void setVehicleLatitude(double latitude) {
        this.vehicle_lat = Double.valueOf(latitude);
    }

    public double getVehicleLongitude() {
        return this.vehicle_lon.doubleValue();
    }

    public void setVehicleLongitude(double longitude) {
        this.vehicle_lon = Double.valueOf(longitude);
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String clazz2) {
        this.clazz = clazz2;
    }

    public String getSignValue() {
        return this.sign_value;
    }

    public void setSignValue(String sign_value2) {
        this.sign_value = sign_value2;
    }

    public double getObjectSizeWidth() {
        return this.object_size_width.doubleValue();
    }

    public void setObjectSizeWidth(double object_size_width2) {
        this.object_size_width = Double.valueOf(object_size_width2);
    }

    public double getObjectSizeHeight() {
        return this.object_size_height.doubleValue();
    }

    public void setObjectSizeHeight(double object_size_height2) {
        this.object_size_height = Double.valueOf(object_size_height2);
    }

    public double getObjectPositionHeight() {
        return this.object_pos_height.doubleValue();
    }

    public void setObjectPositionHeight(double object_pos_height2) {
        this.object_pos_height = Double.valueOf(object_pos_height2);
    }

    public double getDistanceFromCamera() {
        return this.distance_from_camera.doubleValue();
    }

    public void setDistanceFromCamera(double distance_from_camera2) {
        this.distance_from_camera = Double.valueOf(distance_from_camera2);
    }

    /* access modifiers changed from: package-private */
    public Event.Type obtainType() {
        return Event.Type.VIS_OBJ_DETECTION;
    }

    private VisionObjectDetectionEvent(Parcel in2) {
        this.event = in2.readString();
        this.created = in2.readString();
        this.object_lat = readDoubleIfNotNull(in2);
        this.object_lon = readDoubleIfNotNull(in2);
        this.vehicle_lat = readDoubleIfNotNull(in2);
        this.vehicle_lon = readDoubleIfNotNull(in2);
        this.clazz = readStringIfNotNull(in2);
        this.sign_value = readStringIfNotNull(in2);
        this.object_size_width = readDoubleIfNotNull(in2);
        this.object_size_height = readDoubleIfNotNull(in2);
        this.object_pos_height = readDoubleIfNotNull(in2);
        this.distance_from_camera = readDoubleIfNotNull(in2);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.event);
        dest.writeString(this.created);
        writeDoubleIfNotNull(dest, this.object_lat);
        writeDoubleIfNotNull(dest, this.object_lon);
        writeDoubleIfNotNull(dest, this.vehicle_lat);
        writeDoubleIfNotNull(dest, this.vehicle_lon);
        writeStringIfNotNull(dest, this.clazz);
        writeStringIfNotNull(dest, this.sign_value);
        writeDoubleIfNotNull(dest, this.object_size_width);
        writeDoubleIfNotNull(dest, this.object_size_height);
        writeDoubleIfNotNull(dest, this.object_pos_height);
        writeDoubleIfNotNull(dest, this.distance_from_camera);
    }

    private static void writeDoubleIfNotNull(Parcel parcel, Double value) {
        parcel.writeByte((byte) (value != null ? 1 : 0));
        if (value != null) {
            parcel.writeDouble(value.doubleValue());
        }
    }

    private static void writeStringIfNotNull(Parcel parcel, String value) {
        parcel.writeByte((byte) (value != null ? 1 : 0));
        if (value != null) {
            parcel.writeString(value);
        }
    }

    private static Double readDoubleIfNotNull(Parcel parcel) {
        if (parcel.readByte() == 0) {
            return null;
        }
        return Double.valueOf(parcel.readDouble());
    }

    private static String readStringIfNotNull(Parcel parcel) {
        if (parcel.readByte() == 0) {
            return null;
        }
        return parcel.readString();
    }
}
