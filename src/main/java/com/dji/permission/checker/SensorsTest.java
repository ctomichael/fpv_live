package com.dji.permission.checker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.RequiresApi;

class SensorsTest implements PermissionTest {
    private static final SensorEventListener SENSOR_EVENT_LISTENER = new SensorEventListener() {
        /* class com.dji.permission.checker.SensorsTest.AnonymousClass1 */

        public void onSensorChanged(SensorEvent event) {
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private Context mContext;

    SensorsTest(Context context) {
        this.mContext = context;
    }

    @RequiresApi(api = 20)
    public boolean test() throws Throwable {
        SensorManager sensorManager = (SensorManager) this.mContext.getSystemService("sensor");
        Sensor heartRateSensor = sensorManager.getDefaultSensor(21);
        if (heartRateSensor == null) {
            return true;
        }
        sensorManager.registerListener(SENSOR_EVENT_LISTENER, heartRateSensor, 3);
        sensorManager.unregisterListener(SENSOR_EVENT_LISTENER);
        return true;
    }
}
