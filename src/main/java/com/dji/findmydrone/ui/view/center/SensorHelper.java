package com.dji.findmydrone.ui.view.center;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import dji.fieldAnnotation.EXClassNullAway;
import org.greenrobot.eventbus.EventBus;

@EXClassNullAway
public class SensorHelper implements SensorEventListener {
    private float[] R = new float[9];
    private Context ctx;
    private OrientationInfo object = new OrientationInfo();
    private Sensor sensor;
    private SensorManager sm = null;
    private float[] values = new float[3];

    public static class OrientationInfo {
        public float angradX;
        public float angradY;
        public float angradZ;
        public int degreeZ;
    }

    public SensorHelper(Context context) {
        this.ctx = context.getApplicationContext();
        this.sm = (SensorManager) this.ctx.getSystemService("sensor");
        this.sensor = this.sm.getDefaultSensor(3);
    }

    public void disable() {
        this.sm.unregisterListener(this, this.sensor);
    }

    public void enable() {
        this.sm.registerListener(this, this.sensor, 2);
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 3) {
            this.object.degreeZ = (int) event.values[0];
            EventBus.getDefault().post(this.object);
        }
        if (event.sensor.getType() == 11) {
            getRotationMatrixFromVector(this.R, event.values);
            SensorManager.getOrientation(this.R, this.values);
            this.object.degreeZ = (int) Math.toDegrees((double) this.values[0]);
            EventBus.getDefault().post(this.object);
        }
    }

    private float normalizeDegree(float degree) {
        return (720.0f + degree) % 360.0f;
    }

    public static int toDegrees(float angrad) {
        return (int) angrad;
    }

    public void onAccuracyChanged(Sensor sensor2, int accuracy) {
    }

    public static void getRotationMatrixFromVector(float[] R2, float[] rotationVector) {
        float q0;
        float q1 = rotationVector[0];
        float q2 = rotationVector[1];
        float q3 = rotationVector[2];
        if (rotationVector.length == 4) {
            q0 = rotationVector[3];
        } else {
            float q02 = ((1.0f - (q1 * q1)) - (q2 * q2)) - (q3 * q3);
            q0 = q02 > 0.0f ? (float) Math.sqrt((double) q02) : 0.0f;
        }
        float sq_q1 = 2.0f * q1 * q1;
        float sq_q2 = 2.0f * q2 * q2;
        float sq_q3 = 2.0f * q3 * q3;
        float q1_q2 = 2.0f * q1 * q2;
        float q3_q0 = 2.0f * q3 * q0;
        float q1_q3 = 2.0f * q1 * q3;
        float q2_q0 = 2.0f * q2 * q0;
        float q2_q3 = 2.0f * q2 * q3;
        float q1_q0 = 2.0f * q1 * q0;
        if (R2.length == 9) {
            R2[0] = (1.0f - sq_q2) - sq_q3;
            R2[1] = q1_q2 - q3_q0;
            R2[2] = q1_q3 + q2_q0;
            R2[3] = q1_q2 + q3_q0;
            R2[4] = (1.0f - sq_q1) - sq_q3;
            R2[5] = q2_q3 - q1_q0;
            R2[6] = q1_q3 - q2_q0;
            R2[7] = q2_q3 + q1_q0;
            R2[8] = (1.0f - sq_q1) - sq_q2;
        } else if (R2.length == 16) {
            R2[0] = (1.0f - sq_q2) - sq_q3;
            R2[1] = q1_q2 - q3_q0;
            R2[2] = q1_q3 + q2_q0;
            R2[3] = 0.0f;
            R2[4] = q1_q2 + q3_q0;
            R2[5] = (1.0f - sq_q1) - sq_q3;
            R2[6] = q2_q3 - q1_q0;
            R2[7] = 0.0f;
            R2[8] = q1_q3 - q2_q0;
            R2[9] = q2_q3 + q1_q0;
            R2[10] = (1.0f - sq_q1) - sq_q2;
            R2[11] = 0.0f;
            R2[14] = 0.0f;
            R2[13] = 0.0f;
            R2[12] = 0.0f;
            R2[15] = 1.0f;
        }
    }

    public static int getScreenOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == 1) {
            if (rotation == 0 || rotation == 3) {
                return 1;
            }
            return 9;
        } else if (orientation != 2) {
            return -1;
        } else {
            if (rotation == 0 || rotation == 1) {
                return 0;
            }
            return 8;
        }
    }
}
