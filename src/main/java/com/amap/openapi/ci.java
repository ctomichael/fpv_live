package com.amap.openapi;

import com.loc.fd;
import java.nio.ByteBuffer;

/* compiled from: CellWiFiLoc */
public final class ci extends fd {
    public final long a() {
        int c = c(4);
        if (c != 0) {
            return this.c.getLong(c + this.b);
        }
        return 0;
    }

    public final void a(int i, ByteBuffer byteBuffer) {
        this.b = i;
        this.c = byteBuffer;
    }

    public final int b() {
        int c = c(6);
        if (c != 0) {
            return this.c.getInt(c + this.b);
        }
        return 0;
    }

    public final ci b(int i, ByteBuffer byteBuffer) {
        a(i, byteBuffer);
        return this;
    }

    public final int c() {
        int c = c(8);
        if (c != 0) {
            return this.c.getInt(c + this.b);
        }
        return 0;
    }

    public final short d() {
        int c = c(10);
        if (c != 0) {
            return this.c.getShort(c + this.b);
        }
        return 0;
    }

    public final byte e() {
        int c = c(12);
        if (c != 0) {
            return this.c.get(c + this.b);
        }
        return 0;
    }
}
