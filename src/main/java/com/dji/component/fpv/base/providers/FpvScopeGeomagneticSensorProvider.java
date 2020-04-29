package com.dji.component.fpv.base.providers;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.view.WindowManager;
import com.dji.component.fpv.base.filter.MedianFilter;
import dji.log.DJILog;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import java.util.Arrays;

public class FpvScopeGeomagneticSensorProvider implements DisplayManager.DisplayListener, FpvScopeProvider, LifecycleObserver {
    private static final String DJI_VENDOR = "DJI";
    private static final int FILTER_WINDOW = 35;
    private static final String TAG = "FpvScopeGeomagneticSensorProvider";
    private SensorEventListener mAccelerometerAndMagneticListener = new SensorEventListener() {
        /* class com.dji.component.fpv.base.providers.FpvScopeGeomagneticSensorProvider.AnonymousClass2 */
        float[] R = new float[9];
        float[] accelerometerValues = new float[3];
        float[] magneticValues = new float[3];

        /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
         method: ClspMth{java.util.Arrays.fill(float[], float):void}
         arg types: [float[], int]
         candidates:
          ClspMth{java.util.Arrays.fill(double[], double):void}
          ClspMth{java.util.Arrays.fill(byte[], byte):void}
          ClspMth{java.util.Arrays.fill(long[], long):void}
          ClspMth{java.util.Arrays.fill(boolean[], boolean):void}
          ClspMth{java.util.Arrays.fill(char[], char):void}
          ClspMth{java.util.Arrays.fill(short[], short):void}
          ClspMth{java.util.Arrays.fill(java.lang.Object[], java.lang.Object):void}
          ClspMth{java.util.Arrays.fill(int[], int):void}
          ClspMth{java.util.Arrays.fill(float[], float):void} */
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == 1) {
                this.accelerometerValues = (float[]) event.values.clone();
            } else if (event.sensor.getType() == 2) {
                this.magneticValues = (float[]) event.values.clone();
            }
            Arrays.fill(this.R, 0.0f);
            float[] values = new float[3];
            SensorManager.getRotationMatrix(this.R, null, this.accelerometerValues, this.magneticValues);
            SensorManager.getOrientation(this.R, values);
            FpvScopeGeomagneticSensorProvider.this.mYawAngleSubject.onNext(Float.valueOf(((Float) FpvScopeGeomagneticSensorProvider.this.mAccelerometerMedianFilter.process(Float.valueOf((float) Math.toDegrees((double) values[0])))).floatValue()));
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    /* access modifiers changed from: private */
    public MedianFilter<Float> mAccelerometerMedianFilter;
    private SensorEventListener mDeprecatedOrientationListener = new SensorEventListener() {
        /* class com.dji.component.fpv.base.providers.FpvScopeGeomagneticSensorProvider.AnonymousClass1 */

        public void onSensorChanged(SensorEvent event) {
            FpvScopeGeomagneticSensorProvider.this.mYawAngleSubject.onNext(Float.valueOf(event.values[0]));
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private DisplayManager mDisplayManager;
    private SensorManager mSensorManager;
    private Sensor mSensorOrientation;
    private BehaviorSubject<Integer> mSurfaceRotationSubject;
    private WindowManager mWindowManager;
    /* access modifiers changed from: private */
    public BehaviorSubject<Float> mYawAngleSubject = BehaviorSubject.createDefault(Float.valueOf(0.0f));

    public FpvScopeGeomagneticSensorProvider(Context context) {
        this.mSensorManager = (SensorManager) context.getSystemService("sensor");
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        this.mDisplayManager = (DisplayManager) context.getSystemService("display");
        this.mSurfaceRotationSubject = BehaviorSubject.createDefault(Integer.valueOf(getSurfaceRotation()));
        this.mAccelerometerMedianFilter = new MedianFilter<>(35, FpvScopeGeomagneticSensorProvider$$Lambda$0.$instance);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (this.mSensorManager != null) {
            if (this.mSensorOrientation == null) {
                this.mSensorOrientation = this.mSensorManager.getDefaultSensor(3);
            }
            if (this.mSensorOrientation != null) {
                DJILog.logWriteI(TAG, "got deprecated orientation sensor", new Object[0]);
                if (!"DJI".equals(this.mSensorOrientation.getVendor())) {
                    this.mSensorManager.registerListener(this.mDeprecatedOrientationListener, this.mSensorOrientation, 1);
                } else {
                    DJILog.logWriteI(TAG, "got dji device skip listening", new Object[0]);
                }
            } else {
                DJILog.logWriteI(TAG, "no deprecated sensor found, try to use magnetic and accelerometer", new Object[0]);
                Sensor magneticSensor = this.mSensorManager.getDefaultSensor(2);
                Sensor accelerometerSensor = this.mSensorManager.getDefaultSensor(1);
                if (magneticSensor == null || accelerometerSensor == null) {
                    DJILog.logWriteI(TAG, "sensor got failed, skip listening", new Object[0]);
                    DJILog.logWriteI(TAG, "magnetic = " + magneticSensor, new Object[0]);
                    DJILog.logWriteI(TAG, "accelerometer = " + accelerometerSensor, new Object[0]);
                } else {
                    this.mSensorManager.registerListener(this.mAccelerometerAndMagneticListener, magneticSensor, 2);
                    this.mSensorManager.registerListener(this.mAccelerometerAndMagneticListener, accelerometerSensor, 1);
                }
            }
        }
        if (this.mDisplayManager != null) {
            this.mDisplayManager.registerDisplayListener(this, new Handler());
        } else {
            DJILog.logWriteE(TAG, "display manager null", new Object[0]);
        }
        this.mAccelerometerMedianFilter.clear();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        if (this.mSensorManager != null) {
            if (this.mSensorOrientation == null) {
                this.mSensorManager.unregisterListener(this.mAccelerometerAndMagneticListener);
            } else if (!"DJI".equals(this.mSensorOrientation.getVendor())) {
                this.mSensorManager.unregisterListener(this.mDeprecatedOrientationListener);
            }
        }
        if (this.mDisplayManager != null) {
            this.mDisplayManager.unregisterDisplayListener(this);
        }
        this.mAccelerometerMedianFilter.clear();
    }

    public Observable<Float> getPhoneYawAngleObservable() {
        return this.mYawAngleSubject.map(FpvScopeGeomagneticSensorProvider$$Lambda$1.$instance);
    }

    static final /* synthetic */ Float lambda$getPhoneYawAngleObservable$0$FpvScopeGeomagneticSensorProvider(Float aFloat) throws Exception {
        if (aFloat.floatValue() > 180.0f) {
            return Float.valueOf(aFloat.floatValue() - 360.0f);
        }
        return aFloat;
    }

    public Observable<Integer> getSurfaceRotationObservable() {
        return this.mSurfaceRotationSubject.hide();
    }

    public float getPhoneYawAngle() {
        return this.mYawAngleSubject.getValue().floatValue();
    }

    public int getSurfaceRotation() {
        if (this.mWindowManager == null || this.mWindowManager.getDefaultDisplay() == null) {
            return 270;
        }
        switch (this.mWindowManager.getDefaultDisplay().getRotation()) {
            case 0:
                return 0;
            case 1:
                return 90;
            case 2:
                return 180;
            default:
                return 270;
        }
    }

    public void onDisplayAdded(int displayId) {
    }

    public void onDisplayRemoved(int displayId) {
    }

    public void onDisplayChanged(int displayId) {
        this.mSurfaceRotationSubject.onNext(Integer.valueOf(getSurfaceRotation()));
    }
}
