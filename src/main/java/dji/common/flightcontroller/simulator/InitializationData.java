package dji.common.flightcontroller.simulator;

import android.support.annotation.IntRange;
import dji.common.model.LocationCoordinate2D;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class InitializationData {
    private final LocationCoordinate2D location;
    private final int satelliteCount;
    private final int updateFrequency;

    private InitializationData(LocationCoordinate2D location2, int updateFrequency2, int satelliteCount2) {
        this.location = location2;
        this.updateFrequency = updateFrequency2;
        this.satelliteCount = satelliteCount2;
    }

    public static InitializationData createInstance(LocationCoordinate2D location2, @IntRange(from = 2, to = 150) int updateFrequency2, @IntRange(from = 0, to = 20) int satelliteCount2) {
        return new InitializationData(location2, updateFrequency2, satelliteCount2);
    }

    public LocationCoordinate2D getLocation() {
        return this.location;
    }

    public int getUpdateFrequency() {
        return this.updateFrequency;
    }

    public int getSatelliteCount() {
        return this.satelliteCount;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof InitializationData)) {
            return false;
        }
        InitializationData that = (InitializationData) o;
        if (this.updateFrequency != that.updateFrequency || this.satelliteCount != that.satelliteCount) {
            return false;
        }
        if (this.location != null) {
            z = this.location.equals(that.location);
        } else if (that.location != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return ((((this.location != null ? this.location.hashCode() : 0) * 31) + this.updateFrequency) * 31) + this.satelliteCount;
    }
}
