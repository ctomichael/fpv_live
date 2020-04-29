package dji.common.mission.waypoint;

import android.support.annotation.IntRange;
import dji.fieldAnnotation.EXClassNullAway;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@EXClassNullAway
public class WaypointUploadProgress {
    public static final int UNKNOWN = -1;
    public boolean isSummaryUploaded = false;
    @IntRange(from = 2, to = 99)
    public int totalWaypointCount = -1;
    @IntRange(from = 0, to = 98)
    public int uploadedWaypointIndex = -1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface InitialValue {
    }

    public int hashCode() {
        return (((this.uploadedWaypointIndex * 31) + this.totalWaypointCount) * 31) + (this.isSummaryUploaded ? 1 : 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        boolean isEqual = false;
        if (obj instanceof WaypointUploadProgress) {
            WaypointUploadProgress object = (WaypointUploadProgress) obj;
            if (object.uploadedWaypointIndex == this.uploadedWaypointIndex && object.isSummaryUploaded == this.isSummaryUploaded && object.totalWaypointCount == this.totalWaypointCount) {
                isEqual = true;
            } else {
                isEqual = false;
            }
        }
        return isEqual;
    }
}
