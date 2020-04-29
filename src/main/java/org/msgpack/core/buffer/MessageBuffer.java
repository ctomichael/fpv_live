package org.msgpack.core.buffer;

import java.lang.reflect.Constructor;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import org.msgpack.core.Preconditions;
import sun.misc.Unsafe;

public class MessageBuffer {
    static final /* synthetic */ boolean $assertionsDisabled;
    static final int ARRAY_BYTE_BASE_OFFSET;
    private static final String BIGENDIAN_MESSAGE_BUFFER = "org.msgpack.core.buffer.MessageBufferBE";
    private static final String DEFAULT_MESSAGE_BUFFER = "org.msgpack.core.buffer.MessageBuffer";
    private static final String UNIVERSAL_MESSAGE_BUFFER = "org.msgpack.core.buffer.MessageBufferU";
    static final boolean isUniversalBuffer;
    private static final Constructor<?> mbArrConstructor;
    static final Unsafe unsafe;
    protected final long address;
    protected final Object base;
    protected final int size;

    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00ec, code lost:
        r6 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x0193, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0194, code lost:
        r6 = r0;
        r7 = 16;
        r8 = false;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:101:0x01a1  */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x01c5  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0047  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0067 A[Catch:{ Exception -> 0x01f6, all -> 0x0193 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0083  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00e8  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x00ef  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0121  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x012d  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x015d  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0171  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x0193 A[ExcHandler: all (r0v15 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:4:0x0016] */
    static {
        /*
            r5 = 0
            r2 = 0
            r1 = 1
            java.lang.Class<org.msgpack.core.buffer.MessageBuffer> r0 = org.msgpack.core.buffer.MessageBuffer.class
            boolean r0 = r0.desiredAssertionStatus()
            if (r0 != 0) goto L_0x00d9
            r0 = r1
        L_0x000c:
            org.msgpack.core.buffer.MessageBuffer.$assertionsDisabled = r0
            r4 = 16
            java.lang.String r0 = "java.specification.version"
            java.lang.String r3 = ""
            java.lang.String r0 = java.lang.System.getProperty(r0, r3)     // Catch:{ Exception -> 0x01f6, all -> 0x0193 }
            r3 = 46
            int r3 = r0.indexOf(r3)     // Catch:{ Exception -> 0x01f6, all -> 0x0193 }
            r6 = -1
            if (r3 == r6) goto L_0x00e5
            r6 = 0
            java.lang.String r6 = r0.substring(r6, r3)     // Catch:{ NumberFormatException -> 0x00df }
            int r6 = java.lang.Integer.parseInt(r6)     // Catch:{ NumberFormatException -> 0x00df }
            int r3 = r3 + 1
            java.lang.String r0 = r0.substring(r3)     // Catch:{ NumberFormatException -> 0x00df }
            int r0 = java.lang.Integer.parseInt(r0)     // Catch:{ NumberFormatException -> 0x00df }
            if (r6 > r1) goto L_0x003d
            if (r6 != r1) goto L_0x00dc
            r3 = 7
            if (r0 < r3) goto L_0x00dc
        L_0x003d:
            r0 = r1
        L_0x003e:
            java.lang.String r3 = "sun.misc.Unsafe"
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch:{ Exception -> 0x00eb, all -> 0x0193 }
            if (r3 == 0) goto L_0x00e8
            r3 = r1
        L_0x0048:
            r6 = r3
        L_0x0049:
            java.lang.String r3 = "java.runtime.name"
            java.lang.String r7 = ""
            java.lang.String r3 = java.lang.System.getProperty(r3, r7)     // Catch:{ Exception -> 0x01f6, all -> 0x0193 }
            java.lang.String r3 = r3.toLowerCase()     // Catch:{ Exception -> 0x01f6, all -> 0x0193 }
            java.lang.String r7 = "android"
            boolean r7 = r3.contains(r7)     // Catch:{ Exception -> 0x01f6, all -> 0x0193 }
            java.lang.String r3 = "com.google.appengine.runtime.version"
            java.lang.String r3 = java.lang.System.getProperty(r3)     // Catch:{ Exception -> 0x01f6, all -> 0x0193 }
            if (r3 == 0) goto L_0x00ef
            r3 = r1
        L_0x0068:
            java.lang.String r8 = "msgpack.universal-buffer"
            java.lang.String r9 = "false"
            java.lang.String r8 = java.lang.System.getProperty(r8, r9)     // Catch:{ Exception -> 0x01f6, all -> 0x0193 }
            boolean r8 = java.lang.Boolean.parseBoolean(r8)     // Catch:{ Exception -> 0x01f6, all -> 0x0193 }
            if (r8 != 0) goto L_0x0080
            if (r7 != 0) goto L_0x0080
            if (r3 != 0) goto L_0x0080
            if (r0 == 0) goto L_0x0080
            if (r6 != 0) goto L_0x00f2
        L_0x0080:
            r8 = r1
        L_0x0081:
            if (r8 != 0) goto L_0x0121
            java.lang.Class<sun.misc.Unsafe> r0 = sun.misc.Unsafe.class
            java.lang.String r3 = "theUnsafe"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r3)     // Catch:{ Exception -> 0x01fc, all -> 0x01e5 }
            r3 = 1
            r0.setAccessible(r3)     // Catch:{ Exception -> 0x01fc, all -> 0x01e5 }
            r3 = 0
            java.lang.Object r0 = r0.get(r3)     // Catch:{ Exception -> 0x01fc, all -> 0x01e5 }
            sun.misc.Unsafe r0 = (sun.misc.Unsafe) r0     // Catch:{ Exception -> 0x01fc, all -> 0x01e5 }
            if (r0 != 0) goto L_0x00f4
            java.lang.RuntimeException r3 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x00a2, all -> 0x01e9 }
            java.lang.String r5 = "Unsafe is unavailable"
            r3.<init>(r5)     // Catch:{ Exception -> 0x00a2, all -> 0x01e9 }
            throw r3     // Catch:{ Exception -> 0x00a2, all -> 0x01e9 }
        L_0x00a2:
            r3 = move-exception
            r6 = r3
            r7 = r4
            r5 = r0
        L_0x00a6:
            java.io.PrintStream r0 = java.lang.System.err     // Catch:{ all -> 0x01f3 }
            r6.printStackTrace(r0)     // Catch:{ all -> 0x01f3 }
            org.msgpack.core.buffer.MessageBuffer.unsafe = r5
            org.msgpack.core.buffer.MessageBuffer.ARRAY_BYTE_BASE_OFFSET = r7
            org.msgpack.core.buffer.MessageBuffer.isUniversalBuffer = r1
            boolean r0 = org.msgpack.core.buffer.MessageBuffer.isUniversalBuffer
            if (r0 == 0) goto L_0x0171
            java.lang.String r0 = "org.msgpack.core.buffer.MessageBufferU"
        L_0x00b8:
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Exception -> 0x0187 }
            r1 = 3
            java.lang.Class[] r1 = new java.lang.Class[r1]     // Catch:{ Exception -> 0x0187 }
            r2 = 0
            java.lang.Class<byte[]> r3 = byte[].class
            r1[r2] = r3     // Catch:{ Exception -> 0x0187 }
            r2 = 1
            java.lang.Class r3 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x0187 }
            r1[r2] = r3     // Catch:{ Exception -> 0x0187 }
            r2 = 2
            java.lang.Class r3 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x0187 }
            r1[r2] = r3     // Catch:{ Exception -> 0x0187 }
            java.lang.reflect.Constructor r0 = r0.getDeclaredConstructor(r1)     // Catch:{ Exception -> 0x0187 }
            r1 = 1
            r0.setAccessible(r1)     // Catch:{ Exception -> 0x0187 }
            org.msgpack.core.buffer.MessageBuffer.mbArrConstructor = r0     // Catch:{ Exception -> 0x0187 }
        L_0x00d8:
            return
        L_0x00d9:
            r0 = r2
            goto L_0x000c
        L_0x00dc:
            r0 = r2
            goto L_0x003e
        L_0x00df:
            r0 = move-exception
            java.io.PrintStream r3 = java.lang.System.err     // Catch:{ Exception -> 0x01f6, all -> 0x0193 }
            r0.printStackTrace(r3)     // Catch:{ Exception -> 0x01f6, all -> 0x0193 }
        L_0x00e5:
            r0 = r2
            goto L_0x003e
        L_0x00e8:
            r3 = r2
            goto L_0x0048
        L_0x00eb:
            r3 = move-exception
            r6 = r2
            goto L_0x0049
        L_0x00ef:
            r3 = r2
            goto L_0x0068
        L_0x00f2:
            r8 = r2
            goto L_0x0081
        L_0x00f4:
            java.lang.Class<byte[]> r3 = byte[].class
            int r3 = r0.arrayBaseOffset(r3)     // Catch:{ Exception -> 0x00a2, all -> 0x01e9 }
            java.lang.Class<byte[]> r4 = byte[].class
            int r4 = r0.arrayIndexScale(r4)     // Catch:{ Exception -> 0x011c, all -> 0x01ee }
            if (r4 == r1) goto L_0x0123
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException     // Catch:{ Exception -> 0x011c, all -> 0x01ee }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x011c, all -> 0x01ee }
            r6.<init>()     // Catch:{ Exception -> 0x011c, all -> 0x01ee }
            java.lang.String r7 = "Byte array index scale must be 1, but is "
            java.lang.StringBuilder r6 = r6.append(r7)     // Catch:{ Exception -> 0x011c, all -> 0x01ee }
            java.lang.StringBuilder r4 = r6.append(r4)     // Catch:{ Exception -> 0x011c, all -> 0x01ee }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x011c, all -> 0x01ee }
            r5.<init>(r4)     // Catch:{ Exception -> 0x011c, all -> 0x01ee }
            throw r5     // Catch:{ Exception -> 0x011c, all -> 0x01ee }
        L_0x011c:
            r4 = move-exception
            r6 = r4
            r7 = r3
            r5 = r0
            goto L_0x00a6
        L_0x0121:
            r3 = r4
            r0 = r5
        L_0x0123:
            org.msgpack.core.buffer.MessageBuffer.unsafe = r0
            org.msgpack.core.buffer.MessageBuffer.ARRAY_BYTE_BASE_OFFSET = r3
            org.msgpack.core.buffer.MessageBuffer.isUniversalBuffer = r8
            boolean r0 = org.msgpack.core.buffer.MessageBuffer.isUniversalBuffer
            if (r0 == 0) goto L_0x015d
            java.lang.String r0 = "org.msgpack.core.buffer.MessageBufferU"
        L_0x0130:
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Exception -> 0x0151 }
            r1 = 3
            java.lang.Class[] r1 = new java.lang.Class[r1]     // Catch:{ Exception -> 0x0151 }
            r2 = 0
            java.lang.Class<byte[]> r3 = byte[].class
            r1[r2] = r3     // Catch:{ Exception -> 0x0151 }
            r2 = 1
            java.lang.Class r3 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x0151 }
            r1[r2] = r3     // Catch:{ Exception -> 0x0151 }
            r2 = 2
            java.lang.Class r3 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x0151 }
            r1[r2] = r3     // Catch:{ Exception -> 0x0151 }
            java.lang.reflect.Constructor r0 = r0.getDeclaredConstructor(r1)     // Catch:{ Exception -> 0x0151 }
            r1 = 1
            r0.setAccessible(r1)     // Catch:{ Exception -> 0x0151 }
            org.msgpack.core.buffer.MessageBuffer.mbArrConstructor = r0     // Catch:{ Exception -> 0x0151 }
            goto L_0x00d8
        L_0x0151:
            r0 = move-exception
            java.io.PrintStream r1 = java.lang.System.err
            r0.printStackTrace(r1)
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            r1.<init>(r0)
            throw r1
        L_0x015d:
            java.nio.ByteOrder r0 = java.nio.ByteOrder.nativeOrder()
            java.nio.ByteOrder r3 = java.nio.ByteOrder.LITTLE_ENDIAN
            if (r0 != r3) goto L_0x016b
        L_0x0165:
            if (r1 == 0) goto L_0x016d
            java.lang.String r0 = "org.msgpack.core.buffer.MessageBuffer"
            goto L_0x0130
        L_0x016b:
            r1 = r2
            goto L_0x0165
        L_0x016d:
            java.lang.String r0 = "org.msgpack.core.buffer.MessageBufferBE"
            goto L_0x0130
        L_0x0171:
            java.nio.ByteOrder r0 = java.nio.ByteOrder.nativeOrder()
            java.nio.ByteOrder r3 = java.nio.ByteOrder.LITTLE_ENDIAN
            if (r0 != r3) goto L_0x0180
        L_0x0179:
            if (r1 == 0) goto L_0x0182
            java.lang.String r0 = "org.msgpack.core.buffer.MessageBuffer"
            goto L_0x00b8
        L_0x0180:
            r1 = r2
            goto L_0x0179
        L_0x0182:
            java.lang.String r0 = "org.msgpack.core.buffer.MessageBufferBE"
            goto L_0x00b8
        L_0x0187:
            r0 = move-exception
            java.io.PrintStream r1 = java.lang.System.err
            r0.printStackTrace(r1)
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            r1.<init>(r0)
            throw r1
        L_0x0193:
            r0 = move-exception
            r6 = r0
            r7 = r4
            r8 = r2
        L_0x0197:
            org.msgpack.core.buffer.MessageBuffer.unsafe = r5
            org.msgpack.core.buffer.MessageBuffer.ARRAY_BYTE_BASE_OFFSET = r7
            org.msgpack.core.buffer.MessageBuffer.isUniversalBuffer = r8
            boolean r0 = org.msgpack.core.buffer.MessageBuffer.isUniversalBuffer
            if (r0 == 0) goto L_0x01c5
            java.lang.String r0 = "org.msgpack.core.buffer.MessageBufferU"
        L_0x01a4:
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Exception -> 0x01d9 }
            r1 = 3
            java.lang.Class[] r1 = new java.lang.Class[r1]     // Catch:{ Exception -> 0x01d9 }
            r2 = 0
            java.lang.Class<byte[]> r3 = byte[].class
            r1[r2] = r3     // Catch:{ Exception -> 0x01d9 }
            r2 = 1
            java.lang.Class r3 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x01d9 }
            r1[r2] = r3     // Catch:{ Exception -> 0x01d9 }
            r2 = 2
            java.lang.Class r3 = java.lang.Integer.TYPE     // Catch:{ Exception -> 0x01d9 }
            r1[r2] = r3     // Catch:{ Exception -> 0x01d9 }
            java.lang.reflect.Constructor r0 = r0.getDeclaredConstructor(r1)     // Catch:{ Exception -> 0x01d9 }
            r1 = 1
            r0.setAccessible(r1)     // Catch:{ Exception -> 0x01d9 }
            org.msgpack.core.buffer.MessageBuffer.mbArrConstructor = r0     // Catch:{ Exception -> 0x01d9 }
            throw r6
        L_0x01c5:
            java.nio.ByteOrder r0 = java.nio.ByteOrder.nativeOrder()
            java.nio.ByteOrder r3 = java.nio.ByteOrder.LITTLE_ENDIAN
            if (r0 != r3) goto L_0x01d3
        L_0x01cd:
            if (r1 == 0) goto L_0x01d5
            java.lang.String r0 = "org.msgpack.core.buffer.MessageBuffer"
            goto L_0x01a4
        L_0x01d3:
            r1 = r2
            goto L_0x01cd
        L_0x01d5:
            java.lang.String r0 = "org.msgpack.core.buffer.MessageBufferBE"
            goto L_0x01a4
        L_0x01d9:
            r0 = move-exception
            java.io.PrintStream r1 = java.lang.System.err
            r0.printStackTrace(r1)
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            r1.<init>(r0)
            throw r1
        L_0x01e5:
            r0 = move-exception
            r6 = r0
            r7 = r4
            goto L_0x0197
        L_0x01e9:
            r3 = move-exception
            r6 = r3
            r7 = r4
            r5 = r0
            goto L_0x0197
        L_0x01ee:
            r4 = move-exception
            r6 = r4
            r7 = r3
            r5 = r0
            goto L_0x0197
        L_0x01f3:
            r0 = move-exception
            r6 = r0
            goto L_0x0197
        L_0x01f6:
            r0 = move-exception
            r6 = r0
            r7 = r4
            r8 = r2
            goto L_0x00a6
        L_0x01fc:
            r0 = move-exception
            r6 = r0
            r7 = r4
            goto L_0x00a6
        */
        throw new UnsupportedOperationException("Method not decompiled: org.msgpack.core.buffer.MessageBuffer.<clinit>():void");
    }

    public static MessageBuffer allocate(int i) {
        return wrap(new byte[i]);
    }

    public static MessageBuffer wrap(byte[] bArr) {
        return newMessageBuffer(bArr, 0, bArr.length);
    }

    public static MessageBuffer wrap(byte[] bArr, int i, int i2) {
        return newMessageBuffer(bArr, i, i2);
    }

    private static MessageBuffer newMessageBuffer(byte[] bArr, int i, int i2) {
        Preconditions.checkNotNull(bArr);
        try {
            return (MessageBuffer) mbArrConstructor.newInstance(bArr, Integer.valueOf(i), Integer.valueOf(i2));
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
    }

    public static void releaseBuffer(MessageBuffer messageBuffer) {
        if (!isUniversalBuffer && !(messageBuffer.base instanceof byte[])) {
            unsafe.freeMemory(messageBuffer.address);
        }
    }

    MessageBuffer(byte[] bArr, int i, int i2) {
        this.base = bArr;
        this.address = (long) (ARRAY_BYTE_BASE_OFFSET + i);
        this.size = i2;
    }

    protected MessageBuffer(Object obj, long j, int i) {
        this.base = obj;
        this.address = j;
        this.size = i;
    }

    public int size() {
        return this.size;
    }

    public MessageBuffer slice(int i, int i2) {
        if (i == 0 && i2 == size()) {
            return this;
        }
        Preconditions.checkArgument(i + i2 <= size());
        return new MessageBuffer(this.base, this.address + ((long) i), i2);
    }

    public byte getByte(int i) {
        return unsafe.getByte(this.base, this.address + ((long) i));
    }

    public boolean getBoolean(int i) {
        return unsafe.getBoolean(this.base, this.address + ((long) i));
    }

    public short getShort(int i) {
        return Short.reverseBytes(unsafe.getShort(this.base, this.address + ((long) i)));
    }

    public int getInt(int i) {
        return Integer.reverseBytes(unsafe.getInt(this.base, this.address + ((long) i)));
    }

    public float getFloat(int i) {
        return Float.intBitsToFloat(getInt(i));
    }

    public long getLong(int i) {
        return Long.reverseBytes(unsafe.getLong(this.base, this.address + ((long) i)));
    }

    public double getDouble(int i) {
        return Double.longBitsToDouble(getLong(i));
    }

    public void getBytes(int i, byte[] bArr, int i2, int i3) {
        unsafe.copyMemory(this.base, this.address + ((long) i), bArr, (long) (ARRAY_BYTE_BASE_OFFSET + i2), (long) i3);
    }

    public void getBytes(int i, int i2, ByteBuffer byteBuffer) {
        if (byteBuffer.remaining() < i2) {
            throw new BufferOverflowException();
        }
        byteBuffer.put(sliceAsByteBuffer(i, i2));
    }

    public void putByte(int i, byte b) {
        unsafe.putByte(this.base, this.address + ((long) i), b);
    }

    public void putBoolean(int i, boolean z) {
        unsafe.putBoolean(this.base, this.address + ((long) i), z);
    }

    public void putShort(int i, short s) {
        unsafe.putShort(this.base, this.address + ((long) i), Short.reverseBytes(s));
    }

    public void putInt(int i, int i2) {
        unsafe.putInt(this.base, this.address + ((long) i), Integer.reverseBytes(i2));
    }

    public void putFloat(int i, float f) {
        putInt(i, Float.floatToRawIntBits(f));
    }

    public void putLong(int i, long j) {
        unsafe.putLong(this.base, this.address + ((long) i), Long.reverseBytes(j));
    }

    public void putDouble(int i, double d) {
        putLong(i, Double.doubleToRawLongBits(d));
    }

    public void putBytes(int i, byte[] bArr, int i2, int i3) {
        unsafe.copyMemory(bArr, (long) (ARRAY_BYTE_BASE_OFFSET + i2), this.base, this.address + ((long) i), (long) i3);
    }

    public void putByteBuffer(int i, ByteBuffer byteBuffer, int i2) {
        if (!$assertionsDisabled && i2 > byteBuffer.remaining()) {
            throw new AssertionError();
        } else if (!$assertionsDisabled && isUniversalBuffer) {
            throw new AssertionError();
        } else if (byteBuffer.isDirect()) {
            unsafe.copyMemory((Object) null, DirectBufferAccess.getAddress(byteBuffer) + ((long) byteBuffer.position()), this.base, this.address + ((long) i), (long) i2);
            byteBuffer.position(byteBuffer.position() + i2);
        } else if (byteBuffer.hasArray()) {
            unsafe.copyMemory(byteBuffer.array(), (long) (ARRAY_BYTE_BASE_OFFSET + byteBuffer.position()), this.base, this.address + ((long) i), (long) i2);
            byteBuffer.position(byteBuffer.position() + i2);
        } else if (this.base != null) {
            byteBuffer.get((byte[]) this.base, i, i2);
        } else {
            for (int i3 = 0; i3 < i2; i3++) {
                unsafe.putByte(this.base, this.address + ((long) i), byteBuffer.get());
            }
        }
    }

    public ByteBuffer sliceAsByteBuffer(int i, int i2) {
        return ByteBuffer.wrap((byte[]) this.base, (int) ((this.address - ((long) ARRAY_BYTE_BASE_OFFSET)) + ((long) i)), i2);
    }

    public ByteBuffer sliceAsByteBuffer() {
        return sliceAsByteBuffer(0, size());
    }

    public byte[] toByteArray() {
        byte[] bArr = new byte[size()];
        unsafe.copyMemory(this.base, this.address, bArr, (long) ARRAY_BYTE_BASE_OFFSET, (long) size());
        return bArr;
    }

    public byte[] array() {
        return (byte[]) this.base;
    }

    public int arrayOffset() {
        return ((int) this.address) - ARRAY_BYTE_BASE_OFFSET;
    }

    public void copyTo(int i, MessageBuffer messageBuffer, int i2, int i3) {
        unsafe.copyMemory(this.base, this.address + ((long) i), messageBuffer.base, messageBuffer.address + ((long) i2), (long) i3);
    }

    public String toHexString(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        for (int i3 = i; i3 < i2; i3++) {
            if (i3 != i) {
                sb.append(" ");
            }
            sb.append(String.format("%02x", Byte.valueOf(getByte(i3))));
        }
        return sb.toString();
    }
}
