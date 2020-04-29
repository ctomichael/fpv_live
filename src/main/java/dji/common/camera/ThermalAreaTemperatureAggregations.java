package dji.common.camera;

import android.graphics.Point;
import android.support.annotation.NonNull;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class ThermalAreaTemperatureAggregations {
    private float averageTemperature;
    private float maxTemperature;
    private Point maxTemperaturePosition;
    private float minTemperature;
    private Point minTemperaturePosition;

    public interface Callback {
        void onUpdate(@NonNull ThermalAreaTemperatureAggregations thermalAreaTemperatureAggregations);
    }

    public ThermalAreaTemperatureAggregations(float averageTemperature2, float minTemperature2, Point minTemperaturePosition2, float maxTemperature2, Point maxTemperaturePosition2) {
        this.averageTemperature = averageTemperature2;
        this.minTemperature = minTemperature2;
        this.maxTemperature = maxTemperature2;
        this.minTemperaturePosition = minTemperaturePosition2;
        this.maxTemperaturePosition = maxTemperaturePosition2;
    }

    public int hashCode() {
        int i;
        int i2 = 0;
        int floatToIntBits = ((Float.floatToIntBits(this.averageTemperature) * 31) + Float.floatToIntBits(this.averageTemperature)) * 31;
        if (this.minTemperaturePosition != null) {
            i = this.minTemperaturePosition.hashCode();
        } else {
            i = 0;
        }
        int floatToIntBits2 = (((floatToIntBits + i) * 31) + Float.floatToIntBits(this.averageTemperature)) * 31;
        if (this.maxTemperaturePosition != null) {
            i2 = this.maxTemperaturePosition.hashCode();
        }
        return floatToIntBits2 + i2;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ThermalAreaTemperatureAggregations)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (this.averageTemperature != ((ThermalAreaTemperatureAggregations) o).averageTemperature) {
            return false;
        }
        if (this.minTemperature != ((ThermalAreaTemperatureAggregations) o).minTemperature) {
            return false;
        }
        if (this.maxTemperature != ((ThermalAreaTemperatureAggregations) o).maxTemperature) {
            return false;
        }
        if (this.minTemperaturePosition == null || ((ThermalAreaTemperatureAggregations) o).minTemperaturePosition == null) {
            if (this.minTemperaturePosition == null || ((ThermalAreaTemperatureAggregations) o).minTemperaturePosition == null) {
                return false;
            }
        } else if (!this.minTemperaturePosition.equals(((ThermalAreaTemperatureAggregations) o).minTemperaturePosition)) {
            return false;
        }
        if (this.maxTemperaturePosition == null || ((ThermalAreaTemperatureAggregations) o).maxTemperaturePosition == null) {
            if (this.maxTemperaturePosition == null || ((ThermalAreaTemperatureAggregations) o).maxTemperaturePosition == null) {
                return false;
            }
        } else if (!this.maxTemperaturePosition.equals(((ThermalAreaTemperatureAggregations) o).maxTemperaturePosition)) {
            return false;
        }
        return true;
    }

    public float getAverageAreaTemperature() {
        return this.averageTemperature;
    }

    public float getMinAreaTemperature() {
        return this.minTemperature;
    }

    @NonNull
    public Point getMinTemperaturePoint() {
        return this.minTemperaturePosition;
    }

    public float getMaxAreaTemperature() {
        return this.maxTemperature;
    }

    @NonNull
    public Point getMaxTemperaturePoint() {
        return this.maxTemperaturePosition;
    }
}
