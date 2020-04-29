package dji.common.remotecontroller;

import android.support.annotation.NonNull;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class GPSData {
    private float eastSpeed;
    private boolean isValid;
    private GPSLocation location;
    private float locationAccuracy;
    private float northSpeed;
    private int satelliteCount;
    private Time time;

    public interface Callback {
        void onUpdate(@NonNull GPSData gPSData);
    }

    private GPSData(Builder builder) {
        this.time = builder.time;
        this.eastSpeed = builder.eastSpeed;
        this.northSpeed = builder.northSpeed;
        this.location = builder.location;
        this.isValid = builder.isValid;
        this.locationAccuracy = builder.locationAccuracy;
        this.satelliteCount = builder.satelliteCount;
    }

    public Time getTime() {
        return this.time;
    }

    public GPSLocation getLocation() {
        return this.location;
    }

    public float getEastSpeed() {
        return this.eastSpeed;
    }

    public float getNorthSpeed() {
        return this.northSpeed;
    }

    public int getSatelliteCount() {
        return this.satelliteCount;
    }

    public float getLocationAccuracy() {
        return this.locationAccuracy;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GPSData gpsData = (GPSData) o;
        if (Float.compare(gpsData.eastSpeed, this.eastSpeed) != 0 || Float.compare(gpsData.northSpeed, this.northSpeed) != 0 || this.satelliteCount != gpsData.satelliteCount || Float.compare(gpsData.locationAccuracy, this.locationAccuracy) != 0 || this.isValid != gpsData.isValid) {
            return false;
        }
        if (this.time != null) {
            if (!this.time.equals(gpsData.time)) {
                return false;
            }
        } else if (gpsData.time != null) {
            return false;
        }
        if (this.location != null) {
            z = this.location.equals(gpsData.location);
        } else if (gpsData.location != null) {
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
        int i5 = 0;
        if (this.time != null) {
            result = this.time.hashCode();
        } else {
            result = 0;
        }
        int i6 = result * 31;
        if (this.location != null) {
            i = this.location.hashCode();
        } else {
            i = 0;
        }
        int i7 = (i6 + i) * 31;
        if (this.eastSpeed != 0.0f) {
            i2 = Float.floatToIntBits(this.eastSpeed);
        } else {
            i2 = 0;
        }
        int i8 = (i7 + i2) * 31;
        if (this.northSpeed != 0.0f) {
            i3 = Float.floatToIntBits(this.northSpeed);
        } else {
            i3 = 0;
        }
        int i9 = (((i8 + i3) * 31) + this.satelliteCount) * 31;
        if (this.locationAccuracy != 0.0f) {
            i4 = Float.floatToIntBits(this.locationAccuracy);
        } else {
            i4 = 0;
        }
        int i10 = (i9 + i4) * 31;
        if (this.isValid) {
            i5 = 1;
        }
        return i10 + i5;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public float eastSpeed;
        /* access modifiers changed from: private */
        public boolean isValid;
        /* access modifiers changed from: private */
        public GPSLocation location;
        /* access modifiers changed from: private */
        public float locationAccuracy;
        /* access modifiers changed from: private */
        public float northSpeed;
        /* access modifiers changed from: private */
        public int satelliteCount;
        /* access modifiers changed from: private */
        public Time time;

        public Builder time(Time time2) {
            this.time = time2;
            return this;
        }

        public Builder location(GPSLocation location2) {
            this.location = location2;
            return this;
        }

        public Builder eastSpeed(float speed) {
            this.eastSpeed = speed;
            return this;
        }

        public Builder northSpeed(float speed) {
            this.northSpeed = speed;
            return this;
        }

        public Builder satelliteCount(int satelliteCount2) {
            this.satelliteCount = satelliteCount2;
            return this;
        }

        public Builder locationAccuracy(float locationAccuracy2) {
            this.locationAccuracy = locationAccuracy2;
            return this;
        }

        public Builder isValid(boolean isValid2) {
            this.isValid = isValid2;
            return this;
        }

        public GPSData build() {
            return new GPSData(this);
        }
    }

    public static class Time {
        private byte day;
        private byte hour;
        private byte minute;
        private byte month;
        private byte second;
        private int year;

        public static final class Builder {
            /* access modifiers changed from: private */
            public byte day;
            /* access modifiers changed from: private */
            public byte hour;
            /* access modifiers changed from: private */
            public byte minute;
            /* access modifiers changed from: private */
            public byte month;
            /* access modifiers changed from: private */
            public byte second;
            /* access modifiers changed from: private */
            public int year;

            public Builder hour(byte hour2) {
                this.hour = hour2;
                return this;
            }

            public Builder minute(byte minute2) {
                this.minute = minute2;
                return this;
            }

            public Builder second(byte second2) {
                this.second = second2;
                return this;
            }

            public Builder year(int year2) {
                this.year = year2;
                return this;
            }

            public Builder month(byte month2) {
                this.month = month2;
                return this;
            }

            public Builder day(byte day2) {
                this.day = day2;
                return this;
            }

            public Time build() {
                return new Time(this);
            }
        }

        private Time(Builder builder) {
            this.hour = builder.hour;
            this.minute = builder.minute;
            this.second = builder.second;
            this.year = builder.year;
            this.month = builder.month;
            this.day = builder.day;
        }

        public byte getHour() {
            return this.hour;
        }

        public byte getMinute() {
            return this.minute;
        }

        public byte getSecond() {
            return this.second;
        }

        public int getYear() {
            return this.year;
        }

        public byte getMonth() {
            return this.month;
        }

        public byte getDay() {
            return this.day;
        }

        public String toString() {
            return ((int) this.hour) + ":" + ((int) this.minute) + ":" + ((int) this.second) + " " + this.year + IMemberProtocol.PARAM_SEPERATOR + ((int) this.month) + IMemberProtocol.PARAM_SEPERATOR + ((int) this.day);
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Time time = (Time) o;
            if (this.hour != time.hour || this.minute != time.minute || this.second != time.second || this.year != time.year || this.month != time.month) {
                return false;
            }
            if (this.day != time.day) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return (((((((((this.hour * 31) + this.minute) * 31) + this.second) * 31) + this.year) * 31) + this.month) * 31) + this.day;
        }
    }

    public static class GPSLocation {
        private final double latitude;
        private final double longitude;

        public GPSLocation(double longitude2, double latitude2) {
            this.longitude = longitude2;
            this.latitude = latitude2;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public double getLatitude() {
            return this.latitude;
        }
    }
}
