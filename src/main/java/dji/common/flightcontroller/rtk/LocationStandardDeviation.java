package dji.common.flightcontroller.rtk;

public class LocationStandardDeviation {
    private final float stdAltitude;
    private final float stdLatitude;
    private final float stdLongitude;

    public LocationStandardDeviation(float stdLatitude2, float stdLongitude2, float stdAltitude2) {
        this.stdLatitude = stdLatitude2;
        this.stdLongitude = stdLongitude2;
        this.stdAltitude = stdAltitude2;
    }

    public float getStdLatitude() {
        return this.stdLatitude;
    }

    public float getStdLongitude() {
        return this.stdLongitude;
    }

    public float getStdAltitude() {
        return this.stdAltitude;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationStandardDeviation that = (LocationStandardDeviation) o;
        if (Float.compare(that.getStdLatitude(), getStdLatitude()) == 0 && Float.compare(that.getStdLongitude(), getStdLongitude()) == 0 && Float.compare(that.getStdAltitude(), getStdAltitude()) == 0) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.stdLatitude != 0.0f) {
            result = Float.floatToIntBits(this.stdLatitude);
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.stdLongitude != 0.0f) {
            i = Float.floatToIntBits(this.stdLongitude);
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.stdAltitude != 0.0f) {
            i2 = Float.floatToIntBits(this.stdAltitude);
        }
        return i4 + i2;
    }
}
