package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ObstacleDetectionSector {
    private final float obstacleDistanceInMeters;
    private final ObstacleDetectionSectorWarning warningLevel;

    public ObstacleDetectionSector(ObstacleDetectionSectorWarning warningLevel2, float distanceInMeter) {
        this.warningLevel = warningLevel2;
        this.obstacleDistanceInMeters = distanceInMeter;
    }

    public ObstacleDetectionSectorWarning getWarningLevel() {
        return this.warningLevel;
    }

    public float getObstacleDistanceInMeters() {
        return this.obstacleDistanceInMeters;
    }

    public int hashCode() {
        return ((this.warningLevel != null ? this.warningLevel.hashCode() : 0) * 31) + Float.floatToIntBits(this.obstacleDistanceInMeters);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ObstacleDetectionSector)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.warningLevel == ((ObstacleDetectionSector) o).warningLevel && this.obstacleDistanceInMeters == ((ObstacleDetectionSector) o).obstacleDistanceInMeters;
    }
}
