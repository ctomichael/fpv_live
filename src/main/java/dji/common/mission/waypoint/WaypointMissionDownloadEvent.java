package dji.common.mission.waypoint;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dji.common.error.DJIError;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class WaypointMissionDownloadEvent extends WaypointMissionEvent {
    @Nullable
    private final WaypointDownloadProgress progress;

    public WaypointMissionDownloadEvent(@NonNull Builder builder) {
        super(builder.error);
        this.progress = builder.progress;
    }

    @Nullable
    public WaypointDownloadProgress getProgress() {
        return this.progress;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        @Nullable
        public DJIError error;
        /* access modifiers changed from: private */
        @Nullable
        public WaypointDownloadProgress progress;

        public Builder error(DJIError error2) {
            this.error = error2;
            return this;
        }

        public Builder progress(WaypointDownloadProgress progress2) {
            this.progress = progress2;
            return this;
        }

        public WaypointMissionDownloadEvent build() {
            return new WaypointMissionDownloadEvent(this);
        }
    }

    public String toString() {
        return "DownloadEvent=[" + this.progress + IMemberProtocol.STRING_SEPERATOR_RIGHT;
    }
}
