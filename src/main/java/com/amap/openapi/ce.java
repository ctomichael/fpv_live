package com.amap.openapi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/* compiled from: OfflineRequest */
public class ce {
    public byte a;
    public int b;
    public List<Long> c;
    public List<String> d;
    public HashMap<String, String> e;
    public byte[] f;

    public ce(byte b2, List<Long> list, List<String> list2) {
        this.a = b2;
        this.c = list;
        this.d = list2;
    }

    public String toString() {
        return "OfflineRequest{mType=" + ((int) this.a) + ", mWifiList=" + this.c + ", mCellList=" + this.d + ", mHeaders=" + this.e + ", mBody=" + Arrays.toString(this.f) + '}';
    }
}
