package com.amap.openapi;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* compiled from: AbstractBuilder */
public abstract class cf {
    protected ch a = new ch(this.b);
    private ByteBuffer b;

    protected cf(int i) {
        this.b = ByteBuffer.allocate(i);
        this.b.order(ByteOrder.LITTLE_ENDIAN);
    }

    public cf a() {
        this.a.a(this.b);
        return this;
    }
}
