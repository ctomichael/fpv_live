package com.amap.openapi;

import com.amap.location.common.model.AmapLoc;
import java.util.HashMap;

/* compiled from: OffWifiInfo */
public class bu {
    public int a = 0;
    public HashMap<Long, bt> b = new HashMap<>();
    public int c = 0;
    public StringBuilder d = new StringBuilder();
    public StringBuilder e = new StringBuilder();
    public AmapLoc f;

    public String toString() {
        double d2 = 0.0d;
        StringBuilder append = new StringBuilder("{").append(this.a).append("@").append(this.c).append("@").append(this.f != null ? this.f.getLat() : 0.0d).append("@");
        if (this.f != null) {
            d2 = this.f.getLon();
        }
        return append.append(d2).append('}').toString();
    }
}
