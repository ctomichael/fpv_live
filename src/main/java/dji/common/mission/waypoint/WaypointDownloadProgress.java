package dji.common.mission.waypoint;

import android.support.annotation.IntRange;
import dji.fieldAnnotation.EXClassNullAway;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@EXClassNullAway
public class WaypointDownloadProgress {
    public static final int UNKNOWN = -1;
    @IntRange(from = 0, to = 98)
    public int downloadedWaypointIndex = -1;
    public boolean isSummaryDownloaded = false;
    @IntRange(from = 2, to = 99)
    public int totalWaypointCount = -1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface InitialValue {
    }

    public int hashCode() {
        return (((this.downloadedWaypointIndex * 31) + this.totalWaypointCount) * 31) + (this.isSummaryDownloaded ? 1 : 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        boolean isEqual = false;
        if (obj instanceof WaypointDownloadProgress) {
            WaypointDownloadProgress object = (WaypointDownloadProgress) obj;
            if (object.downloadedWaypointIndex == this.downloadedWaypointIndex && object.isSummaryDownloaded == this.isSummaryDownloaded && object.totalWaypointCount == this.totalWaypointCount) {
                isEqual = true;
            } else {
                isEqual = false;
            }
        }
        return isEqual;
    }

    public String toString() {
        return "isSummary= Total=" + this.totalWaypointCount + " index=" + this.downloadedWaypointIndex;
    }
}
