package com.amap.openapi;

import com.loc.fd;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* compiled from: RespOfflineCellWiFi */
public final class ck extends fd {
    public static ck a(ByteBuffer byteBuffer) {
        return a(byteBuffer, new ck());
    }

    public static ck a(ByteBuffer byteBuffer, ck ckVar) {
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return ckVar.b(byteBuffer.getInt(byteBuffer.position()) + byteBuffer.position(), byteBuffer);
    }

    public final int a() {
        int c = c(4);
        if (c != 0) {
            return e(c);
        }
        return 0;
    }

    public final ci a(int i) {
        return a(new ci(), i);
    }

    public final ci a(ci ciVar, int i) {
        int c = c(4);
        if (c != 0) {
            return ciVar.b(d(f(c) + (i * 4)), this.c);
        }
        return null;
    }

    public final void a(int i, ByteBuffer byteBuffer) {
        this.b = i;
        this.c = byteBuffer;
    }

    public final int b() {
        int c = c(6);
        if (c != 0) {
            return e(c);
        }
        return 0;
    }

    public final ci b(int i) {
        return b(new ci(), i);
    }

    public final ci b(ci ciVar, int i) {
        int c = c(6);
        if (c != 0) {
            return ciVar.b(d(f(c) + (i * 4)), this.c);
        }
        return null;
    }

    public final ck b(int i, ByteBuffer byteBuffer) {
        a(i, byteBuffer);
        return this;
    }
}
