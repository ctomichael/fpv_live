package dji.common.camera;

public class WatermarkDisplayContentSettings {
    private final boolean cameraSnEnabled;
    private final boolean cameraTypeEnabled;
    private final boolean dateTimeEnabled;
    private final boolean droneSnEnabled;
    private final boolean droneTypeEnabled;
    private final boolean gpsAltitudeEnabled;
    private final boolean gpsLonLatEnabled;
    private final boolean timeZoneEnabled;
    private final boolean userCustomStringEnabled;

    public WatermarkDisplayContentSettings(Builder builder) {
        this.droneTypeEnabled = builder.droneTypeEnabled;
        this.droneSnEnabled = builder.droneSnEnabled;
        this.cameraTypeEnabled = builder.cameraTypeEnabled;
        this.cameraSnEnabled = builder.cameraSnEnabled;
        this.dateTimeEnabled = builder.dateTimeEnabled;
        this.timeZoneEnabled = builder.timeZoneEnabled;
        this.gpsLonLatEnabled = builder.gpsLonLatEnabled;
        this.gpsAltitudeEnabled = builder.gpsAltitudeEnabled;
        this.userCustomStringEnabled = builder.userCustomStringEnabled;
    }

    public boolean isDroneTypeEnabled() {
        return this.droneTypeEnabled;
    }

    public boolean isDroneSnEnabled() {
        return this.droneSnEnabled;
    }

    public boolean isCameraTypeEnabled() {
        return this.cameraTypeEnabled;
    }

    public boolean isCameraSnEnabled() {
        return this.cameraSnEnabled;
    }

    public boolean isDateTimeEnabled() {
        return this.dateTimeEnabled;
    }

    public boolean isTimeZoneEnabled() {
        return this.timeZoneEnabled;
    }

    public boolean isGpsLonLatEnabled() {
        return this.gpsLonLatEnabled;
    }

    public boolean isGpsAltitudeEnabled() {
        return this.gpsAltitudeEnabled;
    }

    public boolean isUserCustomStringEnabled() {
        return this.userCustomStringEnabled;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WatermarkDisplayContentSettings that = (WatermarkDisplayContentSettings) o;
        if (isDroneTypeEnabled() != that.isDroneTypeEnabled() || isDroneSnEnabled() != that.isDroneSnEnabled() || isCameraTypeEnabled() != that.isCameraTypeEnabled() || isCameraSnEnabled() != that.isCameraSnEnabled() || isDateTimeEnabled() != that.isDateTimeEnabled() || isTimeZoneEnabled() != that.isTimeZoneEnabled() || isGpsLonLatEnabled() != that.isGpsLonLatEnabled() || isGpsAltitudeEnabled() != that.isGpsAltitudeEnabled()) {
            return false;
        }
        if (isUserCustomStringEnabled() != that.isUserCustomStringEnabled()) {
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
        int i8 = 1;
        if (isDroneTypeEnabled()) {
            result = 1;
        } else {
            result = 0;
        }
        int i9 = result * 31;
        if (isDroneSnEnabled()) {
            i = 1;
        } else {
            i = 0;
        }
        int i10 = (i9 + i) * 31;
        if (isCameraTypeEnabled()) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i11 = (i10 + i2) * 31;
        if (isCameraSnEnabled()) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i12 = (i11 + i3) * 31;
        if (isDateTimeEnabled()) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        int i13 = (i12 + i4) * 31;
        if (isTimeZoneEnabled()) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        int i14 = (i13 + i5) * 31;
        if (isGpsLonLatEnabled()) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        int i15 = (i14 + i6) * 31;
        if (isGpsAltitudeEnabled()) {
            i7 = 1;
        } else {
            i7 = 0;
        }
        int i16 = (i15 + i7) * 31;
        if (!isUserCustomStringEnabled()) {
            i8 = 0;
        }
        return i16 + i8;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public boolean cameraSnEnabled;
        /* access modifiers changed from: private */
        public boolean cameraTypeEnabled;
        /* access modifiers changed from: private */
        public boolean dateTimeEnabled;
        /* access modifiers changed from: private */
        public boolean droneSnEnabled;
        /* access modifiers changed from: private */
        public boolean droneTypeEnabled;
        /* access modifiers changed from: private */
        public boolean gpsAltitudeEnabled;
        /* access modifiers changed from: private */
        public boolean gpsLonLatEnabled;
        /* access modifiers changed from: private */
        public boolean timeZoneEnabled;
        /* access modifiers changed from: private */
        public boolean userCustomStringEnabled;

        public Builder isDroneTypeEnabled(boolean droneTypeEnabled2) {
            this.droneTypeEnabled = droneTypeEnabled2;
            return this;
        }

        public Builder isDroneSnEnabled(boolean droneSnEnabled2) {
            this.droneSnEnabled = droneSnEnabled2;
            return this;
        }

        public Builder isCameraTypeEnabled(boolean cameraTypeEnabled2) {
            this.cameraTypeEnabled = cameraTypeEnabled2;
            return this;
        }

        public Builder isCameraSnEnabled(boolean cameraSnEnabled2) {
            this.cameraSnEnabled = cameraSnEnabled2;
            return this;
        }

        public Builder isDateTimeEnabled(boolean isDateTimeEnabled) {
            this.dateTimeEnabled = isDateTimeEnabled;
            return this;
        }

        public Builder isTimeZoneEnabled(boolean timeZoneEnabled2) {
            this.timeZoneEnabled = timeZoneEnabled2;
            return this;
        }

        public Builder isGPSLonLatEnabled(boolean gpsLonLatEnabled2) {
            this.gpsLonLatEnabled = gpsLonLatEnabled2;
            return this;
        }

        public Builder isGPSAltitudeEnabled(boolean gpsAltitudeEnabled2) {
            this.gpsAltitudeEnabled = gpsAltitudeEnabled2;
            return this;
        }

        public Builder isUserCustomStringEnabled(boolean userCustomStringEnabled2) {
            this.userCustomStringEnabled = userCustomStringEnabled2;
            return this;
        }

        public WatermarkDisplayContentSettings build() {
            return new WatermarkDisplayContentSettings(this);
        }
    }
}
