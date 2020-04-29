package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class ReceiverInfo {
    private final boolean constellationSupported;
    private final int satelliteCount;

    private ReceiverInfo(boolean constellationSupported2, int satelliteCount2) {
        this.constellationSupported = constellationSupported2;
        this.satelliteCount = satelliteCount2;
    }

    public static ReceiverInfo createInstance(boolean constellationSupported2, int satelliteCount2) {
        return new ReceiverInfo(constellationSupported2, satelliteCount2);
    }

    public boolean isConstellationSupported() {
        return this.constellationSupported;
    }

    public int getSatelliteCount() {
        return this.satelliteCount;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReceiverInfo that = (ReceiverInfo) o;
        if (isConstellationSupported() != that.isConstellationSupported()) {
            return false;
        }
        if (getSatelliteCount() != that.getSatelliteCount()) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((isConstellationSupported() ? 1 : 0) * 31) + getSatelliteCount();
    }
}
