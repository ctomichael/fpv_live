package com.loc;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;

/* compiled from: FlatBufferBuilder */
public class fc {
    static final Charset c = Charset.forName("UTF-8");
    static final /* synthetic */ boolean p = (!fc.class.desiredAssertionStatus());
    ByteBuffer a;
    int b;
    int d;
    int[] e;
    int f;
    boolean g;
    boolean h;
    int i;
    int[] j;
    int k;
    int l;
    boolean m;
    CharsetEncoder n;
    ByteBuffer o;

    public fc() {
        this(1024);
    }

    public fc(int i2) {
        this.d = 1;
        this.e = null;
        this.f = 0;
        this.g = false;
        this.h = false;
        this.j = new int[16];
        this.k = 0;
        this.l = 0;
        this.m = false;
        this.n = c.newEncoder();
        i2 = i2 <= 0 ? 1 : i2;
        this.b = i2;
        this.a = a(i2);
    }

    public fc(ByteBuffer byteBuffer) {
        this.d = 1;
        this.e = null;
        this.f = 0;
        this.g = false;
        this.h = false;
        this.j = new int[16];
        this.k = 0;
        this.l = 0;
        this.m = false;
        this.n = c.newEncoder();
        a(byteBuffer);
    }

    static ByteBuffer a(int i2) {
        ByteBuffer allocate = ByteBuffer.allocate(i2);
        allocate.order(ByteOrder.LITTLE_ENDIAN);
        return allocate;
    }

    static ByteBuffer b(ByteBuffer byteBuffer) {
        int capacity = byteBuffer.capacity();
        if ((-1073741824 & capacity) != 0) {
            throw new AssertionError("FlatBuffers: cannot grow buffer beyond 2 gigabytes.");
        }
        int i2 = capacity << 1;
        byteBuffer.position(0);
        ByteBuffer a2 = a(i2);
        a2.position(i2 - capacity);
        a2.put(byteBuffer);
        return a2;
    }

    public int a() {
        return this.a.capacity() - this.b;
    }

    public int a(CharSequence charSequence) {
        int length = (int) (((float) charSequence.length()) * this.n.maxBytesPerChar());
        if (this.o == null || this.o.capacity() < length) {
            this.o = ByteBuffer.allocate(Math.max(128, length));
        }
        this.o.clear();
        CoderResult encode = this.n.encode(charSequence instanceof CharBuffer ? (CharBuffer) charSequence : CharBuffer.wrap(charSequence), this.o, true);
        if (encode.isError()) {
            try {
                encode.throwException();
            } catch (CharacterCodingException e2) {
                throw new Error(e2);
            }
        }
        this.o.flip();
        return c(this.o);
    }

    public fc a(ByteBuffer byteBuffer) {
        this.a = byteBuffer;
        this.a.clear();
        this.a.order(ByteOrder.LITTLE_ENDIAN);
        this.d = 1;
        this.b = this.a.capacity();
        this.f = 0;
        this.g = false;
        this.h = false;
        this.i = 0;
        this.k = 0;
        this.l = 0;
        return this;
    }

    public void a(byte b2) {
        ByteBuffer byteBuffer = this.a;
        int i2 = this.b - 1;
        this.b = i2;
        byteBuffer.put(i2, b2);
    }

    public void a(int i2, byte b2, int i3) {
        if (this.m || b2 != i3) {
            b(b2);
            g(i2);
        }
    }

    public void a(int i2, int i3) {
        if (i2 > this.d) {
            this.d = i2;
        }
        int capacity = ((((this.a.capacity() - this.b) + i3) ^ -1) + 1) & (i2 - 1);
        while (this.b < capacity + i2 + i3) {
            int capacity2 = this.a.capacity();
            this.a = b(this.a);
            this.b = (this.a.capacity() - capacity2) + this.b;
        }
        b(capacity);
    }

    public void a(int i2, int i3, int i4) {
        d();
        this.l = i3;
        a(4, i2 * i3);
        a(i4, i2 * i3);
        this.g = true;
    }

    public void a(int i2, long j2, long j3) {
        if (this.m || j2 != j3) {
            b(j2);
            g(i2);
        }
    }

    public void a(int i2, short s, int i3) {
        if (this.m || s != i3) {
            b(s);
            g(i2);
        }
    }

    public void a(int i2, boolean z, boolean z2) {
        if (this.m || z != z2) {
            b(z);
            g(i2);
        }
    }

    public void a(long j2) {
        ByteBuffer byteBuffer = this.a;
        int i2 = this.b - 8;
        this.b = i2;
        byteBuffer.putLong(i2, j2);
    }

    public void a(short s) {
        ByteBuffer byteBuffer = this.a;
        int i2 = this.b - 2;
        this.b = i2;
        byteBuffer.putShort(i2, s);
    }

    public void a(boolean z) {
        ByteBuffer byteBuffer = this.a;
        int i2 = this.b - 1;
        this.b = i2;
        byteBuffer.put(i2, (byte) (z ? 1 : 0));
    }

    public int b() {
        if (!this.g) {
            throw new AssertionError("FlatBuffers: endVector called without startVector");
        }
        this.g = false;
        c(this.l);
        return a();
    }

    public void b(byte b2) {
        a(1, 0);
        a(b2);
    }

    public void b(int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            ByteBuffer byteBuffer = this.a;
            int i4 = this.b - 1;
            this.b = i4;
            byteBuffer.put(i4, (byte) 0);
        }
    }

    public void b(int i2, int i3, int i4) {
        if (this.m || i3 != i4) {
            d(i3);
            g(i2);
        }
    }

    public void b(long j2) {
        a(8, 0);
        a(j2);
    }

    public void b(short s) {
        a(2, 0);
        a(s);
    }

    public void b(boolean z) {
        a(1, 0);
        a(z);
    }

    public byte[] b(int i2, int i3) {
        c();
        byte[] bArr = new byte[i3];
        this.a.position(i2);
        this.a.get(bArr);
        return bArr;
    }

    public int c(ByteBuffer byteBuffer) {
        int remaining = byteBuffer.remaining();
        b((byte) 0);
        a(1, remaining, 1);
        ByteBuffer byteBuffer2 = this.a;
        int i2 = this.b - remaining;
        this.b = i2;
        byteBuffer2.position(i2);
        this.a.put(byteBuffer);
        return b();
    }

    public void c() {
        if (!this.h) {
            throw new AssertionError("FlatBuffers: you can only access the serialized buffer after it has been finished by FlatBufferBuilder.finish().");
        }
    }

    public void c(int i2) {
        ByteBuffer byteBuffer = this.a;
        int i3 = this.b - 4;
        this.b = i3;
        byteBuffer.putInt(i3, i2);
    }

    public void c(int i2, int i3, int i4) {
        if (this.m || i3 != i4) {
            e(i3);
            g(i2);
        }
    }

    public void d() {
        if (this.g) {
            throw new AssertionError("FlatBuffers: object serialization must not be nested.");
        }
    }

    public void d(int i2) {
        a(4, 0);
        c(i2);
    }

    public int e() {
        int i2;
        if (this.e == null || !this.g) {
            throw new AssertionError("FlatBuffers: endObject called without startObject");
        }
        d(0);
        int a2 = a();
        for (int i3 = this.f - 1; i3 >= 0; i3--) {
            b((short) (this.e[i3] != 0 ? a2 - this.e[i3] : 0));
        }
        b((short) (a2 - this.i));
        b((short) ((this.f + 2) * 2));
        int i4 = 0;
        loop1:
        while (true) {
            if (i4 >= this.k) {
                i2 = 0;
                break;
            }
            int capacity = this.a.capacity() - this.j[i4];
            int i5 = this.b;
            short s = this.a.getShort(capacity);
            if (s == this.a.getShort(i5)) {
                int i6 = 2;
                while (i6 < s) {
                    if (this.a.getShort(capacity + i6) == this.a.getShort(i5 + i6)) {
                        i6 += 2;
                    }
                }
                i2 = this.j[i4];
                break loop1;
            }
            i4++;
        }
        if (i2 != 0) {
            this.b = this.a.capacity() - a2;
            this.a.putInt(this.b, i2 - a2);
        } else {
            if (this.k == this.j.length) {
                this.j = Arrays.copyOf(this.j, this.k * 2);
            }
            int[] iArr = this.j;
            int i7 = this.k;
            this.k = i7 + 1;
            iArr[i7] = a();
            this.a.putInt(this.a.capacity() - a2, a() - a2);
        }
        this.g = false;
        return a2;
    }

    public void e(int i2) {
        a(4, 0);
        if (p || i2 <= a()) {
            c((a() - i2) + 4);
            return;
        }
        throw new AssertionError();
    }

    public void f(int i2) {
        d();
        if (this.e == null || this.e.length < i2) {
            this.e = new int[i2];
        }
        this.f = i2;
        Arrays.fill(this.e, 0, this.f, 0);
        this.g = true;
        this.i = a();
    }

    public byte[] f() {
        return b(this.b, this.a.capacity() - this.b);
    }

    public void g(int i2) {
        this.e[i2] = a();
    }

    public void h(int i2) {
        a(this.d, 4);
        e(i2);
        this.a.position(this.b);
        this.h = true;
    }
}
