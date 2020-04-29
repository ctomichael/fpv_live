package dji.common.camera;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class PhotoTimeLapseSettings {
    private int duration;
    private SettingsDefinitions.PhotoTimeLapseFileFormat fileFormat = SettingsDefinitions.PhotoTimeLapseFileFormat.UNKNOWN;
    private int interval;

    public PhotoTimeLapseSettings(int interval2, int duration2, SettingsDefinitions.PhotoTimeLapseFileFormat fileFormat2) {
        this.interval = interval2;
        this.duration = duration2;
        this.fileFormat = fileFormat2;
    }

    public int hashCode() {
        return (((this.interval * 31) + this.duration) * 31) + (this.fileFormat != null ? this.fileFormat.hashCode() : 0);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof PhotoTimeLapseSettings)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.interval == ((PhotoTimeLapseSettings) o).interval && this.duration == ((PhotoTimeLapseSettings) o).duration && this.fileFormat == ((PhotoTimeLapseSettings) o).fileFormat;
    }

    public void setDuration(@IntRange(from = 0, to = 2147483647L) int duration2) {
        this.duration = duration2;
    }

    public void setInterval(@IntRange(from = 0, to = 1000) int interval2) {
        this.interval = interval2;
    }

    public void setFileFormat(@NonNull SettingsDefinitions.PhotoTimeLapseFileFormat fileFormat2) {
        this.fileFormat = fileFormat2;
    }

    public SettingsDefinitions.PhotoTimeLapseFileFormat getFileFormat() {
        return this.fileFormat;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getInterval() {
        return this.interval;
    }
}
