package com.mapbox.mapboxsdk.location;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import com.mapbox.mapboxsdk.log.Logger;
import java.util.ArrayList;
import java.util.List;

class LocationComponentCompassEngine implements CompassEngine, SensorEventListener {
    private static final float ALPHA = 0.45f;
    static final int SENSOR_DELAY_MICROS = 100000;
    private static final String TAG = "Mbgl-LocationComponentCompassEngine";
    private final List<CompassListener> compassListeners = new ArrayList();
    @Nullable
    private Sensor compassSensor;
    private long compassUpdateNextTimestamp;
    @Nullable
    private Sensor gravitySensor;
    @Nullable
    private float[] gravityValues = new float[3];
    private int lastAccuracySensorStatus;
    private float lastHeading;
    @Nullable
    private Sensor magneticFieldSensor;
    @Nullable
    private float[] magneticValues = new float[3];
    @NonNull
    private float[] rotationMatrix = new float[9];
    private float[] rotationVectorValue;
    @NonNull
    private final SensorManager sensorManager;
    @NonNull
    private float[] truncatedRotationVectorValue = new float[4];
    @NonNull
    private final WindowManager windowManager;

    LocationComponentCompassEngine(@NonNull WindowManager windowManager2, @NonNull SensorManager sensorManager2) {
        this.windowManager = windowManager2;
        this.sensorManager = sensorManager2;
        this.compassSensor = sensorManager2.getDefaultSensor(11);
        if (this.compassSensor != null) {
            return;
        }
        if (isGyroscopeAvailable()) {
            Logger.d(TAG, "Rotation vector sensor not supported on device, falling back to orientation.");
            this.compassSensor = sensorManager2.getDefaultSensor(3);
            return;
        }
        Logger.d(TAG, "Rotation vector sensor not supported on device, falling back to accelerometer and magnetic field.");
        this.gravitySensor = sensorManager2.getDefaultSensor(1);
        this.magneticFieldSensor = sensorManager2.getDefaultSensor(2);
    }

    public void addCompassListener(@NonNull CompassListener compassListener) {
        if (this.compassListeners.isEmpty()) {
            registerSensorListeners();
        }
        this.compassListeners.add(compassListener);
    }

    public void removeCompassListener(@NonNull CompassListener compassListener) {
        this.compassListeners.remove(compassListener);
        if (this.compassListeners.isEmpty()) {
            unregisterSensorListeners();
        }
    }

    public int getLastAccuracySensorStatus() {
        return this.lastAccuracySensorStatus;
    }

    public float getLastHeading() {
        return this.lastHeading;
    }

    public void onSensorChanged(@NonNull SensorEvent event) {
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime >= this.compassUpdateNextTimestamp) {
            if (this.lastAccuracySensorStatus == 0) {
                Logger.d(TAG, "Compass sensor is unreliable, device calibration is needed.");
                return;
            }
            if (event.sensor.getType() == 11) {
                this.rotationVectorValue = getRotationVectorFromSensorEvent(event);
                updateOrientation();
            } else if (event.sensor.getType() == 3) {
                notifyCompassChangeListeners((event.values[0] + 360.0f) % 360.0f);
            } else if (event.sensor.getType() == 1) {
                this.gravityValues = lowPassFilter(getRotationVectorFromSensorEvent(event), this.gravityValues);
                updateOrientation();
            } else if (event.sensor.getType() == 2) {
                this.magneticValues = lowPassFilter(getRotationVectorFromSensorEvent(event), this.magneticValues);
                updateOrientation();
            }
            this.compassUpdateNextTimestamp = 500 + currentTime;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (this.lastAccuracySensorStatus != accuracy) {
            for (CompassListener compassListener : this.compassListeners) {
                compassListener.onCompassAccuracyChange(accuracy);
            }
            this.lastAccuracySensorStatus = accuracy;
        }
    }

    private boolean isGyroscopeAvailable() {
        return this.sensorManager.getDefaultSensor(4) != null;
    }

    private void updateOrientation() {
        int worldAxisForDeviceAxisX;
        int worldAxisForDeviceAxisY;
        if (this.rotationVectorValue != null) {
            SensorManager.getRotationMatrixFromVector(this.rotationMatrix, this.rotationVectorValue);
        } else {
            SensorManager.getRotationMatrix(this.rotationMatrix, null, this.gravityValues, this.magneticValues);
        }
        switch (this.windowManager.getDefaultDisplay().getRotation()) {
            case 1:
                worldAxisForDeviceAxisX = 3;
                worldAxisForDeviceAxisY = 129;
                break;
            case 2:
                worldAxisForDeviceAxisX = 129;
                worldAxisForDeviceAxisY = 131;
                break;
            case 3:
                worldAxisForDeviceAxisX = 131;
                worldAxisForDeviceAxisY = 1;
                break;
            default:
                worldAxisForDeviceAxisX = 1;
                worldAxisForDeviceAxisY = 3;
                break;
        }
        float[] adjustedRotationMatrix = new float[9];
        SensorManager.remapCoordinateSystem(this.rotationMatrix, worldAxisForDeviceAxisX, worldAxisForDeviceAxisY, adjustedRotationMatrix);
        float[] orientation = new float[3];
        SensorManager.getOrientation(adjustedRotationMatrix, orientation);
        notifyCompassChangeListeners((float) Math.toDegrees((double) orientation[0]));
    }

    private void notifyCompassChangeListeners(float heading) {
        for (CompassListener compassListener : this.compassListeners) {
            compassListener.onCompassChanged(heading);
        }
        this.lastHeading = heading;
    }

    private void registerSensorListeners() {
        if (isCompassSensorAvailable()) {
            this.sensorManager.registerListener(this, this.compassSensor, (int) SENSOR_DELAY_MICROS);
            return;
        }
        this.sensorManager.registerListener(this, this.gravitySensor, (int) SENSOR_DELAY_MICROS);
        this.sensorManager.registerListener(this, this.magneticFieldSensor, (int) SENSOR_DELAY_MICROS);
    }

    private void unregisterSensorListeners() {
        if (isCompassSensorAvailable()) {
            this.sensorManager.unregisterListener(this, this.compassSensor);
            return;
        }
        this.sensorManager.unregisterListener(this, this.gravitySensor);
        this.sensorManager.unregisterListener(this, this.magneticFieldSensor);
    }

    private boolean isCompassSensorAvailable() {
        return this.compassSensor != null;
    }

    @NonNull
    private float[] lowPassFilter(@NonNull float[] newValues, @Nullable float[] smoothedValues) {
        if (smoothedValues == null) {
            return newValues;
        }
        for (int i = 0; i < newValues.length; i++) {
            smoothedValues[i] = smoothedValues[i] + (ALPHA * (newValues[i] - smoothedValues[i]));
        }
        return smoothedValues;
    }

    @NonNull
    private float[] getRotationVectorFromSensorEvent(@NonNull SensorEvent event) {
        if (event.values.length <= 4) {
            return event.values;
        }
        System.arraycopy(event.values, 0, this.truncatedRotationVectorValue, 0, 4);
        return this.truncatedRotationVectorValue;
    }
}
