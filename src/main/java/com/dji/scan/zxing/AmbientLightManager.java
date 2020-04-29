package com.dji.scan.zxing;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import com.dji.scan.qr.camera.CameraManager;
import com.dji.scan.qr.camera.CameraSettings;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public final class AmbientLightManager implements SensorEventListener {
    private static final float BRIGHT_ENOUGH_LUX = 450.0f;
    private static final float TOO_DARK_LUX = 45.0f;
    /* access modifiers changed from: private */
    public CameraManager cameraManager;
    private CameraSettings cameraSettings;
    private Context context;
    private Handler handler = new Handler();
    private Sensor lightSensor;

    public AmbientLightManager(Context context2, CameraManager cameraManager2, CameraSettings settings) {
        this.context = context2;
        this.cameraManager = cameraManager2;
        this.cameraSettings = settings;
    }

    public void start() {
        if (this.cameraSettings.isAutoTorchEnabled()) {
            SensorManager sensorManager = (SensorManager) this.context.getSystemService("sensor");
            this.lightSensor = sensorManager.getDefaultSensor(5);
            if (this.lightSensor != null) {
                sensorManager.registerListener(this, this.lightSensor, 3);
            }
        }
    }

    public void stop() {
        if (this.lightSensor != null) {
            ((SensorManager) this.context.getSystemService("sensor")).unregisterListener(this);
            this.lightSensor = null;
        }
    }

    private void setTorch(final boolean on) {
        this.handler.post(new Runnable() {
            /* class com.dji.scan.zxing.AmbientLightManager.AnonymousClass1 */

            public void run() {
                AmbientLightManager.this.cameraManager.setTorch(on);
            }
        });
    }

    public void onSensorChanged(SensorEvent sensorEvent) {
        float ambientLightLux = sensorEvent.values[0];
        if (this.cameraManager == null) {
            return;
        }
        if (ambientLightLux <= 45.0f) {
            setTorch(true);
        } else if (ambientLightLux >= BRIGHT_ENOUGH_LUX) {
            setTorch(false);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
