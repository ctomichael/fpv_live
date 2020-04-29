package dji.common.camera;

import android.support.annotation.NonNull;
import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ExposureSettings {
    private SettingsDefinitions.Aperture aperture;
    private SettingsDefinitions.ExposureCompensation exposureCompensation;
    private int iso;
    private SettingsDefinitions.ShutterSpeed shutterSpeed;

    public interface Callback {
        void onUpdate(@NonNull ExposureSettings exposureSettings);
    }

    public ExposureSettings(SettingsDefinitions.Aperture aperture2, SettingsDefinitions.ShutterSpeed shutterSpeed2, int iso2, SettingsDefinitions.ExposureCompensation exposureCompensation2) {
        this.aperture = aperture2;
        this.shutterSpeed = shutterSpeed2;
        this.iso = iso2;
        this.exposureCompensation = exposureCompensation2;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ExposureSettings)) {
            return false;
        }
        ExposureSettings exposureSetting = (ExposureSettings) o;
        if (this.aperture == exposureSetting.aperture && this.shutterSpeed == exposureSetting.shutterSpeed && this.iso == exposureSetting.iso && this.exposureCompensation == exposureSetting.exposureCompensation) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.aperture != null) {
            result = this.aperture.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.shutterSpeed != null) {
            i = this.shutterSpeed.hashCode();
        } else {
            i = 0;
        }
        int i4 = (((i3 + i) * 31) + this.iso) * 31;
        if (this.exposureCompensation != null) {
            i2 = this.exposureCompensation.hashCode();
        }
        return i4 + i2;
    }

    public SettingsDefinitions.Aperture getAperture() {
        return this.aperture;
    }

    public SettingsDefinitions.ShutterSpeed getShutterSpeed() {
        return this.shutterSpeed;
    }

    public int getISO() {
        return this.iso;
    }

    public SettingsDefinitions.ExposureCompensation getExposureCompensation() {
        return this.exposureCompensation;
    }
}
