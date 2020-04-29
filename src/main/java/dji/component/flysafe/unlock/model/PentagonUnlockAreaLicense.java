package dji.component.flysafe.unlock.model;

import android.support.annotation.Keep;
import com.dji.mapkit.core.models.DJILatLng;
import dji.fieldAnnotation.EXClassNullAway;
import dji.flysafe.v3.GPSPoint;
import dji.flysafe.v3.License;
import dji.flysafe.v3.LicenseDataPolygon;
import dji.midware.data.model.P3.DataWhiteListRequestLicense;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class PentagonUnlockAreaLicense extends WhiteListLicense {
    private List<DJILatLng> points = new ArrayList();

    public List<DJILatLng> getPoints() {
        return this.points;
    }

    public void setPoints(List<DJILatLng> points2) {
        this.points = new ArrayList(points2);
    }

    public PentagonUnlockAreaLicense(DataWhiteListRequestLicense licenseData, int index) {
        super(licenseData, index);
        float[][] rawPoints = licenseData.getPentagonUnlockAreaPoints();
        for (int i = 0; i < rawPoints.length; i++) {
            this.points.add(new DJILatLng((double) rawPoints[i][0], (double) rawPoints[i][1]));
        }
    }

    public PentagonUnlockAreaLicense(License protobufLicense) {
        super(protobufLicense);
        LicenseDataPolygon prtbPolygon = protobufLicense.data.polygon;
        if (prtbPolygon != null && prtbPolygon.points != null) {
            for (GPSPoint gpsPoint : prtbPolygon.points) {
                this.points.add(new DJILatLng((double) gpsPoint.lat.floatValue(), (double) gpsPoint.lng.floatValue()));
            }
        }
    }

    public PentagonUnlockAreaLicense() {
    }
}
