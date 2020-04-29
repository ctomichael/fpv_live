package dji.common.camera;

import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ThermalExternalSceneSettings {
    private float atmosphericTemp;
    private float atmosphericTransCoefficient;
    private float bckgrndTemp;
    private float sceneEmissivity;
    private float windowReflectedTemp;
    private float windowReflection;
    private float windowTemp;
    private float windowTransCoefficient;

    public interface Callback {
        void onUpdate(@NonNull ThermalExternalSceneSettings thermalExternalSceneSettings);
    }

    public ThermalExternalSceneSettings(float atmosphericTemp2, float atmosphericTransCoefficient2, float bckgrndTemp2, float sceneEmissivity2, float windowReflection2, float windowReflectedTemp2, float windowTemp2, float windowTransCoefficient2) {
        this.atmosphericTemp = atmosphericTemp2;
        this.atmosphericTransCoefficient = atmosphericTransCoefficient2;
        this.bckgrndTemp = bckgrndTemp2;
        this.sceneEmissivity = sceneEmissivity2;
        this.windowReflection = windowReflection2;
        this.windowReflectedTemp = windowReflectedTemp2;
        this.windowTemp = windowTemp2;
        this.windowTransCoefficient = windowTransCoefficient2;
    }

    public int hashCode() {
        return (((((((((((((Float.floatToIntBits(this.atmosphericTemp) * 31) + Float.floatToIntBits(this.atmosphericTransCoefficient)) * 31) + Float.floatToIntBits(this.bckgrndTemp)) * 31) + Float.floatToIntBits(this.sceneEmissivity)) * 31) + Float.floatToIntBits(this.windowReflection)) * 31) + Float.floatToIntBits(this.windowReflectedTemp)) * 31) + Float.floatToIntBits(this.windowTemp)) * 31) + Float.floatToIntBits(this.windowTransCoefficient);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ThermalExternalSceneSettings)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.atmosphericTemp == ((ThermalExternalSceneSettings) o).atmosphericTemp && this.atmosphericTransCoefficient == ((ThermalExternalSceneSettings) o).atmosphericTransCoefficient && this.bckgrndTemp == ((ThermalExternalSceneSettings) o).bckgrndTemp && this.sceneEmissivity == ((ThermalExternalSceneSettings) o).sceneEmissivity && this.windowReflection == ((ThermalExternalSceneSettings) o).windowReflection && this.windowReflectedTemp == ((ThermalExternalSceneSettings) o).windowReflectedTemp && this.windowTemp == ((ThermalExternalSceneSettings) o).windowTemp && this.windowTransCoefficient == ((ThermalExternalSceneSettings) o).windowTransCoefficient;
    }

    public ThermalExternalSceneSettings() {
    }

    public float getAtmosphericTemperature() {
        return this.atmosphericTemp;
    }

    public float getAtmosphericTransmissionCoefficient() {
        return this.atmosphericTransCoefficient;
    }

    public float getBackgroundTemperature() {
        return this.bckgrndTemp;
    }

    public float getSceneEmissivity() {
        return this.sceneEmissivity;
    }

    public float getWindowReflection() {
        return this.windowReflection;
    }

    public float getWindowReflectedTemperature() {
        return this.windowReflectedTemp;
    }

    public float getWindowTemperature() {
        return this.windowTemp;
    }

    public float getWindowTransmissionCoefficient() {
        return this.windowTransCoefficient;
    }
}
