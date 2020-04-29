package com.loc;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/* compiled from: Table */
public class fd {
    public static final ThreadLocal<Charset> a = new ThreadLocal<Charset>() {
        /* class com.loc.fd.AnonymousClass2 */

        /* access modifiers changed from: protected */
        public final /* synthetic */ Object initialValue() {
            return Charset.forName("UTF-8");
        }
    };
    private static final ThreadLocal<CharsetDecoder> d = new ThreadLocal<CharsetDecoder>() {
        /* class com.loc.fd.AnonymousClass1 */

        /* access modifiers changed from: protected */
        public final /* synthetic */ Object initialValue() {
            return Charset.forName("UTF-8").newDecoder();
        }
    };
    private static final ThreadLocal<CharBuffer> e = new ThreadLocal<>();
    protected int b;
    protected ByteBuffer c;

    /* access modifiers changed from: protected */
    public int a(Integer num, Integer num2, ByteBuffer byteBuffer) {
        return 0;
    }

    /* access modifiers changed from: protected */
    public int c(int i) {
        int i2 = this.b - this.c.getInt(this.b);
        if (i < this.c.getShort(i2)) {
            return this.c.getShort(i2 + i);
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public int d(int i) {
        return this.c.getInt(i) + i;
    }

    /* access modifiers changed from: protected */
    public int e(int i) {
        int i2 = this.b + i;
        return this.c.getInt(i2 + this.c.getInt(i2));
    }

    /* access modifiers changed from: protected */
    public int f(int i) {
        int i2 = this.b + i;
        return i2 + this.c.getInt(i2) + 4;
    }
}
