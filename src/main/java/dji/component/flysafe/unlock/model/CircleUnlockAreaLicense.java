package dji.component.flysafe.unlock.model;

import android.support.annotation.Keep;
import dji.component.flysafe.util.ProtobufHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.flysafe.v3.License;
import dji.flysafe.v3.LicenseDataCircle;
import dji.midware.data.model.P3.DataWhiteListRequestLicense;

@Keep
@EXClassNullAway
public class CircleUnlockAreaLicense extends WhiteListLicense {
    private float latitude;
    private float limitedHeight;
    private float longitude;
    private float radius;

    public CircleUnlockAreaLicense() {
    }

    public CircleUnlockAreaLicense(DataWhiteListRequestLicense licenseData, int index) {
        super(licenseData, index);
        this.latitude = licenseData.getCircleUnlockAreaLatitude();
        this.longitude = licenseData.getCircleUnlockAreaLongitude();
        this.radius = (float) licenseData.getCircleUnlockAreaRadius();
        this.limitedHeight = (float) licenseData.getCircleUnlockAreaLimitedHeight();
    }

    public CircleUnlockAreaLicense(License protobufLicense) {
        super(protobufLicense);
        LicenseDataCircle prtbCircle = protobufLicense.data.circle;
        if (prtbCircle != null) {
            this.limitedHeight = (float) ProtobufHelper.toInt(prtbCircle.height_limit);
            if (prtbCircle.point != null) {
                this.latitude = ProtobufHelper.toFloat(prtbCircle.point.lat);
                this.longitude = ProtobufHelper.toFloat(prtbCircle.point.lng);
            }
            this.radius = (float) ProtobufHelper.toInt(prtbCircle.radius);
        }
    }

    public float getLatitude() {
        return this.latitude;
    }

    public void setLatitude(float latitude2) {
        this.latitude = latitude2;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public void setLongitude(float longitude2) {
        this.longitude = longitude2;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius2) {
        this.radius = radius2;
    }

    public float getLimitedHeight() {
        return this.limitedHeight;
    }

    public void setLimitedHeight(float limitedHeight2) {
        this.limitedHeight = limitedHeight2;
    }
}
