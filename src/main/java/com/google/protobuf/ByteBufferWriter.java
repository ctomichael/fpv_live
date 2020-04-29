package com.google.protobuf;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;

final class ByteBufferWriter {
    private static final ThreadLocal<SoftReference<byte[]>> BUFFER = new ThreadLocal<>();
    private static final float BUFFER_REALLOCATION_THRESHOLD = 0.5f;
    private static final long CHANNEL_FIELD_OFFSET = getChannelFieldOffset(FILE_OUTPUT_STREAM_CLASS);
    private static final Class<?> FILE_OUTPUT_STREAM_CLASS = safeGetClass("java.io.FileOutputStream");
    private static final int MAX_CACHED_BUFFER_SIZE = 16384;
    private static final int MIN_CACHED_BUFFER_SIZE = 1024;

    private ByteBufferWriter() {
    }

    static void clearCachedBuffer() {
        BUFFER.set(null);
    }

    static void write(ByteBuffer buffer, OutputStream output) throws IOException {
        int initialPos = buffer.position();
        try {
            if (buffer.hasArray()) {
                output.write(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());
            } else if (!writeToChannel(buffer, output)) {
                byte[] array = getOrCreateBuffer(buffer.remaining());
                while (buffer.hasRemaining()) {
                    int length = Math.min(buffer.remaining(), array.length);
                    buffer.get(array, 0, length);
                    output.write(array, 0, length);
                }
            }
        } finally {
            buffer.position(initialPos);
        }
    }

    private static byte[] getOrCreateBuffer(int requestedSize) {
        int requestedSize2 = Math.max(requestedSize, 1024);
        byte[] buffer = getBuffer();
        if (buffer == null || needToReallocate(requestedSize2, buffer.length)) {
            buffer = new byte[requestedSize2];
            if (requestedSize2 <= 16384) {
                setBuffer(buffer);
            }
        }
        return buffer;
    }

    private static boolean needToReallocate(int requestedSize, int bufferLength) {
        return bufferLength < requestedSize && ((float) bufferLength) < ((float) requestedSize) * BUFFER_REALLOCATION_THRESHOLD;
    }

    private static byte[] getBuffer() {
        SoftReference<byte[]> sr = BUFFER.get();
        if (sr == null) {
            return null;
        }
        return (byte[]) sr.get();
    }

    private static void setBuffer(byte[] value) {
        BUFFER.set(new SoftReference(value));
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.nio.channels.WritableByteChannel} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean writeToChannel(java.nio.ByteBuffer r8, java.io.OutputStream r9) throws java.io.IOException {
        /*
            long r4 = com.google.protobuf.ByteBufferWriter.CHANNEL_FIELD_OFFSET
            r6 = 0
            int r3 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r3 < 0) goto L_0x0022
            java.lang.Class<?> r3 = com.google.protobuf.ByteBufferWriter.FILE_OUTPUT_STREAM_CLASS
            boolean r3 = r3.isInstance(r9)
            if (r3 == 0) goto L_0x0022
            r2 = 0
            long r4 = com.google.protobuf.ByteBufferWriter.CHANNEL_FIELD_OFFSET     // Catch:{ ClassCastException -> 0x0024 }
            java.lang.Object r3 = com.google.protobuf.UnsafeUtil.getObject(r9, r4)     // Catch:{ ClassCastException -> 0x0024 }
            r0 = r3
            java.nio.channels.WritableByteChannel r0 = (java.nio.channels.WritableByteChannel) r0     // Catch:{ ClassCastException -> 0x0024 }
            r2 = r0
        L_0x001b:
            if (r2 == 0) goto L_0x0022
            r2.write(r8)
            r3 = 1
        L_0x0021:
            return r3
        L_0x0022:
            r3 = 0
            goto L_0x0021
        L_0x0024:
            r3 = move-exception
            goto L_0x001b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.ByteBufferWriter.writeToChannel(java.nio.ByteBuffer, java.io.OutputStream):boolean");
    }

    private static Class<?> safeGetClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static long getChannelFieldOffset(Class<?> clazz) {
        if (clazz != null) {
            try {
                if (UnsafeUtil.hasUnsafeArrayOperations()) {
                    return UnsafeUtil.objectFieldOffset(clazz.getDeclaredField("channel"));
                }
            } catch (Throwable th) {
            }
        }
        return -1;
    }
}
