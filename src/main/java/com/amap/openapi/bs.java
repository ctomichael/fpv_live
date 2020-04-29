package com.amap.openapi;

import com.amap.location.common.model.AmapLoc;

/* compiled from: OffCellInfo */
public class bs {
    public boolean a;
    public int b;
    public int c;
    public int d;
    public int e;
    public String f;
    public long g;
    public boolean h = false;
    public AmapLoc i;

    public bs(boolean z, String str, long j, int i2, int i3, int i4, int i5) {
        this.a = z;
        this.f = str;
        this.g = j;
        this.b = i2;
        this.c = i3;
        this.d = i4;
        this.e = i5;
    }

    public String toString() {
        double d2 = 0.0d;
        StringBuilder append = new StringBuilder("{").append(this.a).append("@").append(this.f).append("@").append(this.b).append("@").append(this.c).append("@").append(this.e).append("@").append(this.h).append("@").append(this.i != null ? this.i.getLat() : 0.0d).append("@");
        if (this.i != null) {
            d2 = this.i.getLon();
        }
        return append.append(d2).append('}').toString();
    }
}
