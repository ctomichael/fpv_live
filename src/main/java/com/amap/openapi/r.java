package com.amap.openapi;

/* compiled from: CellPart */
public class r<T> {
    public byte a;
    public byte b;
    public byte c;
    public short d = 0;
    public long e;
    public T f;

    public String toString() {
        return "CellPart{type=" + ((int) this.a) + ", isMain=" + ((int) this.b) + ", interfaceType=" + ((int) this.c) + ", freshness=" + ((int) this.d) + ", firstBuildTime=" + this.e + ", cellData=" + ((Object) this.f) + '}';
    }
}
