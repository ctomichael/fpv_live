package com.loc;

import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.autonavi.aps.amapapi.model.AMapLocationServer;

/* compiled from: LocFilter */
public final class cu {
    AMapLocationServer a = null;
    long b = 0;
    long c = 0;
    int d = 0;
    long e = 0;
    AMapLocation f = null;
    long g = 0;
    private boolean h = true;

    private AMapLocationServer b(AMapLocationServer aMapLocationServer) {
        if (fa.a(aMapLocationServer)) {
            if (!this.h || !er.b(aMapLocationServer.getTime())) {
                aMapLocationServer.setLocationType(this.d);
            } else if (aMapLocationServer.getLocationType() == 5 || aMapLocationServer.getLocationType() == 6) {
                aMapLocationServer.setLocationType(4);
            }
        }
        return aMapLocationServer;
    }

    public final AMapLocation a(AMapLocation aMapLocation) {
        if (!fa.a(aMapLocation)) {
            return aMapLocation;
        }
        long c2 = fa.c() - this.g;
        this.g = fa.c();
        if (c2 > 5000) {
            return aMapLocation;
        }
        if (this.f == null) {
            this.f = aMapLocation;
            return aMapLocation;
        } else if (1 != this.f.getLocationType() && !"gps".equalsIgnoreCase(this.f.getProvider())) {
            this.f = aMapLocation;
            return aMapLocation;
        } else if (this.f.getAltitude() == aMapLocation.getAltitude() && this.f.getLongitude() == aMapLocation.getLongitude()) {
            this.f = aMapLocation;
            return aMapLocation;
        } else {
            long abs = Math.abs(aMapLocation.getTime() - this.f.getTime());
            if (30000 < abs) {
                this.f = aMapLocation;
                return aMapLocation;
            }
            if (fa.a(aMapLocation, this.f) > ((((float) abs) * (this.f.getSpeed() + aMapLocation.getSpeed())) / 2000.0f) + (2.0f * (this.f.getAccuracy() + aMapLocation.getAccuracy())) + 3000.0f) {
                return this.f;
            }
            this.f = aMapLocation;
            return aMapLocation;
        }
    }

    public final AMapLocationServer a(AMapLocationServer aMapLocationServer) {
        if (fa.c() - this.e > 30000) {
            this.a = aMapLocationServer;
            this.e = fa.c();
            return this.a;
        }
        this.e = fa.c();
        if (!fa.a(this.a) || !fa.a(aMapLocationServer)) {
            this.b = fa.c();
            this.a = aMapLocationServer;
            return this.a;
        } else if (aMapLocationServer.getTime() == this.a.getTime() && aMapLocationServer.getAccuracy() < 300.0f) {
            return aMapLocationServer;
        } else {
            if (aMapLocationServer.getProvider().equals("gps")) {
                this.b = fa.c();
                this.a = aMapLocationServer;
                return this.a;
            } else if (aMapLocationServer.c() != this.a.c()) {
                this.b = fa.c();
                this.a = aMapLocationServer;
                return this.a;
            } else if (aMapLocationServer.getBuildingId().equals(this.a.getBuildingId()) || TextUtils.isEmpty(aMapLocationServer.getBuildingId())) {
                this.d = aMapLocationServer.getLocationType();
                float a2 = fa.a(aMapLocationServer, this.a);
                float accuracy = this.a.getAccuracy();
                float accuracy2 = aMapLocationServer.getAccuracy();
                float f2 = accuracy2 - accuracy;
                long c2 = fa.c();
                long j = c2 - this.b;
                boolean z = accuracy <= 100.0f && accuracy2 > 299.0f;
                boolean z2 = accuracy > 299.0f && accuracy2 > 299.0f;
                if (z || z2) {
                    if (this.c == 0) {
                        this.c = c2;
                    } else if (c2 - this.c > 30000) {
                        this.b = c2;
                        this.a = aMapLocationServer;
                        this.c = 0;
                        return this.a;
                    }
                    this.a = b(this.a);
                    return this.a;
                } else if (accuracy2 >= 100.0f || accuracy <= 299.0f) {
                    if (accuracy2 <= 299.0f) {
                        this.c = 0;
                    }
                    if (a2 >= 10.0f || ((double) a2) <= 0.1d || accuracy2 <= 5.0f) {
                        if (f2 < 300.0f) {
                            this.b = fa.c();
                            this.a = aMapLocationServer;
                            return this.a;
                        } else if (j >= 30000) {
                            this.b = fa.c();
                            this.a = aMapLocationServer;
                            return this.a;
                        } else {
                            this.a = b(this.a);
                            return this.a;
                        }
                    } else if (f2 >= -300.0f) {
                        this.a = b(this.a);
                        return this.a;
                    } else if (accuracy / accuracy2 >= 2.0f) {
                        this.b = c2;
                        this.a = aMapLocationServer;
                        return this.a;
                    } else {
                        this.a = b(this.a);
                        return this.a;
                    }
                } else {
                    this.b = c2;
                    this.a = aMapLocationServer;
                    this.c = 0;
                    return this.a;
                }
            } else {
                this.b = fa.c();
                this.a = aMapLocationServer;
                return this.a;
            }
        }
    }

    public final void a() {
        this.a = null;
        this.b = 0;
        this.c = 0;
        this.f = null;
        this.g = 0;
    }

    public final void a(boolean z) {
        this.h = z;
    }
}
