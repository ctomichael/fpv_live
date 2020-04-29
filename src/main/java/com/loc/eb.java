package com.loc;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import dji.midware.Lifecycle;

/* compiled from: AmapSensorManager */
public final class eb implements SensorEventListener {
    SensorManager a = null;
    Sensor b = null;
    Sensor c = null;
    Sensor d = null;
    public boolean e = false;
    public double f = 0.0d;
    public float g = 0.0f;
    Handler h = new Handler() {
        /* class com.loc.eb.AnonymousClass1 */
    };
    double i = 0.0d;
    double j = 0.0d;
    double k = 0.0d;
    double l = 0.0d;
    double[] m = new double[3];
    volatile double n = 0.0d;
    long o = 0;
    long p = 0;
    final int q = 100;
    final int r = 30;
    private Context s = null;
    private float t = 1013.25f;
    private float u = 0.0f;

    public eb(Context context) {
        try {
            this.s = context;
            if (this.a == null) {
                this.a = (SensorManager) this.s.getSystemService("sensor");
            }
            try {
                this.b = this.a.getDefaultSensor(6);
            } catch (Throwable th) {
            }
            try {
                this.c = this.a.getDefaultSensor(11);
            } catch (Throwable th2) {
            }
            try {
                this.d = this.a.getDefaultSensor(1);
            } catch (Throwable th3) {
            }
        } catch (Throwable th4) {
            es.a(th4, "AMapSensorManager", "<init>");
        }
    }

    public final void a() {
        if (this.a != null && !this.e) {
            this.e = true;
            try {
                if (this.b != null) {
                    this.a.registerListener(this, this.b, 3, this.h);
                }
            } catch (Throwable th) {
                es.a(th, "AMapSensorManager", "registerListener mPressure");
            }
            try {
                if (this.c != null) {
                    this.a.registerListener(this, this.c, 3, this.h);
                }
            } catch (Throwable th2) {
                es.a(th2, "AMapSensorManager", "registerListener mRotationVector");
            }
            try {
                if (this.d != null) {
                    this.a.registerListener(this, this.d, 3, this.h);
                }
            } catch (Throwable th3) {
                es.a(th3, "AMapSensorManager", "registerListener mAcceleroMeterVector");
            }
        }
    }

    public final void b() {
        if (this.a != null && this.e) {
            this.e = false;
            try {
                if (this.b != null) {
                    this.a.unregisterListener(this, this.b);
                }
            } catch (Throwable th) {
            }
            try {
                if (this.c != null) {
                    this.a.unregisterListener(this, this.c);
                }
            } catch (Throwable th2) {
            }
            try {
                if (this.d != null) {
                    this.a.unregisterListener(this, this.d);
                }
            } catch (Throwable th3) {
            }
        }
    }

    public final float c() {
        return this.u;
    }

    public final double d() {
        return this.l;
    }

    public final void e() {
        try {
            b();
            this.b = null;
            this.c = null;
            this.a = null;
            this.d = null;
            this.e = false;
        } catch (Throwable th) {
            es.a(th, "AMapSensorManager", Lifecycle.STATUS_DESTROY);
        }
    }

    public final void onAccuracyChanged(Sensor sensor, int i2) {
    }

    public final void onSensorChanged(SensorEvent sensorEvent) {
        float[] fArr;
        if (sensorEvent != null) {
            try {
                switch (sensorEvent.sensor.getType()) {
                    case 1:
                        try {
                            if (this.d != null) {
                                float[] fArr2 = (float[]) sensorEvent.values.clone();
                                this.m[0] = (this.m[0] * 0.800000011920929d) + ((double) (fArr2[0] * 0.19999999f));
                                this.m[1] = (this.m[1] * 0.800000011920929d) + ((double) (fArr2[1] * 0.19999999f));
                                this.m[2] = (this.m[2] * 0.800000011920929d) + ((double) (fArr2[2] * 0.19999999f));
                                this.i = ((double) fArr2[0]) - this.m[0];
                                this.j = ((double) fArr2[1]) - this.m[1];
                                this.k = ((double) fArr2[2]) - this.m[2];
                                long currentTimeMillis = System.currentTimeMillis();
                                if (currentTimeMillis - this.o >= 100) {
                                    double sqrt = Math.sqrt((this.i * this.i) + (this.j * this.j) + (this.k * this.k));
                                    this.p++;
                                    this.o = currentTimeMillis;
                                    this.n += sqrt;
                                    if (this.p >= 30) {
                                        this.l = this.n / ((double) this.p);
                                        this.n = 0.0d;
                                        this.p = 0;
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            return;
                        } catch (Throwable th) {
                            return;
                        }
                    case 6:
                        try {
                            if (this.b != null) {
                                float[] fArr3 = (float[]) sensorEvent.values.clone();
                                if (fArr3 != null) {
                                    this.g = fArr3[0];
                                }
                                if (fArr3 != null) {
                                    this.f = (double) fa.a(SensorManager.getAltitude(this.t, fArr3[0]));
                                    return;
                                }
                                return;
                            }
                            return;
                        } catch (Throwable th2) {
                            return;
                        }
                    case 11:
                        try {
                            if (this.c != null && (fArr = (float[]) sensorEvent.values.clone()) != null) {
                                float[] fArr4 = new float[9];
                                SensorManager.getRotationMatrixFromVector(fArr4, fArr);
                                float[] fArr5 = new float[3];
                                SensorManager.getOrientation(fArr4, fArr5);
                                this.u = (float) Math.toDegrees((double) fArr5[0]);
                                this.u = (float) Math.floor(this.u > 0.0f ? (double) this.u : (double) (this.u + 360.0f));
                                return;
                            }
                            return;
                        } catch (Throwable th3) {
                            return;
                        }
                    default:
                        return;
                }
            } catch (Throwable th4) {
            }
        }
    }
}
