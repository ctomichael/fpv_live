package dji.common.flightcontroller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class VisionDetectionState {
    private final VisionSystemWarning SystemWarning;
    private final ObstacleDetectionSector[] detectionSectors;
    private final boolean isDisabled;
    private final double obstacleDistanceInMeters;
    private final VisionSensorPosition position;
    private final boolean sensorBeingUsed;

    public interface Callback {
        void onUpdate(@NonNull VisionDetectionState visionDetectionState);
    }

    private VisionDetectionState(boolean sensorBeingUsed2, double obstacleDistanceInMeters2, VisionSystemWarning SystemWarning2, ObstacleDetectionSector[] detectionSectors2, VisionSensorPosition position2, boolean isDisabled2) {
        this.sensorBeingUsed = sensorBeingUsed2;
        this.obstacleDistanceInMeters = obstacleDistanceInMeters2;
        this.SystemWarning = SystemWarning2;
        this.detectionSectors = detectionSectors2;
        this.position = position2;
        this.isDisabled = isDisabled2;
    }

    public static VisionDetectionState createInstance(boolean sensorBeingUsed2, double obstacleDistanceInMeters2, VisionSystemWarning warningLevel, ObstacleDetectionSector[] detectionSectors2, VisionSensorPosition position2, boolean isDisabled2) {
        return new VisionDetectionState(sensorBeingUsed2, obstacleDistanceInMeters2, warningLevel, detectionSectors2, position2, isDisabled2);
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4 = 1;
        long temp = Double.doubleToLongBits(this.obstacleDistanceInMeters);
        if (this.sensorBeingUsed) {
            result = 1;
        } else {
            result = 0;
        }
        int i5 = ((result * 31) + ((int) ((temp >>> 32) ^ temp))) * 31;
        if (this.SystemWarning != null) {
            i = this.SystemWarning.hashCode();
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (this.position != null) {
            i2 = this.position.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (i6 + i2) * 31;
        if (this.detectionSectors != null) {
            i3 = this.detectionSectors.hashCode();
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (!this.isDisabled) {
            i4 = 0;
        }
        return i8 + i4;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof VisionDetectionState)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.detectionSectors == null || ((VisionDetectionState) o).detectionSectors == null) {
            if (this.detectionSectors == null || ((VisionDetectionState) o).detectionSectors == null) {
                return false;
            }
        } else if (this.detectionSectors.length != ((VisionDetectionState) o).detectionSectors.length) {
            return false;
        } else {
            for (int i = 0; i < this.detectionSectors.length; i++) {
                if (this.detectionSectors[i] != ((VisionDetectionState) o).detectionSectors[i]) {
                    return false;
                }
            }
        }
        return this.sensorBeingUsed == ((VisionDetectionState) o).sensorBeingUsed && this.obstacleDistanceInMeters == ((VisionDetectionState) o).obstacleDistanceInMeters && this.SystemWarning == ((VisionDetectionState) o).SystemWarning && this.position == ((VisionDetectionState) o).position && this.isDisabled == ((VisionDetectionState) o).isDisabled;
    }

    public double getObstacleDistanceInMeters() {
        return this.obstacleDistanceInMeters;
    }

    public VisionSensorPosition getPosition() {
        return this.position;
    }

    public boolean isSensorBeingUsed() {
        return this.sensorBeingUsed;
    }

    public VisionSystemWarning getSystemWarning() {
        return this.SystemWarning;
    }

    @Nullable
    public ObstacleDetectionSector[] getDetectionSectors() {
        if (this.detectionSectors != null) {
            return (ObstacleDetectionSector[]) this.detectionSectors.clone();
        }
        return null;
    }

    public boolean isDisabled() {
        return this.isDisabled;
    }
}
