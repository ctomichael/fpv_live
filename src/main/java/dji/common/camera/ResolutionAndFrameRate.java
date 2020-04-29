package dji.common.camera;

import android.support.annotation.NonNull;
import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ResolutionAndFrameRate implements Comparable<ResolutionAndFrameRate> {
    private SettingsDefinitions.VideoFov fov = SettingsDefinitions.VideoFov.DEFAULT;
    private SettingsDefinitions.VideoFrameRate frameRate = SettingsDefinitions.VideoFrameRate.UNKNOWN;
    private SettingsDefinitions.VideoResolution resolution = SettingsDefinitions.VideoResolution.UNKNOWN;

    public ResolutionAndFrameRate(@NonNull SettingsDefinitions.VideoResolution resolution2, @NonNull SettingsDefinitions.VideoFrameRate frameRate2) {
        this.resolution = resolution2;
        this.frameRate = frameRate2;
    }

    public ResolutionAndFrameRate(@NonNull SettingsDefinitions.VideoResolution resolution2, @NonNull SettingsDefinitions.VideoFrameRate frameRate2, @NonNull SettingsDefinitions.VideoFov fov2) {
        this.resolution = resolution2;
        this.frameRate = frameRate2;
        this.fov = fov2;
    }

    public void setResolution(@NonNull SettingsDefinitions.VideoResolution resolution2) {
        this.resolution = resolution2;
    }

    public void setFrameRate(@NonNull SettingsDefinitions.VideoFrameRate frameRate2) {
        this.frameRate = frameRate2;
    }

    public SettingsDefinitions.VideoResolution getResolution() {
        return this.resolution;
    }

    public SettingsDefinitions.VideoFrameRate getFrameRate() {
        return this.frameRate;
    }

    public SettingsDefinitions.VideoFov getFov() {
        return this.fov;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResolutionAndFrameRate that = (ResolutionAndFrameRate) o;
        if (getResolution() != that.getResolution() || getFrameRate() != that.getFrameRate()) {
            return false;
        }
        if (getFov() != that.getFov()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (getResolution() != null) {
            result = getResolution().hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (getFrameRate() != null) {
            i = getFrameRate().hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (getFov() != null) {
            i2 = getFov().hashCode();
        }
        return i4 + i2;
    }

    public String toString() {
        return "ResolutionAndFrameRate{resolution=" + this.resolution + ", frameRate=" + this.frameRate + ", fov=" + this.fov + '}';
    }

    public int compareTo(@NonNull ResolutionAndFrameRate resolutionAndFrameRate) {
        if (this.resolution.value() > resolutionAndFrameRate.resolution.value()) {
            return 1;
        }
        if (this.resolution.value() < resolutionAndFrameRate.resolution.value()) {
            return -1;
        }
        if (this.frameRate.value() > resolutionAndFrameRate.frameRate.value()) {
            return 1;
        }
        if (this.frameRate.value() < resolutionAndFrameRate.frameRate.value()) {
            return -1;
        }
        if (this.fov.value() > resolutionAndFrameRate.fov.value()) {
            return 1;
        }
        if (this.fov.value() < resolutionAndFrameRate.fov.value()) {
            return -1;
        }
        return 0;
    }
}
