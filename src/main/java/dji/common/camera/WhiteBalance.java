package dji.common.camera;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import dji.common.camera.SettingsDefinitions;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class WhiteBalance {
    private final int colorTemperature;
    private final SettingsDefinitions.WhiteBalancePreset whiteBalancePreset;

    public WhiteBalance(@NonNull SettingsDefinitions.WhiteBalancePreset preset) {
        this.whiteBalancePreset = preset;
        this.colorTemperature = 0;
    }

    public WhiteBalance(@NonNull SettingsDefinitions.WhiteBalancePreset preset, @IntRange(from = 20, to = 100) int temperature) {
        this.whiteBalancePreset = preset;
        this.colorTemperature = temperature;
    }

    public SettingsDefinitions.WhiteBalancePreset getWhiteBalancePreset() {
        return this.whiteBalancePreset;
    }

    public int getColorTemperature() {
        return this.colorTemperature;
    }

    public boolean equals(Object o) {
        boolean ret = super.equals(o);
        if (ret || !(o instanceof WhiteBalance)) {
            return ret;
        }
        WhiteBalance tmp = (WhiteBalance) o;
        return this.colorTemperature == tmp.colorTemperature && this.whiteBalancePreset.value() == tmp.whiteBalancePreset.value();
    }

    public int hashCode() {
        return ((this.whiteBalancePreset != null ? this.whiteBalancePreset.hashCode() : 0) * 31) + this.colorTemperature;
    }
}
