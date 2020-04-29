package dji.common.flightcontroller;

import android.support.annotation.NonNull;
import dji.common.error.DJIError;
import dji.common.flightcontroller.rtk.DataSource;
import dji.common.flightcontroller.rtk.LocationStandardDeviation;
import dji.common.model.LocationCoordinate2D;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class RTKState {
    private final float bsAltitude;
    private final LocationCoordinate2D bsLocation;
    private final ReceiverInfo bsReceiverBeiDouInfo;
    private final ReceiverInfo bsReceiverGLONASSInfo;
    private final ReceiverInfo bsReceiverGPSInfo;
    private final ReceiverInfo bsReceiverGalileoInfo;
    private final float distanceToHomePoint;
    private final DataSource distanceToHomePointDataSource;
    private final DJIError error;
    private final float fusionHeading;
    private final float heading;
    private final DataSource homePointDataSource;
    private final LocationCoordinate2D homePointLocation;
    private final boolean isHeadingValid;
    private final boolean isRTKBeingUsed;
    private final boolean isTakeoffAltitudeRecorded;
    private final int locating;
    private final LocationStandardDeviation mobileStationStandardDeviation;
    private final float msAntenna1Altitude;
    private final LocationCoordinate2D msAntenna1Location;
    private final float msFusionAltitude;
    private final LocationCoordinate2D msFusionLocation;
    private final ReceiverInfo msReceiver1BeiDouInfo;
    private final ReceiverInfo msReceiver1GLONASSInfo;
    private final ReceiverInfo msReceiver1GPSInfo;
    private final ReceiverInfo msReceiver1GalileoInfo;
    private final ReceiverInfo msReceiver2BeiDouInfo;
    private final ReceiverInfo msReceiver2GLONASSInfo;
    private final ReceiverInfo msReceiver2GPSInfo;
    private final ReceiverInfo msReceiver2GalileoInfo;
    private final PositioningSolution positioningSolution;
    private final int satelliteCount;
    private final float takeOffAltitude;

    public interface Callback {
        void onUpdate(@NonNull RTKState rTKState);
    }

    public RTKState(Builder builder) {
        this.error = builder.error;
        this.positioningSolution = builder.positioningSolution;
        this.msReceiver1GPSInfo = builder.msReceiver1GPSInfo;
        this.msReceiver2GPSInfo = builder.msReceiver2GPSInfo;
        this.bsReceiverGPSInfo = builder.bsReceiverGPSInfo;
        this.msReceiver1BeiDouInfo = builder.msReceiver1BeiDouInfo;
        this.msReceiver2BeiDouInfo = builder.msReceiver2BeiDouInfo;
        this.bsReceiverBeiDouInfo = builder.bsReceiverBeiDouInfo;
        this.msReceiver1GLONASSInfo = builder.msReceiver1GLONASSInfo;
        this.msReceiver2GLONASSInfo = builder.msReceiver2GLONASSInfo;
        this.bsReceiverGLONASSInfo = builder.bsReceiverGLONASSInfo;
        this.msReceiver1GalileoInfo = builder.msReceiver1GalileoInfo;
        this.msReceiver2GalileoInfo = builder.msReceiver2GalileoInfo;
        this.bsReceiverGalileoInfo = builder.bsReceiverGalileoInfo;
        this.msAntenna1Location = builder.msAntenna1Location;
        this.msFusionLocation = builder.msFusionLocation;
        this.msAntenna1Altitude = builder.msAntenna1Altitude;
        this.msFusionAltitude = builder.msFusionAltitude;
        this.bsLocation = builder.bsLocation;
        this.bsAltitude = builder.bsAltitude;
        this.heading = builder.heading;
        this.locating = builder.locating;
        this.isHeadingValid = builder.isHeadingValid;
        this.isRTKBeingUsed = builder.isRTKBeingUsed;
        this.fusionHeading = builder.fusionHeading;
        this.distanceToHomePointDataSource = builder.distanceToHomeDataSource;
        this.isTakeoffAltitudeRecorded = builder.hasSetTakeOffAltitude;
        this.homePointDataSource = builder.homePointDataSource;
        this.satelliteCount = builder.satelliteCount;
        this.homePointLocation = builder.homePointLocation;
        this.takeOffAltitude = builder.takeOffAltitude;
        this.distanceToHomePoint = builder.distanceToHome;
        this.mobileStationStandardDeviation = builder.mobileStationStandardDeviation;
    }

    public DJIError getError() {
        return this.error;
    }

    public PositioningSolution getPositioningSolution() {
        return this.positioningSolution;
    }

    public ReceiverInfo getMobileStationReceiver1GPSInfo() {
        return this.msReceiver1GPSInfo;
    }

    public ReceiverInfo getMobileStationReceiver2GPSInfo() {
        return this.msReceiver2GPSInfo;
    }

    public ReceiverInfo getBaseStationReceiverGPSInfo() {
        return this.bsReceiverGPSInfo;
    }

    public ReceiverInfo getMobileStationReceiver1BeiDouInfo() {
        return this.msReceiver1BeiDouInfo;
    }

    public ReceiverInfo getMobileStationReceiver2BeiDouInfo() {
        return this.msReceiver2BeiDouInfo;
    }

    public ReceiverInfo getBaseStationReceiverBeiDouInfo() {
        return this.bsReceiverBeiDouInfo;
    }

    public ReceiverInfo getMobileStationReceiver1GLONASSInfo() {
        return this.msReceiver1GLONASSInfo;
    }

    public ReceiverInfo getMobileStationReceiver2GLONASSInfo() {
        return this.msReceiver2GLONASSInfo;
    }

    public ReceiverInfo getBaseStationReceiverGLONASSInfo() {
        return this.bsReceiverGLONASSInfo;
    }

    public ReceiverInfo getMobileStationReceiver1GalileoInfo() {
        return this.msReceiver1GalileoInfo;
    }

    public ReceiverInfo getMobileStationReceiver2GalileoInfo() {
        return this.msReceiver2GalileoInfo;
    }

    public ReceiverInfo getBaseStationReceiverGalileoInfo() {
        return this.bsReceiverGalileoInfo;
    }

    public LocationCoordinate2D getMobileStationLocation() {
        return this.msAntenna1Location;
    }

    public LocationCoordinate2D getFusionMobileStationLocation() {
        return this.msFusionLocation;
    }

    public float getMobileStationAltitude() {
        return this.msAntenna1Altitude;
    }

    public float getFusionMobileStationAltitude() {
        return this.msFusionAltitude;
    }

    public LocationCoordinate2D getBaseStationLocation() {
        return this.bsLocation;
    }

    public float getBaseStationAltitude() {
        return this.bsAltitude;
    }

    public float getHeading() {
        return this.heading;
    }

    public float getFusionHeading() {
        return this.fusionHeading;
    }

    public int getLocating() {
        return this.locating;
    }

    public boolean isHeadingValid() {
        return this.isHeadingValid;
    }

    public boolean isRTKBeingUsed() {
        return this.isRTKBeingUsed;
    }

    public int getAntannaMNum() {
        int i;
        int i2;
        int i3 = 0;
        int satelliteCount2 = this.msReceiver1GPSInfo != null ? this.msReceiver1GPSInfo.getSatelliteCount() : 0;
        if (this.msReceiver1BeiDouInfo != null) {
            i = this.msReceiver1BeiDouInfo.getSatelliteCount();
        } else {
            i = 0;
        }
        int i4 = i + satelliteCount2;
        if (this.msReceiver1GLONASSInfo != null) {
            i2 = this.msReceiver1GLONASSInfo.getSatelliteCount();
        } else {
            i2 = 0;
        }
        int i5 = i2 + i4;
        if (this.msReceiver1GalileoInfo != null) {
            i3 = this.msReceiver1GalileoInfo.getSatelliteCount();
        }
        return i5 + i3;
    }

    public int getBaseStationNum() {
        int i;
        int i2;
        int i3 = 0;
        int satelliteCount2 = this.msReceiver2BeiDouInfo != null ? this.msReceiver2BeiDouInfo.getSatelliteCount() : 0;
        if (this.msReceiver2GLONASSInfo != null) {
            i = this.msReceiver2GLONASSInfo.getSatelliteCount();
        } else {
            i = 0;
        }
        int i4 = i + satelliteCount2;
        if (this.msReceiver2GPSInfo != null) {
            i2 = this.msReceiver2GPSInfo.getSatelliteCount();
        } else {
            i2 = 0;
        }
        int i5 = i2 + i4;
        if (this.msReceiver2GalileoInfo != null) {
            i3 = this.msReceiver2GalileoInfo.getSatelliteCount();
        }
        return i5 + i3;
    }

    public DataSource getDistanceToHomePointDataSource() {
        return this.distanceToHomePointDataSource;
    }

    public boolean isTakeoffAltitudeRecorded() {
        return this.isTakeoffAltitudeRecorded;
    }

    public DataSource getHomePointDataSource() {
        return this.homePointDataSource;
    }

    public int getSatelliteCount() {
        return this.satelliteCount;
    }

    public LocationCoordinate2D getHomePointLocation() {
        return this.homePointLocation;
    }

    public float getTakeOffAltitude() {
        return this.takeOffAltitude;
    }

    public float getDistanceToHomePoint() {
        return this.distanceToHomePoint;
    }

    public LocationStandardDeviation getMobileStationStandardDeviation() {
        return this.mobileStationStandardDeviation;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RTKState rtkState = (RTKState) o;
        if (Float.compare(rtkState.msAntenna1Altitude, this.msAntenna1Altitude) != 0 || Float.compare(rtkState.msFusionAltitude, this.msFusionAltitude) != 0 || Float.compare(rtkState.bsAltitude, this.bsAltitude) != 0 || Float.compare(rtkState.getHeading(), getHeading()) != 0 || Float.compare(rtkState.getFusionHeading(), getFusionHeading()) != 0 || isHeadingValid() != rtkState.isHeadingValid() || this.locating != rtkState.locating || isRTKBeingUsed() != rtkState.isRTKBeingUsed()) {
            return false;
        }
        if (getError() != null) {
            if (!getError().equals(rtkState.getError())) {
                return false;
            }
        } else if (rtkState.getError() != null) {
            return false;
        }
        if (rtkState.distanceToHomePointDataSource != this.distanceToHomePointDataSource || rtkState.isTakeoffAltitudeRecorded != this.isTakeoffAltitudeRecorded || rtkState.homePointDataSource != this.homePointDataSource || rtkState.homePointLocation != this.homePointLocation || Float.compare(rtkState.takeOffAltitude, this.takeOffAltitude) != 0 || Float.compare(rtkState.distanceToHomePoint, this.distanceToHomePoint) != 0 || Integer.compare(rtkState.satelliteCount, this.satelliteCount) != 0 || rtkState.mobileStationStandardDeviation != this.mobileStationStandardDeviation || getPositioningSolution() != rtkState.getPositioningSolution()) {
            return false;
        }
        if (this.msReceiver1GPSInfo != null) {
            if (!this.msReceiver1GPSInfo.equals(rtkState.msReceiver1GPSInfo)) {
                return false;
            }
        } else if (rtkState.msReceiver1GPSInfo != null) {
            return false;
        }
        if (this.msReceiver2GPSInfo != null) {
            if (!this.msReceiver2GPSInfo.equals(rtkState.msReceiver2GPSInfo)) {
                return false;
            }
        } else if (rtkState.msReceiver2GPSInfo != null) {
            return false;
        }
        if (this.bsReceiverGPSInfo != null) {
            if (!this.bsReceiverGPSInfo.equals(rtkState.bsReceiverGPSInfo)) {
                return false;
            }
        } else if (rtkState.bsReceiverGPSInfo != null) {
            return false;
        }
        if (this.msReceiver1BeiDouInfo != null) {
            if (!this.msReceiver1BeiDouInfo.equals(rtkState.msReceiver1BeiDouInfo)) {
                return false;
            }
        } else if (rtkState.msReceiver1BeiDouInfo != null) {
            return false;
        }
        if (this.msReceiver2BeiDouInfo != null) {
            if (!this.msReceiver2BeiDouInfo.equals(rtkState.msReceiver2BeiDouInfo)) {
                return false;
            }
        } else if (rtkState.msReceiver2BeiDouInfo != null) {
            return false;
        }
        if (this.bsReceiverBeiDouInfo != null) {
            if (!this.bsReceiverBeiDouInfo.equals(rtkState.bsReceiverBeiDouInfo)) {
                return false;
            }
        } else if (rtkState.bsReceiverBeiDouInfo != null) {
            return false;
        }
        if (this.msReceiver1GLONASSInfo != null) {
            if (!this.msReceiver1GLONASSInfo.equals(rtkState.msReceiver1GLONASSInfo)) {
                return false;
            }
        } else if (rtkState.msReceiver1GLONASSInfo != null) {
            return false;
        }
        if (this.msReceiver2GLONASSInfo != null) {
            if (!this.msReceiver2GLONASSInfo.equals(rtkState.msReceiver2GLONASSInfo)) {
                return false;
            }
        } else if (rtkState.msReceiver2GLONASSInfo != null) {
            return false;
        }
        if (this.bsReceiverGLONASSInfo != null) {
            if (!this.bsReceiverGLONASSInfo.equals(rtkState.bsReceiverGLONASSInfo)) {
                return false;
            }
        } else if (rtkState.bsReceiverGLONASSInfo != null) {
            return false;
        }
        if (this.msReceiver1GalileoInfo != null) {
            if (!this.msReceiver1GalileoInfo.equals(rtkState.msReceiver1GalileoInfo)) {
                return false;
            }
        } else if (rtkState.msReceiver1GalileoInfo != null) {
            return false;
        }
        if (this.msReceiver2GalileoInfo != null) {
            if (!this.msReceiver2GalileoInfo.equals(rtkState.msReceiver2GalileoInfo)) {
                return false;
            }
        } else if (rtkState.msReceiver2GalileoInfo != null) {
            return false;
        }
        if (this.bsReceiverGalileoInfo != null) {
            if (!this.bsReceiverGalileoInfo.equals(rtkState.bsReceiverGalileoInfo)) {
                return false;
            }
        } else if (rtkState.bsReceiverGalileoInfo != null) {
            return false;
        }
        if (this.msAntenna1Location != null) {
            if (!this.msAntenna1Location.equals(rtkState.msAntenna1Location)) {
                return false;
            }
        } else if (rtkState.msAntenna1Location != null) {
            return false;
        }
        if (this.msFusionLocation != null) {
            if (!this.msFusionLocation.equals(rtkState.msFusionLocation)) {
                return false;
            }
        } else if (rtkState.msFusionLocation != null) {
            return false;
        }
        if (this.bsLocation != null) {
            z = this.bsLocation.equals(rtkState.bsLocation);
        } else if (rtkState.bsLocation != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        int i24;
        int i25;
        int i26;
        int i27;
        int i28;
        int i29 = 1;
        int i30 = 0;
        if (getError() != null) {
            result = getError().hashCode();
        } else {
            result = 0;
        }
        int i31 = result * 31;
        if (getPositioningSolution() != null) {
            i = getPositioningSolution().hashCode();
        } else {
            i = 0;
        }
        int i32 = (i31 + i) * 31;
        if (this.msReceiver1GPSInfo != null) {
            i2 = this.msReceiver1GPSInfo.hashCode();
        } else {
            i2 = 0;
        }
        int i33 = (i32 + i2) * 31;
        if (this.msReceiver2GPSInfo != null) {
            i3 = this.msReceiver2GPSInfo.hashCode();
        } else {
            i3 = 0;
        }
        int i34 = (i33 + i3) * 31;
        if (this.bsReceiverGPSInfo != null) {
            i4 = this.bsReceiverGPSInfo.hashCode();
        } else {
            i4 = 0;
        }
        int i35 = (i34 + i4) * 31;
        if (this.msReceiver1BeiDouInfo != null) {
            i5 = this.msReceiver1BeiDouInfo.hashCode();
        } else {
            i5 = 0;
        }
        int i36 = (i35 + i5) * 31;
        if (this.msReceiver2BeiDouInfo != null) {
            i6 = this.msReceiver2BeiDouInfo.hashCode();
        } else {
            i6 = 0;
        }
        int i37 = (i36 + i6) * 31;
        if (this.bsReceiverBeiDouInfo != null) {
            i7 = this.bsReceiverBeiDouInfo.hashCode();
        } else {
            i7 = 0;
        }
        int i38 = (i37 + i7) * 31;
        if (this.msReceiver1GLONASSInfo != null) {
            i8 = this.msReceiver1GLONASSInfo.hashCode();
        } else {
            i8 = 0;
        }
        int i39 = (i38 + i8) * 31;
        if (this.msReceiver2GLONASSInfo != null) {
            i9 = this.msReceiver2GLONASSInfo.hashCode();
        } else {
            i9 = 0;
        }
        int i40 = (i39 + i9) * 31;
        if (this.bsReceiverGLONASSInfo != null) {
            i10 = this.bsReceiverGLONASSInfo.hashCode();
        } else {
            i10 = 0;
        }
        int i41 = (i40 + i10) * 31;
        if (this.bsReceiverGalileoInfo != null) {
            i11 = this.bsReceiverGalileoInfo.hashCode();
        } else {
            i11 = 0;
        }
        int i42 = (i41 + i11) * 31;
        if (this.msReceiver1GalileoInfo != null) {
            i12 = this.msReceiver1GalileoInfo.hashCode();
        } else {
            i12 = 0;
        }
        int i43 = (i42 + i12) * 31;
        if (this.msReceiver2GalileoInfo != null) {
            i13 = this.msReceiver2GalileoInfo.hashCode();
        } else {
            i13 = 0;
        }
        int i44 = (i43 + i13) * 31;
        if (this.msAntenna1Location != null) {
            i14 = this.msAntenna1Location.hashCode();
        } else {
            i14 = 0;
        }
        int i45 = (i44 + i14) * 31;
        if (this.msFusionLocation != null) {
            i15 = this.msFusionLocation.hashCode();
        } else {
            i15 = 0;
        }
        int i46 = (i45 + i15) * 31;
        if (this.msAntenna1Altitude != 0.0f) {
            i16 = Float.floatToIntBits(this.msAntenna1Altitude);
        } else {
            i16 = 0;
        }
        int i47 = (i46 + i16) * 31;
        if (this.msFusionAltitude != 0.0f) {
            i17 = Float.floatToIntBits(this.msFusionAltitude);
        } else {
            i17 = 0;
        }
        int i48 = (i47 + i17) * 31;
        if (this.bsLocation != null) {
            i18 = this.bsLocation.hashCode();
        } else {
            i18 = 0;
        }
        int i49 = (i48 + i18) * 31;
        if (this.bsAltitude != 0.0f) {
            i19 = Float.floatToIntBits(this.bsAltitude);
        } else {
            i19 = 0;
        }
        int i50 = (i49 + i19) * 31;
        if (getHeading() != 0.0f) {
            i20 = Float.floatToIntBits(getHeading());
        } else {
            i20 = 0;
        }
        int i51 = (((i50 + i20) * 31) + this.locating) * 31;
        if (getFusionHeading() != 0.0f) {
            i21 = Float.floatToIntBits(getFusionHeading());
        } else {
            i21 = 0;
        }
        int i52 = (i51 + i21) * 31;
        if (isHeadingValid()) {
            i22 = 1;
        } else {
            i22 = 0;
        }
        int i53 = (i52 + i22) * 31;
        if (isRTKBeingUsed()) {
            i23 = 1;
        } else {
            i23 = 0;
        }
        int i54 = (i53 + i23) * 31;
        if (this.mobileStationStandardDeviation != null) {
            i24 = this.mobileStationStandardDeviation.hashCode();
        } else {
            i24 = 0;
        }
        int i55 = (i54 + i24) * 31;
        if (this.distanceToHomePointDataSource != null) {
            i25 = this.distanceToHomePointDataSource.hashCode();
        } else {
            i25 = 0;
        }
        int i56 = (i55 + i25) * 31;
        if (!isTakeoffAltitudeRecorded()) {
            i29 = 0;
        }
        int i57 = (i56 + i29) * 31;
        if (this.homePointDataSource != null) {
            i26 = this.homePointDataSource.hashCode();
        } else {
            i26 = 0;
        }
        int i58 = (((i57 + i26) * 31) + this.satelliteCount) * 31;
        if (this.homePointLocation != null) {
            i27 = this.homePointLocation.hashCode();
        } else {
            i27 = 0;
        }
        int i59 = (i58 + i27) * 31;
        if (this.takeOffAltitude != 0.0f) {
            i28 = Float.floatToIntBits(this.takeOffAltitude);
        } else {
            i28 = 0;
        }
        int i60 = (i59 + i28) * 31;
        if (this.distanceToHomePoint != 0.0f) {
            i30 = Float.floatToIntBits(this.distanceToHomePoint);
        }
        return i60 + i30;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public float bsAltitude;
        /* access modifiers changed from: private */
        public LocationCoordinate2D bsLocation;
        /* access modifiers changed from: private */
        public ReceiverInfo bsReceiverBeiDouInfo;
        /* access modifiers changed from: private */
        public ReceiverInfo bsReceiverGLONASSInfo;
        /* access modifiers changed from: private */
        public ReceiverInfo bsReceiverGPSInfo;
        /* access modifiers changed from: private */
        public ReceiverInfo bsReceiverGalileoInfo;
        /* access modifiers changed from: private */
        public float distanceToHome;
        /* access modifiers changed from: private */
        public DataSource distanceToHomeDataSource;
        /* access modifiers changed from: private */
        public DJIError error;
        /* access modifiers changed from: private */
        public float fusionHeading;
        /* access modifiers changed from: private */
        public boolean hasSetTakeOffAltitude;
        /* access modifiers changed from: private */
        public float heading;
        /* access modifiers changed from: private */
        public DataSource homePointDataSource;
        /* access modifiers changed from: private */
        public LocationCoordinate2D homePointLocation;
        /* access modifiers changed from: private */
        public boolean isHeadingValid;
        /* access modifiers changed from: private */
        public boolean isRTKBeingUsed;
        /* access modifiers changed from: private */
        public int locating;
        /* access modifiers changed from: private */
        public LocationStandardDeviation mobileStationStandardDeviation;
        /* access modifiers changed from: private */
        public float msAntenna1Altitude;
        /* access modifiers changed from: private */
        public LocationCoordinate2D msAntenna1Location;
        /* access modifiers changed from: private */
        public float msFusionAltitude;
        /* access modifiers changed from: private */
        public LocationCoordinate2D msFusionLocation;
        /* access modifiers changed from: private */
        public ReceiverInfo msReceiver1BeiDouInfo;
        /* access modifiers changed from: private */
        public ReceiverInfo msReceiver1GLONASSInfo;
        /* access modifiers changed from: private */
        public ReceiverInfo msReceiver1GPSInfo;
        /* access modifiers changed from: private */
        public ReceiverInfo msReceiver1GalileoInfo;
        /* access modifiers changed from: private */
        public ReceiverInfo msReceiver2BeiDouInfo;
        /* access modifiers changed from: private */
        public ReceiverInfo msReceiver2GLONASSInfo;
        /* access modifiers changed from: private */
        public ReceiverInfo msReceiver2GPSInfo;
        /* access modifiers changed from: private */
        public ReceiverInfo msReceiver2GalileoInfo;
        /* access modifiers changed from: private */
        public PositioningSolution positioningSolution;
        /* access modifiers changed from: private */
        public int satelliteCount;
        /* access modifiers changed from: private */
        public float takeOffAltitude;

        public Builder mobileStationStandardDeviation(LocationStandardDeviation locationStd) {
            this.mobileStationStandardDeviation = locationStd;
            return this;
        }

        public Builder distanceToHomeDataSource(DataSource source) {
            this.distanceToHomeDataSource = source;
            return this;
        }

        public Builder hasSetTakeOffAltitude(boolean hasSet) {
            this.hasSetTakeOffAltitude = hasSet;
            return this;
        }

        public Builder homePointDataSource(DataSource source) {
            this.homePointDataSource = source;
            return this;
        }

        public Builder homePointLocation(LocationCoordinate2D homePointLocation2) {
            this.homePointLocation = homePointLocation2;
            return this;
        }

        public Builder takeOffAltitude(float takeOffAltitude2) {
            this.takeOffAltitude = takeOffAltitude2;
            return this;
        }

        public Builder distanceToHome(float distanceToHome2) {
            this.distanceToHome = distanceToHome2;
            return this;
        }

        public Builder msAntenna1Altitude(float msAntenna1Altitude2) {
            this.msAntenna1Altitude = msAntenna1Altitude2;
            return this;
        }

        public Builder msFusionAltitude(float msMixedAltitude) {
            this.msFusionAltitude = msMixedAltitude;
            return this;
        }

        public Builder msFusionHeading(float fusionHeading2) {
            this.fusionHeading = fusionHeading2;
            return this;
        }

        public Builder bsAltitude(float bsAltitude2) {
            this.bsAltitude = bsAltitude2;
            return this;
        }

        public Builder heading(float heading2) {
            this.heading = heading2;
            return this;
        }

        public Builder isHeadingValid(boolean isHeadingValid2) {
            this.isHeadingValid = isHeadingValid2;
            return this;
        }

        public Builder locating(int locating2) {
            this.locating = locating2;
            return this;
        }

        public Builder isRTKBeingUsed(boolean isRTKBeingUsed2) {
            this.isRTKBeingUsed = isRTKBeingUsed2;
            return this;
        }

        public Builder msReceiver1GPSInfo(ReceiverInfo msReceiver1GPSInfo2) {
            this.msReceiver1GPSInfo = msReceiver1GPSInfo2;
            return this;
        }

        public Builder msReceiver2GPSInfo(ReceiverInfo msReceiver2GPSInfo2) {
            this.msReceiver2GPSInfo = msReceiver2GPSInfo2;
            return this;
        }

        public Builder bsReceiverGPSInfo(ReceiverInfo bsReceiverGPSInfo2) {
            this.bsReceiverGPSInfo = bsReceiverGPSInfo2;
            return this;
        }

        public Builder msReceiver1BeiDouInfo(ReceiverInfo msReceiver1BeiDouInfo2) {
            this.msReceiver1BeiDouInfo = msReceiver1BeiDouInfo2;
            return this;
        }

        public Builder msReceiver2BeiDouInfo(ReceiverInfo msReceiver2BeiDouInfo2) {
            this.msReceiver2BeiDouInfo = msReceiver2BeiDouInfo2;
            return this;
        }

        public Builder bsReceiverBeiDouInfo(ReceiverInfo bsReceiverBeiDouInfo2) {
            this.bsReceiverBeiDouInfo = bsReceiverBeiDouInfo2;
            return this;
        }

        public Builder msReceiver1GLONASSInfo(ReceiverInfo msReceiver1GLONASSInfo2) {
            this.msReceiver1GLONASSInfo = msReceiver1GLONASSInfo2;
            return this;
        }

        public Builder msReceiver2GLONASSInfo(ReceiverInfo msReceiver2GLONASSInfo2) {
            this.msReceiver2GLONASSInfo = msReceiver2GLONASSInfo2;
            return this;
        }

        public Builder bsReceiverGLONASSInfo(ReceiverInfo bsReceiverGLONASSInfo2) {
            this.bsReceiverGLONASSInfo = bsReceiverGLONASSInfo2;
            return this;
        }

        public Builder msReceiver1GalileoInfo(ReceiverInfo msReceiver1GalileoInfo2) {
            this.msReceiver1GalileoInfo = msReceiver1GalileoInfo2;
            return this;
        }

        public Builder msReceiver2GalileoInfo(ReceiverInfo msReceiver2GalileoInfo2) {
            this.msReceiver2GalileoInfo = msReceiver2GalileoInfo2;
            return this;
        }

        public Builder bsReceiverGalileoInfo(ReceiverInfo bsReceiverGalileoInfo2) {
            this.bsReceiverGalileoInfo = bsReceiverGalileoInfo2;
            return this;
        }

        public Builder msAntenna1Location(LocationCoordinate2D msAntenna1Location2) {
            this.msAntenna1Location = msAntenna1Location2;
            return this;
        }

        public Builder msFusionLocation(LocationCoordinate2D msMixedLocation) {
            this.msFusionLocation = msMixedLocation;
            return this;
        }

        public Builder bsLocation(LocationCoordinate2D bsLocation2) {
            this.bsLocation = bsLocation2;
            return this;
        }

        public Builder positioningSolution(PositioningSolution positioningSolution2) {
            this.positioningSolution = positioningSolution2;
            return this;
        }

        public Builder error(DJIError error2) {
            this.error = error2;
            return this;
        }

        public Builder gpsCount(int gpsCount) {
            this.satelliteCount = gpsCount;
            return this;
        }

        public RTKState build() {
            return new RTKState(this);
        }
    }
}
