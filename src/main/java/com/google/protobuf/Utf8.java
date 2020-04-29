package com.google.protobuf;

import dji.diagnostics.model.DJIDiagnosticsError;
import java.nio.ByteBuffer;

final class Utf8 {
    private static final long ASCII_MASK_LONG = -9187201950435737472L;
    public static final int COMPLETE = 0;
    public static final int MALFORMED = -1;
    static final int MAX_BYTES_PER_CHAR = 3;
    private static final int UNSAFE_COUNT_ASCII_THRESHOLD = 16;
    private static final Processor processor = ((!UnsafeProcessor.isAvailable() || Android.isOnAndroidDevice()) ? new SafeProcessor() : new UnsafeProcessor());

    public static boolean isValidUtf8(byte[] bytes) {
        return processor.isValidUtf8(bytes, 0, bytes.length);
    }

    public static boolean isValidUtf8(byte[] bytes, int index, int limit) {
        return processor.isValidUtf8(bytes, index, limit);
    }

    public static int partialIsValidUtf8(int state, byte[] bytes, int index, int limit) {
        return processor.partialIsValidUtf8(state, bytes, index, limit);
    }

    /* access modifiers changed from: private */
    public static int incompleteStateFor(int byte1) {
        if (byte1 > -12) {
            return -1;
        }
        return byte1;
    }

    /* access modifiers changed from: private */
    public static int incompleteStateFor(int byte1, int byte2) {
        if (byte1 > -12 || byte2 > -65) {
            return -1;
        }
        return (byte2 << 8) ^ byte1;
    }

    /* access modifiers changed from: private */
    public static int incompleteStateFor(int byte1, int byte2, int byte3) {
        if (byte1 > -12 || byte2 > -65 || byte3 > -65) {
            return -1;
        }
        return ((byte2 << 8) ^ byte1) ^ (byte3 << 16);
    }

    /* access modifiers changed from: private */
    public static int incompleteStateFor(byte[] bytes, int index, int limit) {
        byte b = bytes[index - 1];
        switch (limit - index) {
            case 0:
                return incompleteStateFor(b);
            case 1:
                return incompleteStateFor(b, bytes[index]);
            case 2:
                return incompleteStateFor(b, bytes[index], bytes[index + 1]);
            default:
                throw new AssertionError();
        }
    }

    /* access modifiers changed from: private */
    public static int incompleteStateFor(ByteBuffer buffer, int byte1, int index, int remaining) {
        switch (remaining) {
            case 0:
                return incompleteStateFor(byte1);
            case 1:
                return incompleteStateFor(byte1, buffer.get(index));
            case 2:
                return incompleteStateFor(byte1, buffer.get(index), buffer.get(index + 1));
            default:
                throw new AssertionError();
        }
    }

    static class UnpairedSurrogateException extends IllegalArgumentException {
        UnpairedSurrogateException(int index, int length) {
            super("Unpaired surrogate at index " + index + " of " + length);
        }
    }

    static int encodedLength(CharSequence sequence) {
        int utf16Length = sequence.length();
        int utf8Length = utf16Length;
        int i = 0;
        while (i < utf16Length && sequence.charAt(i) < 128) {
            i++;
        }
        while (true) {
            if (i < utf16Length) {
                char c = sequence.charAt(i);
                if (c >= 2048) {
                    utf8Length += encodedLengthGeneral(sequence, i);
                    break;
                }
                utf8Length += (127 - c) >>> 31;
                i++;
            } else {
                break;
            }
        }
        if (utf8Length >= utf16Length) {
            return utf8Length;
        }
        throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (((long) utf8Length) + 4294967296L));
    }

    private static int encodedLengthGeneral(CharSequence sequence, int start) {
        int utf16Length = sequence.length();
        int utf8Length = 0;
        int i = start;
        while (i < utf16Length) {
            char c = sequence.charAt(i);
            if (c < 2048) {
                utf8Length += (127 - c) >>> 31;
            } else {
                utf8Length += 2;
                if (55296 <= c && c <= 57343) {
                    if (Character.codePointAt(sequence, i) < 65536) {
                        throw new UnpairedSurrogateException(i, utf16Length);
                    }
                    i++;
                }
            }
            i++;
        }
        return utf8Length;
    }

    static int encode(CharSequence in2, byte[] out, int offset, int length) {
        return processor.encodeUtf8(in2, out, offset, length);
    }

    static boolean isValidUtf8(ByteBuffer buffer) {
        return processor.isValidUtf8(buffer, buffer.position(), buffer.remaining());
    }

    static int partialIsValidUtf8(int state, ByteBuffer buffer, int index, int limit) {
        return processor.partialIsValidUtf8(state, buffer, index, limit);
    }

    static String decodeUtf8(ByteBuffer buffer, int index, int size) throws InvalidProtocolBufferException {
        return processor.decodeUtf8(buffer, index, size);
    }

    static String decodeUtf8(byte[] bytes, int index, int size) throws InvalidProtocolBufferException {
        return processor.decodeUtf8(bytes, index, size);
    }

    static void encodeUtf8(CharSequence in2, ByteBuffer out) {
        processor.encodeUtf8(in2, out);
    }

    /* access modifiers changed from: private */
    public static int estimateConsecutiveAscii(ByteBuffer buffer, int index, int limit) {
        int i = index;
        int lim = limit - 7;
        while (i < lim && (buffer.getLong(i) & ASCII_MASK_LONG) == 0) {
            i += 8;
        }
        return i - index;
    }

    static abstract class Processor {
        /* access modifiers changed from: package-private */
        public abstract String decodeUtf8(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException;

        /* access modifiers changed from: package-private */
        public abstract String decodeUtf8Direct(ByteBuffer byteBuffer, int i, int i2) throws InvalidProtocolBufferException;

        /* access modifiers changed from: package-private */
        public abstract int encodeUtf8(CharSequence charSequence, byte[] bArr, int i, int i2);

        /* access modifiers changed from: package-private */
        public abstract void encodeUtf8Direct(CharSequence charSequence, ByteBuffer byteBuffer);

        /* access modifiers changed from: package-private */
        public abstract int partialIsValidUtf8(int i, byte[] bArr, int i2, int i3);

        /* access modifiers changed from: package-private */
        public abstract int partialIsValidUtf8Direct(int i, ByteBuffer byteBuffer, int i2, int i3);

        Processor() {
        }

        /* access modifiers changed from: package-private */
        public final boolean isValidUtf8(byte[] bytes, int index, int limit) {
            return partialIsValidUtf8(0, bytes, index, limit) == 0;
        }

        /* access modifiers changed from: package-private */
        public final boolean isValidUtf8(ByteBuffer buffer, int index, int limit) {
            return partialIsValidUtf8(0, buffer, index, limit) == 0;
        }

        /* access modifiers changed from: package-private */
        public final int partialIsValidUtf8(int state, ByteBuffer buffer, int index, int limit) {
            if (buffer.hasArray()) {
                int offset = buffer.arrayOffset();
                return partialIsValidUtf8(state, buffer.array(), offset + index, offset + limit);
            } else if (buffer.isDirect()) {
                return partialIsValidUtf8Direct(state, buffer, index, limit);
            } else {
                return partialIsValidUtf8Default(state, buffer, index, limit);
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x004a, code lost:
            if (r11.get(r3) > -65) goto L_0x004c;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x008b, code lost:
            if (r11.get(r3) > -65) goto L_0x008d;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final int partialIsValidUtf8Default(int r10, java.nio.ByteBuffer r11, int r12, int r13) {
            /*
                r9 = this;
                r8 = -32
                r6 = -96
                r4 = -1
                r7 = -65
                if (r10 == 0) goto L_0x0091
                if (r12 < r13) goto L_0x000c
            L_0x000b:
                return r10
            L_0x000c:
                byte r0 = (byte) r10
                if (r0 >= r8) goto L_0x001e
                r5 = -62
                if (r0 < r5) goto L_0x001c
                int r3 = r12 + 1
                byte r5 = r11.get(r12)
                if (r5 <= r7) goto L_0x0090
                r12 = r3
            L_0x001c:
                r10 = r4
                goto L_0x000b
            L_0x001e:
                r5 = -16
                if (r0 >= r5) goto L_0x004e
                int r5 = r10 >> 8
                r5 = r5 ^ -1
                byte r1 = (byte) r5
                if (r1 != 0) goto L_0x0037
                int r3 = r12 + 1
                byte r1 = r11.get(r12)
                if (r3 < r13) goto L_0x0038
                int r10 = com.google.protobuf.Utf8.incompleteStateFor(r0, r1)
                r12 = r3
                goto L_0x000b
            L_0x0037:
                r3 = r12
            L_0x0038:
                if (r1 > r7) goto L_0x0099
                if (r0 != r8) goto L_0x003e
                if (r1 < r6) goto L_0x0099
            L_0x003e:
                r5 = -19
                if (r0 != r5) goto L_0x0044
                if (r1 >= r6) goto L_0x0099
            L_0x0044:
                int r12 = r3 + 1
                byte r5 = r11.get(r3)
                if (r5 <= r7) goto L_0x0091
            L_0x004c:
                r10 = r4
                goto L_0x000b
            L_0x004e:
                int r5 = r10 >> 8
                r5 = r5 ^ -1
                byte r1 = (byte) r5
                r2 = 0
                if (r1 != 0) goto L_0x0064
                int r3 = r12 + 1
                byte r1 = r11.get(r12)
                if (r3 < r13) goto L_0x0068
                int r10 = com.google.protobuf.Utf8.incompleteStateFor(r0, r1)
                r12 = r3
                goto L_0x000b
            L_0x0064:
                int r5 = r10 >> 16
                byte r2 = (byte) r5
                r3 = r12
            L_0x0068:
                if (r2 != 0) goto L_0x0078
                int r12 = r3 + 1
                byte r2 = r11.get(r3)
                if (r12 < r13) goto L_0x0077
                int r10 = com.google.protobuf.Utf8.incompleteStateFor(r0, r1, r2)
                goto L_0x000b
            L_0x0077:
                r3 = r12
            L_0x0078:
                if (r1 > r7) goto L_0x0097
                int r5 = r0 << 28
                int r6 = r1 + 112
                int r5 = r5 + r6
                int r5 = r5 >> 30
                if (r5 != 0) goto L_0x0097
                if (r2 > r7) goto L_0x0097
                int r12 = r3 + 1
                byte r5 = r11.get(r3)
                if (r5 <= r7) goto L_0x0091
            L_0x008d:
                r10 = r4
                goto L_0x000b
            L_0x0090:
                r12 = r3
            L_0x0091:
                int r10 = partialIsValidUtf8(r11, r12, r13)
                goto L_0x000b
            L_0x0097:
                r12 = r3
                goto L_0x008d
            L_0x0099:
                r12 = r3
                goto L_0x004c
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.Processor.partialIsValidUtf8Default(int, java.nio.ByteBuffer, int, int):int");
        }

        private static int partialIsValidUtf8(ByteBuffer buffer, int index, int limit) {
            int index2;
            int index3 = index + Utf8.estimateConsecutiveAscii(buffer, index, limit);
            while (index3 < limit) {
                int index4 = index3 + 1;
                int byte1 = buffer.get(index3);
                if (byte1 < 0) {
                    if (byte1 < -32) {
                        if (index4 >= limit) {
                            return byte1;
                        }
                        if (byte1 < -62 || buffer.get(index4) > -65) {
                            return -1;
                        }
                        index2 = index4 + 1;
                    } else if (byte1 < -16) {
                        if (index4 >= limit - 1) {
                            return Utf8.incompleteStateFor(buffer, byte1, index4, limit - index4);
                        }
                        int index5 = index4 + 1;
                        byte byte2 = buffer.get(index4);
                        if (byte2 > -65 || ((byte1 == -32 && byte2 < -96) || ((byte1 == -19 && byte2 >= -96) || buffer.get(index5) > -65))) {
                            return -1;
                        }
                        index2 = index5 + 1;
                    } else if (index4 >= limit - 2) {
                        return Utf8.incompleteStateFor(buffer, byte1, index4, limit - index4);
                    } else {
                        int index6 = index4 + 1;
                        int byte22 = buffer.get(index4);
                        if (byte22 <= -65 && (((byte1 << 28) + (byte22 + 112)) >> 30) == 0) {
                            int index7 = index6 + 1;
                            if (buffer.get(index6) > -65) {
                                return -1;
                            }
                            index6 = index7 + 1;
                            if (buffer.get(index7) <= -65) {
                                index2 = index6;
                            }
                        }
                        return -1;
                    }
                    index3 = index2;
                } else {
                    index3 = index4;
                }
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public final String decodeUtf8(ByteBuffer buffer, int index, int size) throws InvalidProtocolBufferException {
            if (buffer.hasArray()) {
                return decodeUtf8(buffer.array(), buffer.arrayOffset() + index, size);
            } else if (buffer.isDirect()) {
                return decodeUtf8Direct(buffer, index, size);
            } else {
                return decodeUtf8Default(buffer, index, size);
            }
        }

        /* access modifiers changed from: package-private */
        public final String decodeUtf8Default(ByteBuffer buffer, int index, int size) throws InvalidProtocolBufferException {
            int offset;
            int resultPos;
            int resultPos2;
            if ((index | size | ((buffer.limit() - index) - size)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("buffer limit=%d, index=%d, limit=%d", Integer.valueOf(buffer.limit()), Integer.valueOf(index), Integer.valueOf(size)));
            }
            int offset2 = index;
            int limit = offset2 + size;
            char[] resultArr = new char[size];
            int resultPos3 = 0;
            while (true) {
                if (offset2 >= limit) {
                    resultPos = resultPos3;
                    offset = offset2;
                    break;
                }
                byte b = buffer.get(offset2);
                if (!DecodeUtil.isOneByte(b)) {
                    resultPos = resultPos3;
                    offset = offset2;
                    break;
                }
                offset2++;
                DecodeUtil.handleOneByte(b, resultArr, resultPos3);
                resultPos3++;
            }
            while (offset < limit) {
                int offset3 = offset + 1;
                byte byte1 = buffer.get(offset);
                if (DecodeUtil.isOneByte(byte1)) {
                    int resultPos4 = resultPos + 1;
                    DecodeUtil.handleOneByte(byte1, resultArr, resultPos);
                    while (true) {
                        if (offset3 >= limit) {
                            resultPos2 = resultPos4;
                            break;
                        }
                        byte b2 = buffer.get(offset3);
                        if (!DecodeUtil.isOneByte(b2)) {
                            resultPos2 = resultPos4;
                            break;
                        }
                        offset3++;
                        DecodeUtil.handleOneByte(b2, resultArr, resultPos4);
                        resultPos4++;
                    }
                } else if (DecodeUtil.isTwoBytes(byte1)) {
                    if (offset3 >= limit) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    DecodeUtil.handleTwoBytes(byte1, buffer.get(offset3), resultArr, resultPos);
                    resultPos2 = resultPos + 1;
                    offset3++;
                } else if (DecodeUtil.isThreeBytes(byte1)) {
                    if (offset3 >= limit - 1) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    int offset4 = offset3 + 1;
                    byte b3 = buffer.get(offset3);
                    offset3 = offset4 + 1;
                    DecodeUtil.handleThreeBytes(byte1, b3, buffer.get(offset4), resultArr, resultPos);
                    resultPos2 = resultPos + 1;
                } else if (offset3 >= limit - 2) {
                    throw InvalidProtocolBufferException.invalidUtf8();
                } else {
                    int offset5 = offset3 + 1;
                    byte b4 = buffer.get(offset3);
                    int offset6 = offset5 + 1;
                    DecodeUtil.handleFourBytes(byte1, b4, buffer.get(offset5), buffer.get(offset6), resultArr, resultPos);
                    resultPos2 = resultPos + 1 + 1;
                    offset3 = offset6 + 1;
                }
                offset = offset3;
            }
            return new String(resultArr, 0, resultPos);
        }

        /* access modifiers changed from: package-private */
        public final void encodeUtf8(CharSequence in2, ByteBuffer out) {
            if (out.hasArray()) {
                int offset = out.arrayOffset();
                out.position(Utf8.encode(in2, out.array(), out.position() + offset, out.remaining()) - offset);
            } else if (out.isDirect()) {
                encodeUtf8Direct(in2, out);
            } else {
                encodeUtf8Default(in2, out);
            }
        }

        /* access modifiers changed from: package-private */
        public final void encodeUtf8Default(CharSequence in2, ByteBuffer out) {
            int inLength = in2.length();
            int outIx = out.position();
            int inIx = 0;
            while (inIx < inLength) {
                try {
                    char c = in2.charAt(inIx);
                    if (c >= 128) {
                        break;
                    }
                    out.put(outIx + inIx, (byte) c);
                    inIx++;
                } catch (IndexOutOfBoundsException e) {
                }
            }
            if (inIx == inLength) {
                out.position(outIx + inIx);
                return;
            }
            int outIx2 = outIx + inIx;
            while (inIx < inLength) {
                try {
                    char c2 = in2.charAt(inIx);
                    if (c2 < 128) {
                        out.put(outIx2, (byte) c2);
                        outIx = outIx2;
                    } else if (c2 < 2048) {
                        outIx = outIx2 + 1;
                        out.put(outIx2, (byte) ((c2 >>> 6) | 192));
                        out.put(outIx, (byte) ((c2 & '?') | 128));
                    } else if (c2 < 55296 || 57343 < c2) {
                        outIx = outIx2 + 1;
                        out.put(outIx2, (byte) ((c2 >>> 12) | 224));
                        outIx2 = outIx + 1;
                        out.put(outIx, (byte) (((c2 >>> 6) & 63) | 128));
                        out.put(outIx2, (byte) ((c2 & '?') | 128));
                        outIx = outIx2;
                    } else {
                        if (inIx + 1 != inLength) {
                            inIx++;
                            char low = in2.charAt(inIx);
                            if (Character.isSurrogatePair(c2, low)) {
                                int codePoint = Character.toCodePoint(c2, low);
                                outIx = outIx2 + 1;
                                out.put(outIx2, (byte) ((codePoint >>> 18) | 240));
                                outIx2 = outIx + 1;
                                out.put(outIx, (byte) (((codePoint >>> 12) & 63) | 128));
                                outIx = outIx2 + 1;
                                out.put(outIx2, (byte) (((codePoint >>> 6) & 63) | 128));
                                out.put(outIx, (byte) ((codePoint & 63) | 128));
                            }
                        }
                        throw new UnpairedSurrogateException(inIx, inLength);
                    }
                    inIx++;
                    outIx2 = outIx + 1;
                } catch (IndexOutOfBoundsException e2) {
                    outIx = outIx2;
                    throw new ArrayIndexOutOfBoundsException("Failed writing " + in2.charAt(inIx) + " at index " + (out.position() + Math.max(inIx, (outIx - out.position()) + 1)));
                }
            }
            out.position(outIx2);
        }
    }

    static final class SafeProcessor extends Processor {
        SafeProcessor() {
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x0044, code lost:
            if (r11[r3] > -65) goto L_0x0046;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x007f, code lost:
            if (r11[r3] > -65) goto L_0x0081;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int partialIsValidUtf8(int r10, byte[] r11, int r12, int r13) {
            /*
                r9 = this;
                r8 = -32
                r6 = -96
                r4 = -1
                r7 = -65
                if (r10 == 0) goto L_0x0084
                if (r12 < r13) goto L_0x000c
            L_0x000b:
                return r10
            L_0x000c:
                byte r0 = (byte) r10
                if (r0 >= r8) goto L_0x001c
                r5 = -62
                if (r0 < r5) goto L_0x001a
                int r3 = r12 + 1
                byte r5 = r11[r12]
                if (r5 <= r7) goto L_0x0083
                r12 = r3
            L_0x001a:
                r10 = r4
                goto L_0x000b
            L_0x001c:
                r5 = -16
                if (r0 >= r5) goto L_0x0048
                int r5 = r10 >> 8
                r5 = r5 ^ -1
                byte r1 = (byte) r5
                if (r1 != 0) goto L_0x0033
                int r3 = r12 + 1
                byte r1 = r11[r12]
                if (r3 < r13) goto L_0x0034
                int r10 = com.google.protobuf.Utf8.incompleteStateFor(r0, r1)
                r12 = r3
                goto L_0x000b
            L_0x0033:
                r3 = r12
            L_0x0034:
                if (r1 > r7) goto L_0x008b
                if (r0 != r8) goto L_0x003a
                if (r1 < r6) goto L_0x008b
            L_0x003a:
                r5 = -19
                if (r0 != r5) goto L_0x0040
                if (r1 >= r6) goto L_0x008b
            L_0x0040:
                int r12 = r3 + 1
                byte r5 = r11[r3]
                if (r5 <= r7) goto L_0x0084
            L_0x0046:
                r10 = r4
                goto L_0x000b
            L_0x0048:
                int r5 = r10 >> 8
                r5 = r5 ^ -1
                byte r1 = (byte) r5
                r2 = 0
                if (r1 != 0) goto L_0x005c
                int r3 = r12 + 1
                byte r1 = r11[r12]
                if (r3 < r13) goto L_0x0060
                int r10 = com.google.protobuf.Utf8.incompleteStateFor(r0, r1)
                r12 = r3
                goto L_0x000b
            L_0x005c:
                int r5 = r10 >> 16
                byte r2 = (byte) r5
                r3 = r12
            L_0x0060:
                if (r2 != 0) goto L_0x006e
                int r12 = r3 + 1
                byte r2 = r11[r3]
                if (r12 < r13) goto L_0x006d
                int r10 = com.google.protobuf.Utf8.incompleteStateFor(r0, r1, r2)
                goto L_0x000b
            L_0x006d:
                r3 = r12
            L_0x006e:
                if (r1 > r7) goto L_0x0089
                int r5 = r0 << 28
                int r6 = r1 + 112
                int r5 = r5 + r6
                int r5 = r5 >> 30
                if (r5 != 0) goto L_0x0089
                if (r2 > r7) goto L_0x0089
                int r12 = r3 + 1
                byte r5 = r11[r3]
                if (r5 <= r7) goto L_0x0084
            L_0x0081:
                r10 = r4
                goto L_0x000b
            L_0x0083:
                r12 = r3
            L_0x0084:
                int r10 = partialIsValidUtf8(r11, r12, r13)
                goto L_0x000b
            L_0x0089:
                r12 = r3
                goto L_0x0081
            L_0x008b:
                r12 = r3
                goto L_0x0046
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.SafeProcessor.partialIsValidUtf8(int, byte[], int, int):int");
        }

        /* access modifiers changed from: package-private */
        public int partialIsValidUtf8Direct(int state, ByteBuffer buffer, int index, int limit) {
            return partialIsValidUtf8Default(state, buffer, index, limit);
        }

        /* access modifiers changed from: package-private */
        public String decodeUtf8(byte[] bytes, int index, int size) throws InvalidProtocolBufferException {
            int offset;
            int resultPos;
            int resultPos2;
            if ((index | size | ((bytes.length - index) - size)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(bytes.length), Integer.valueOf(index), Integer.valueOf(size)));
            }
            int offset2 = index;
            int limit = offset2 + size;
            char[] resultArr = new char[size];
            int resultPos3 = 0;
            while (true) {
                if (offset2 >= limit) {
                    resultPos = resultPos3;
                    offset = offset2;
                    break;
                }
                byte b = bytes[offset2];
                if (!DecodeUtil.isOneByte(b)) {
                    resultPos = resultPos3;
                    offset = offset2;
                    break;
                }
                offset2++;
                DecodeUtil.handleOneByte(b, resultArr, resultPos3);
                resultPos3++;
            }
            while (offset < limit) {
                int offset3 = offset + 1;
                byte byte1 = bytes[offset];
                if (DecodeUtil.isOneByte(byte1)) {
                    int resultPos4 = resultPos + 1;
                    DecodeUtil.handleOneByte(byte1, resultArr, resultPos);
                    while (true) {
                        if (offset3 >= limit) {
                            resultPos2 = resultPos4;
                            break;
                        }
                        byte b2 = bytes[offset3];
                        if (!DecodeUtil.isOneByte(b2)) {
                            resultPos2 = resultPos4;
                            break;
                        }
                        offset3++;
                        DecodeUtil.handleOneByte(b2, resultArr, resultPos4);
                        resultPos4++;
                    }
                } else if (DecodeUtil.isTwoBytes(byte1)) {
                    if (offset3 >= limit) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    DecodeUtil.handleTwoBytes(byte1, bytes[offset3], resultArr, resultPos);
                    resultPos2 = resultPos + 1;
                    offset3++;
                } else if (DecodeUtil.isThreeBytes(byte1)) {
                    if (offset3 >= limit - 1) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    int offset4 = offset3 + 1;
                    byte b3 = bytes[offset3];
                    offset3 = offset4 + 1;
                    DecodeUtil.handleThreeBytes(byte1, b3, bytes[offset4], resultArr, resultPos);
                    resultPos2 = resultPos + 1;
                } else if (offset3 >= limit - 2) {
                    throw InvalidProtocolBufferException.invalidUtf8();
                } else {
                    int offset5 = offset3 + 1;
                    byte b4 = bytes[offset3];
                    int offset6 = offset5 + 1;
                    DecodeUtil.handleFourBytes(byte1, b4, bytes[offset5], bytes[offset6], resultArr, resultPos);
                    resultPos2 = resultPos + 1 + 1;
                    offset3 = offset6 + 1;
                }
                offset = offset3;
            }
            return new String(resultArr, 0, resultPos);
        }

        /* access modifiers changed from: package-private */
        public String decodeUtf8Direct(ByteBuffer buffer, int index, int size) throws InvalidProtocolBufferException {
            return decodeUtf8Default(buffer, index, size);
        }

        /* access modifiers changed from: package-private */
        public int encodeUtf8(CharSequence in2, byte[] out, int offset, int length) {
            int j;
            int utf16Length = in2.length();
            int j2 = offset;
            int i = 0;
            int limit = offset + length;
            while (i < utf16Length && i + j2 < limit) {
                char c = in2.charAt(i);
                if (c >= 128) {
                    break;
                }
                out[j2 + i] = (byte) c;
                i++;
            }
            if (i == utf16Length) {
                return j2 + utf16Length;
            }
            int j3 = j2 + i;
            while (i < utf16Length) {
                char c2 = in2.charAt(i);
                if (c2 < 128 && j3 < limit) {
                    j = j3 + 1;
                    out[j3] = (byte) c2;
                } else if (c2 < 2048 && j3 <= limit - 2) {
                    int j4 = j3 + 1;
                    out[j3] = (byte) ((c2 >>> 6) | 960);
                    out[j4] = (byte) ((c2 & '?') | 128);
                    j = j4 + 1;
                } else if ((c2 < 55296 || 57343 < c2) && j3 <= limit - 3) {
                    int j5 = j3 + 1;
                    out[j3] = (byte) ((c2 >>> 12) | 480);
                    int j6 = j5 + 1;
                    out[j5] = (byte) (((c2 >>> 6) & 63) | 128);
                    j = j6 + 1;
                    out[j6] = (byte) ((c2 & '?') | 128);
                } else if (j3 <= limit - 4) {
                    if (i + 1 != in2.length()) {
                        i++;
                        char low = in2.charAt(i);
                        if (Character.isSurrogatePair(c2, low)) {
                            int codePoint = Character.toCodePoint(c2, low);
                            int j7 = j3 + 1;
                            out[j3] = (byte) ((codePoint >>> 18) | 240);
                            int j8 = j7 + 1;
                            out[j7] = (byte) (((codePoint >>> 12) & 63) | 128);
                            int j9 = j8 + 1;
                            out[j8] = (byte) (((codePoint >>> 6) & 63) | 128);
                            out[j9] = (byte) ((codePoint & 63) | 128);
                            j = j9 + 1;
                        }
                    }
                    throw new UnpairedSurrogateException(i - 1, utf16Length);
                } else if (55296 > c2 || c2 > 57343 || (i + 1 != in2.length() && Character.isSurrogatePair(c2, in2.charAt(i + 1)))) {
                    throw new ArrayIndexOutOfBoundsException("Failed writing " + c2 + " at index " + j3);
                } else {
                    throw new UnpairedSurrogateException(i, utf16Length);
                }
                i++;
                j3 = j;
            }
            return j3;
        }

        /* access modifiers changed from: package-private */
        public void encodeUtf8Direct(CharSequence in2, ByteBuffer out) {
            encodeUtf8Default(in2, out);
        }

        private static int partialIsValidUtf8(byte[] bytes, int index, int limit) {
            while (index < limit && bytes[index] >= 0) {
                index++;
            }
            if (index >= limit) {
                return 0;
            }
            return partialIsValidUtf8NonAscii(bytes, index, limit);
        }

        private static int partialIsValidUtf8NonAscii(byte[] bytes, int index, int limit) {
            int index2;
            int index3;
            int index4 = index;
            while (index4 < limit) {
                int index5 = index4 + 1;
                byte b = bytes[index4];
                if (b < 0) {
                    if (b < -32) {
                        if (index5 >= limit) {
                            return b;
                        }
                        if (b >= -62) {
                            index3 = index5 + 1;
                            if (bytes[index5] > -65) {
                            }
                        }
                        return -1;
                    } else if (b < -16) {
                        if (index5 >= limit - 1) {
                            return Utf8.incompleteStateFor(bytes, index5, limit);
                        }
                        int index6 = index5 + 1;
                        byte b2 = bytes[index5];
                        if (b2 <= -65 && ((b != -32 || b2 >= -96) && (b != -19 || b2 < -96))) {
                            index2 = index6 + 1;
                            if (bytes[index6] > -65) {
                            }
                            index4 = index2;
                        }
                        return -1;
                    } else if (index5 >= limit - 2) {
                        return Utf8.incompleteStateFor(bytes, index5, limit);
                    } else {
                        index3 = index5 + 1;
                        byte b3 = bytes[index5];
                        if (b3 <= -65 && (((b << 28) + (b3 + 112)) >> 30) == 0) {
                            int index7 = index3 + 1;
                            if (bytes[index3] > -65) {
                                return -1;
                            }
                            index3 = index7 + 1;
                            if (bytes[index7] > -65) {
                            }
                        }
                        return -1;
                    }
                    index2 = index3;
                    index4 = index2;
                } else {
                    index4 = index5;
                }
            }
            return 0;
        }
    }

    static final class UnsafeProcessor extends Processor {
        UnsafeProcessor() {
        }

        static boolean isAvailable() {
            return UnsafeUtil.hasUnsafeArrayOperations() && UnsafeUtil.hasUnsafeByteBufferOperations();
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x009c, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r18, r8) > -65) goto L_0x009e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x00f5, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r18, r8) > -65) goto L_0x00f7;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int partialIsValidUtf8(int r17, byte[] r18, int r19, int r20) {
            /*
                r16 = this;
                r5 = r19 | r20
                r0 = r18
                int r12 = r0.length
                int r12 = r12 - r20
                r5 = r5 | r12
                if (r5 >= 0) goto L_0x0032
                java.lang.ArrayIndexOutOfBoundsException r5 = new java.lang.ArrayIndexOutOfBoundsException
                java.lang.String r12 = "Array length=%d, index=%d, limit=%d"
                r13 = 3
                java.lang.Object[] r13 = new java.lang.Object[r13]
                r14 = 0
                r0 = r18
                int r15 = r0.length
                java.lang.Integer r15 = java.lang.Integer.valueOf(r15)
                r13[r14] = r15
                r14 = 1
                java.lang.Integer r15 = java.lang.Integer.valueOf(r19)
                r13[r14] = r15
                r14 = 2
                java.lang.Integer r15 = java.lang.Integer.valueOf(r20)
                r13[r14] = r15
                java.lang.String r12 = java.lang.String.format(r12, r13)
                r5.<init>(r12)
                throw r5
            L_0x0032:
                r0 = r19
                long r6 = (long) r0
                r0 = r20
                long r10 = (long) r0
                if (r17 == 0) goto L_0x00fc
                int r5 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
                if (r5 < 0) goto L_0x003f
            L_0x003e:
                return r17
            L_0x003f:
                r0 = r17
                byte r2 = (byte) r0
                r5 = -32
                if (r2 >= r5) goto L_0x005c
                r5 = -62
                if (r2 < r5) goto L_0x0059
                r12 = 1
                long r8 = r6 + r12
                r0 = r18
                byte r5 = com.google.protobuf.UnsafeUtil.getByte(r0, r6)
                r12 = -65
                if (r5 <= r12) goto L_0x00fb
                r6 = r8
            L_0x0059:
                r17 = -1
                goto L_0x003e
            L_0x005c:
                r5 = -16
                if (r2 >= r5) goto L_0x00a1
                int r5 = r17 >> 8
                r5 = r5 ^ -1
                byte r3 = (byte) r5
                if (r3 != 0) goto L_0x007b
                r12 = 1
                long r8 = r6 + r12
                r0 = r18
                byte r3 = com.google.protobuf.UnsafeUtil.getByte(r0, r6)
                int r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                if (r5 < 0) goto L_0x007c
                int r17 = com.google.protobuf.Utf8.incompleteStateFor(r2, r3)
                r6 = r8
                goto L_0x003e
            L_0x007b:
                r8 = r6
            L_0x007c:
                r5 = -65
                if (r3 > r5) goto L_0x0109
                r5 = -32
                if (r2 != r5) goto L_0x0088
                r5 = -96
                if (r3 < r5) goto L_0x0109
            L_0x0088:
                r5 = -19
                if (r2 != r5) goto L_0x0090
                r5 = -96
                if (r3 >= r5) goto L_0x0109
            L_0x0090:
                r12 = 1
                long r6 = r8 + r12
                r0 = r18
                byte r5 = com.google.protobuf.UnsafeUtil.getByte(r0, r8)
                r12 = -65
                if (r5 <= r12) goto L_0x00fc
            L_0x009e:
                r17 = -1
                goto L_0x003e
            L_0x00a1:
                int r5 = r17 >> 8
                r5 = r5 ^ -1
                byte r3 = (byte) r5
                r4 = 0
                if (r3 != 0) goto L_0x00bd
                r12 = 1
                long r8 = r6 + r12
                r0 = r18
                byte r3 = com.google.protobuf.UnsafeUtil.getByte(r0, r6)
                int r5 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                if (r5 < 0) goto L_0x00c1
                int r17 = com.google.protobuf.Utf8.incompleteStateFor(r2, r3)
                r6 = r8
                goto L_0x003e
            L_0x00bd:
                int r5 = r17 >> 16
                byte r4 = (byte) r5
                r8 = r6
            L_0x00c1:
                if (r4 != 0) goto L_0x00d8
                r12 = 1
                long r6 = r8 + r12
                r0 = r18
                byte r4 = com.google.protobuf.UnsafeUtil.getByte(r0, r8)
                int r5 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
                if (r5 < 0) goto L_0x00d7
                int r17 = com.google.protobuf.Utf8.incompleteStateFor(r2, r3, r4)
                goto L_0x003e
            L_0x00d7:
                r8 = r6
            L_0x00d8:
                r5 = -65
                if (r3 > r5) goto L_0x0107
                int r5 = r2 << 28
                int r12 = r3 + 112
                int r5 = r5 + r12
                int r5 = r5 >> 30
                if (r5 != 0) goto L_0x0107
                r5 = -65
                if (r4 > r5) goto L_0x0107
                r12 = 1
                long r6 = r8 + r12
                r0 = r18
                byte r5 = com.google.protobuf.UnsafeUtil.getByte(r0, r8)
                r12 = -65
                if (r5 <= r12) goto L_0x00fc
            L_0x00f7:
                r17 = -1
                goto L_0x003e
            L_0x00fb:
                r6 = r8
            L_0x00fc:
                long r12 = r10 - r6
                int r5 = (int) r12
                r0 = r18
                int r17 = partialIsValidUtf8(r0, r6, r5)
                goto L_0x003e
            L_0x0107:
                r6 = r8
                goto L_0x00f7
            L_0x0109:
                r6 = r8
                goto L_0x009e
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.partialIsValidUtf8(int, byte[], int, int):int");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x00a0, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r4) > -65) goto L_0x00a2;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:0x00f3, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r4) > -65) goto L_0x00f5;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int partialIsValidUtf8Direct(int r17, java.nio.ByteBuffer r18, int r19, int r20) {
            /*
                r16 = this;
                r11 = r19 | r20
                int r12 = r18.limit()
                int r12 = r12 - r20
                r11 = r11 | r12
                if (r11 >= 0) goto L_0x0034
                java.lang.ArrayIndexOutOfBoundsException r11 = new java.lang.ArrayIndexOutOfBoundsException
                java.lang.String r12 = "buffer limit=%d, index=%d, limit=%d"
                r13 = 3
                java.lang.Object[] r13 = new java.lang.Object[r13]
                r14 = 0
                int r15 = r18.limit()
                java.lang.Integer r15 = java.lang.Integer.valueOf(r15)
                r13[r14] = r15
                r14 = 1
                java.lang.Integer r15 = java.lang.Integer.valueOf(r19)
                r13[r14] = r15
                r14 = 2
                java.lang.Integer r15 = java.lang.Integer.valueOf(r20)
                r13[r14] = r15
                java.lang.String r12 = java.lang.String.format(r12, r13)
                r11.<init>(r12)
                throw r11
            L_0x0034:
                long r12 = com.google.protobuf.UnsafeUtil.addressOffset(r18)
                r0 = r19
                long r14 = (long) r0
                long r2 = r12 + r14
                int r11 = r20 - r19
                long r12 = (long) r11
                long r6 = r2 + r12
                if (r17 == 0) goto L_0x00fa
                int r11 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                if (r11 < 0) goto L_0x0049
            L_0x0048:
                return r17
            L_0x0049:
                r0 = r17
                byte r8 = (byte) r0
                r11 = -32
                if (r8 >= r11) goto L_0x0064
                r11 = -62
                if (r8 < r11) goto L_0x0061
                r12 = 1
                long r4 = r2 + r12
                byte r11 = com.google.protobuf.UnsafeUtil.getByte(r2)
                r12 = -65
                if (r11 <= r12) goto L_0x00f9
                r2 = r4
            L_0x0061:
                r17 = -1
                goto L_0x0048
            L_0x0064:
                r11 = -16
                if (r8 >= r11) goto L_0x00a5
                int r11 = r17 >> 8
                r11 = r11 ^ -1
                byte r9 = (byte) r11
                if (r9 != 0) goto L_0x0081
                r12 = 1
                long r4 = r2 + r12
                byte r9 = com.google.protobuf.UnsafeUtil.getByte(r2)
                int r11 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                if (r11 < 0) goto L_0x0082
                int r17 = com.google.protobuf.Utf8.incompleteStateFor(r8, r9)
                r2 = r4
                goto L_0x0048
            L_0x0081:
                r4 = r2
            L_0x0082:
                r11 = -65
                if (r9 > r11) goto L_0x0105
                r11 = -32
                if (r8 != r11) goto L_0x008e
                r11 = -96
                if (r9 < r11) goto L_0x0105
            L_0x008e:
                r11 = -19
                if (r8 != r11) goto L_0x0096
                r11 = -96
                if (r9 >= r11) goto L_0x0105
            L_0x0096:
                r12 = 1
                long r2 = r4 + r12
                byte r11 = com.google.protobuf.UnsafeUtil.getByte(r4)
                r12 = -65
                if (r11 <= r12) goto L_0x00fa
            L_0x00a2:
                r17 = -1
                goto L_0x0048
            L_0x00a5:
                int r11 = r17 >> 8
                r11 = r11 ^ -1
                byte r9 = (byte) r11
                r10 = 0
                if (r9 != 0) goto L_0x00bf
                r12 = 1
                long r4 = r2 + r12
                byte r9 = com.google.protobuf.UnsafeUtil.getByte(r2)
                int r11 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                if (r11 < 0) goto L_0x00c3
                int r17 = com.google.protobuf.Utf8.incompleteStateFor(r8, r9)
                r2 = r4
                goto L_0x0048
            L_0x00bf:
                int r11 = r17 >> 16
                byte r10 = (byte) r11
                r4 = r2
            L_0x00c3:
                if (r10 != 0) goto L_0x00d8
                r12 = 1
                long r2 = r4 + r12
                byte r10 = com.google.protobuf.UnsafeUtil.getByte(r4)
                int r11 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                if (r11 < 0) goto L_0x00d7
                int r17 = com.google.protobuf.Utf8.incompleteStateFor(r8, r9, r10)
                goto L_0x0048
            L_0x00d7:
                r4 = r2
            L_0x00d8:
                r11 = -65
                if (r9 > r11) goto L_0x0103
                int r11 = r8 << 28
                int r12 = r9 + 112
                int r11 = r11 + r12
                int r11 = r11 >> 30
                if (r11 != 0) goto L_0x0103
                r11 = -65
                if (r10 > r11) goto L_0x0103
                r12 = 1
                long r2 = r4 + r12
                byte r11 = com.google.protobuf.UnsafeUtil.getByte(r4)
                r12 = -65
                if (r11 <= r12) goto L_0x00fa
            L_0x00f5:
                r17 = -1
                goto L_0x0048
            L_0x00f9:
                r2 = r4
            L_0x00fa:
                long r12 = r6 - r2
                int r11 = (int) r12
                int r17 = partialIsValidUtf8(r2, r11)
                goto L_0x0048
            L_0x0103:
                r2 = r4
                goto L_0x00f5
            L_0x0105:
                r2 = r4
                goto L_0x00a2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.partialIsValidUtf8Direct(int, java.nio.ByteBuffer, int, int):int");
        }

        /* access modifiers changed from: package-private */
        public String decodeUtf8(byte[] bytes, int index, int size) throws InvalidProtocolBufferException {
            int offset;
            int resultPos;
            int resultPos2;
            if ((index | size | ((bytes.length - index) - size)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(bytes.length), Integer.valueOf(index), Integer.valueOf(size)));
            }
            int offset2 = index;
            int limit = offset2 + size;
            char[] resultArr = new char[size];
            int resultPos3 = 0;
            while (true) {
                if (offset2 >= limit) {
                    resultPos = resultPos3;
                    offset = offset2;
                    break;
                }
                byte b = UnsafeUtil.getByte(bytes, (long) offset2);
                if (!DecodeUtil.isOneByte(b)) {
                    resultPos = resultPos3;
                    offset = offset2;
                    break;
                }
                offset2++;
                DecodeUtil.handleOneByte(b, resultArr, resultPos3);
                resultPos3++;
            }
            while (offset < limit) {
                int offset3 = offset + 1;
                byte byte1 = UnsafeUtil.getByte(bytes, (long) offset);
                if (DecodeUtil.isOneByte(byte1)) {
                    int resultPos4 = resultPos + 1;
                    DecodeUtil.handleOneByte(byte1, resultArr, resultPos);
                    while (true) {
                        if (offset3 >= limit) {
                            resultPos2 = resultPos4;
                            break;
                        }
                        byte b2 = UnsafeUtil.getByte(bytes, (long) offset3);
                        if (!DecodeUtil.isOneByte(b2)) {
                            resultPos2 = resultPos4;
                            break;
                        }
                        offset3++;
                        DecodeUtil.handleOneByte(b2, resultArr, resultPos4);
                        resultPos4++;
                    }
                } else if (DecodeUtil.isTwoBytes(byte1)) {
                    if (offset3 >= limit) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    DecodeUtil.handleTwoBytes(byte1, UnsafeUtil.getByte(bytes, (long) offset3), resultArr, resultPos);
                    resultPos2 = resultPos + 1;
                    offset3++;
                } else if (DecodeUtil.isThreeBytes(byte1)) {
                    if (offset3 >= limit - 1) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    int offset4 = offset3 + 1;
                    byte b3 = UnsafeUtil.getByte(bytes, (long) offset3);
                    offset3 = offset4 + 1;
                    DecodeUtil.handleThreeBytes(byte1, b3, UnsafeUtil.getByte(bytes, (long) offset4), resultArr, resultPos);
                    resultPos2 = resultPos + 1;
                } else if (offset3 >= limit - 2) {
                    throw InvalidProtocolBufferException.invalidUtf8();
                } else {
                    int offset5 = offset3 + 1;
                    byte b4 = UnsafeUtil.getByte(bytes, (long) offset3);
                    int offset6 = offset5 + 1;
                    DecodeUtil.handleFourBytes(byte1, b4, UnsafeUtil.getByte(bytes, (long) offset5), UnsafeUtil.getByte(bytes, (long) offset6), resultArr, resultPos);
                    resultPos2 = resultPos + 1 + 1;
                    offset3 = offset6 + 1;
                }
                offset = offset3;
            }
            return new String(resultArr, 0, resultPos);
        }

        /* access modifiers changed from: package-private */
        public String decodeUtf8Direct(ByteBuffer buffer, int index, int size) throws InvalidProtocolBufferException {
            long address;
            int resultPos;
            int resultPos2;
            if ((index | size | ((buffer.limit() - index) - size)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("buffer limit=%d, index=%d, limit=%d", Integer.valueOf(buffer.limit()), Integer.valueOf(index), Integer.valueOf(size)));
            }
            long address2 = UnsafeUtil.addressOffset(buffer) + ((long) index);
            long addressLimit = address2 + ((long) size);
            char[] resultArr = new char[size];
            int resultPos3 = 0;
            while (true) {
                int resultPos4 = resultPos3;
                if (address >= addressLimit) {
                    resultPos = resultPos4;
                    break;
                }
                byte b = UnsafeUtil.getByte(address);
                if (!DecodeUtil.isOneByte(b)) {
                    resultPos = resultPos4;
                    break;
                }
                address2 = address + 1;
                resultPos3 = resultPos4 + 1;
                DecodeUtil.handleOneByte(b, resultArr, resultPos4);
            }
            while (true) {
                long address3 = address;
                if (address3 >= addressLimit) {
                    return new String(resultArr, 0, resultPos);
                }
                address = address3 + 1;
                byte byte1 = UnsafeUtil.getByte(address3);
                if (DecodeUtil.isOneByte(byte1)) {
                    int resultPos5 = resultPos + 1;
                    DecodeUtil.handleOneByte(byte1, resultArr, resultPos);
                    while (true) {
                        if (address >= addressLimit) {
                            resultPos2 = resultPos5;
                            break;
                        }
                        byte b2 = UnsafeUtil.getByte(address);
                        if (!DecodeUtil.isOneByte(b2)) {
                            resultPos2 = resultPos5;
                            break;
                        }
                        address++;
                        DecodeUtil.handleOneByte(b2, resultArr, resultPos5);
                        resultPos5++;
                    }
                } else if (DecodeUtil.isTwoBytes(byte1)) {
                    if (address >= addressLimit) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    DecodeUtil.handleTwoBytes(byte1, UnsafeUtil.getByte(address), resultArr, resultPos);
                    resultPos2 = resultPos + 1;
                    address++;
                } else if (DecodeUtil.isThreeBytes(byte1)) {
                    if (address >= addressLimit - 1) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    long address4 = address + 1;
                    byte b3 = UnsafeUtil.getByte(address);
                    address = address4 + 1;
                    DecodeUtil.handleThreeBytes(byte1, b3, UnsafeUtil.getByte(address4), resultArr, resultPos);
                    resultPos2 = resultPos + 1;
                } else if (address >= addressLimit - 2) {
                    throw InvalidProtocolBufferException.invalidUtf8();
                } else {
                    long address5 = address + 1;
                    byte b4 = UnsafeUtil.getByte(address);
                    long address6 = address5 + 1;
                    DecodeUtil.handleFourBytes(byte1, b4, UnsafeUtil.getByte(address5), UnsafeUtil.getByte(address6), resultArr, resultPos);
                    resultPos2 = resultPos + 1 + 1;
                    address = address6 + 1;
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x016f, code lost:
            if (java.lang.Character.isSurrogatePair(r2, r17.charAt(r4 + 1)) == false) goto L_0x0171;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int encodeUtf8(java.lang.CharSequence r17, byte[] r18, int r19, int r20) {
            /*
                r16 = this;
                r0 = r19
                long r8 = (long) r0
                r0 = r20
                long r14 = (long) r0
                long r12 = r8 + r14
                int r5 = r17.length()
                r0 = r20
                if (r5 > r0) goto L_0x0019
                r0 = r18
                int r7 = r0.length
                int r7 = r7 - r20
                r0 = r19
                if (r7 >= r0) goto L_0x0048
            L_0x0019:
                java.lang.ArrayIndexOutOfBoundsException r7 = new java.lang.ArrayIndexOutOfBoundsException
                java.lang.StringBuilder r14 = new java.lang.StringBuilder
                r14.<init>()
                java.lang.String r15 = "Failed writing "
                java.lang.StringBuilder r14 = r14.append(r15)
                int r15 = r5 + -1
                r0 = r17
                char r15 = r0.charAt(r15)
                java.lang.StringBuilder r14 = r14.append(r15)
                java.lang.String r15 = " at index "
                java.lang.StringBuilder r14 = r14.append(r15)
                int r15 = r19 + r20
                java.lang.StringBuilder r14 = r14.append(r15)
                java.lang.String r14 = r14.toString()
                r7.<init>(r14)
                throw r7
            L_0x0048:
                r4 = 0
                r10 = r8
            L_0x004a:
                if (r4 >= r5) goto L_0x0064
                r0 = r17
                char r2 = r0.charAt(r4)
                r7 = 128(0x80, float:1.794E-43)
                if (r2 >= r7) goto L_0x0064
                r14 = 1
                long r8 = r10 + r14
                byte r7 = (byte) r2
                r0 = r18
                com.google.protobuf.UnsafeUtil.putByte(r0, r10, r7)
                int r4 = r4 + 1
                r10 = r8
                goto L_0x004a
            L_0x0064:
                if (r4 != r5) goto L_0x0069
                int r7 = (int) r10
                r8 = r10
            L_0x0068:
                return r7
            L_0x0069:
                if (r4 >= r5) goto L_0x019c
                r0 = r17
                char r2 = r0.charAt(r4)
                r7 = 128(0x80, float:1.794E-43)
                if (r2 >= r7) goto L_0x0087
                int r7 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
                if (r7 >= 0) goto L_0x0087
                r14 = 1
                long r8 = r10 + r14
                byte r7 = (byte) r2
                r0 = r18
                com.google.protobuf.UnsafeUtil.putByte(r0, r10, r7)
            L_0x0083:
                int r4 = r4 + 1
                r10 = r8
                goto L_0x0069
            L_0x0087:
                r7 = 2048(0x800, float:2.87E-42)
                if (r2 >= r7) goto L_0x00b1
                r14 = 2
                long r14 = r12 - r14
                int r7 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
                if (r7 > 0) goto L_0x00b1
                r14 = 1
                long r8 = r10 + r14
                int r7 = r2 >>> 6
                r7 = r7 | 960(0x3c0, float:1.345E-42)
                byte r7 = (byte) r7
                r0 = r18
                com.google.protobuf.UnsafeUtil.putByte(r0, r10, r7)
                r14 = 1
                long r10 = r8 + r14
                r7 = r2 & 63
                r7 = r7 | 128(0x80, float:1.794E-43)
                byte r7 = (byte) r7
                r0 = r18
                com.google.protobuf.UnsafeUtil.putByte(r0, r8, r7)
                r8 = r10
                goto L_0x0083
            L_0x00b1:
                r7 = 55296(0xd800, float:7.7486E-41)
                if (r2 < r7) goto L_0x00bb
                r7 = 57343(0xdfff, float:8.0355E-41)
                if (r7 >= r2) goto L_0x00f0
            L_0x00bb:
                r14 = 3
                long r14 = r12 - r14
                int r7 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
                if (r7 > 0) goto L_0x00f0
                r14 = 1
                long r8 = r10 + r14
                int r7 = r2 >>> 12
                r7 = r7 | 480(0x1e0, float:6.73E-43)
                byte r7 = (byte) r7
                r0 = r18
                com.google.protobuf.UnsafeUtil.putByte(r0, r10, r7)
                r14 = 1
                long r10 = r8 + r14
                int r7 = r2 >>> 6
                r7 = r7 & 63
                r7 = r7 | 128(0x80, float:1.794E-43)
                byte r7 = (byte) r7
                r0 = r18
                com.google.protobuf.UnsafeUtil.putByte(r0, r8, r7)
                r14 = 1
                long r8 = r10 + r14
                r7 = r2 & 63
                r7 = r7 | 128(0x80, float:1.794E-43)
                byte r7 = (byte) r7
                r0 = r18
                com.google.protobuf.UnsafeUtil.putByte(r0, r10, r7)
                goto L_0x0083
            L_0x00f0:
                r14 = 4
                long r14 = r12 - r14
                int r7 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1))
                if (r7 > 0) goto L_0x0155
                int r7 = r4 + 1
                if (r7 == r5) goto L_0x010a
                int r4 = r4 + 1
                r0 = r17
                char r6 = r0.charAt(r4)
                boolean r7 = java.lang.Character.isSurrogatePair(r2, r6)
                if (r7 != 0) goto L_0x0112
            L_0x010a:
                com.google.protobuf.Utf8$UnpairedSurrogateException r7 = new com.google.protobuf.Utf8$UnpairedSurrogateException
                int r14 = r4 + -1
                r7.<init>(r14, r5)
                throw r7
            L_0x0112:
                int r3 = java.lang.Character.toCodePoint(r2, r6)
                r14 = 1
                long r8 = r10 + r14
                int r7 = r3 >>> 18
                r7 = r7 | 240(0xf0, float:3.36E-43)
                byte r7 = (byte) r7
                r0 = r18
                com.google.protobuf.UnsafeUtil.putByte(r0, r10, r7)
                r14 = 1
                long r10 = r8 + r14
                int r7 = r3 >>> 12
                r7 = r7 & 63
                r7 = r7 | 128(0x80, float:1.794E-43)
                byte r7 = (byte) r7
                r0 = r18
                com.google.protobuf.UnsafeUtil.putByte(r0, r8, r7)
                r14 = 1
                long r8 = r10 + r14
                int r7 = r3 >>> 6
                r7 = r7 & 63
                r7 = r7 | 128(0x80, float:1.794E-43)
                byte r7 = (byte) r7
                r0 = r18
                com.google.protobuf.UnsafeUtil.putByte(r0, r10, r7)
                r14 = 1
                long r10 = r8 + r14
                r7 = r3 & 63
                r7 = r7 | 128(0x80, float:1.794E-43)
                byte r7 = (byte) r7
                r0 = r18
                com.google.protobuf.UnsafeUtil.putByte(r0, r8, r7)
                r8 = r10
                goto L_0x0083
            L_0x0155:
                r7 = 55296(0xd800, float:7.7486E-41)
                if (r7 > r2) goto L_0x0177
                r7 = 57343(0xdfff, float:8.0355E-41)
                if (r2 > r7) goto L_0x0177
                int r7 = r4 + 1
                if (r7 == r5) goto L_0x0171
                int r7 = r4 + 1
                r0 = r17
                char r7 = r0.charAt(r7)
                boolean r7 = java.lang.Character.isSurrogatePair(r2, r7)
                if (r7 != 0) goto L_0x0177
            L_0x0171:
                com.google.protobuf.Utf8$UnpairedSurrogateException r7 = new com.google.protobuf.Utf8$UnpairedSurrogateException
                r7.<init>(r4, r5)
                throw r7
            L_0x0177:
                java.lang.ArrayIndexOutOfBoundsException r7 = new java.lang.ArrayIndexOutOfBoundsException
                java.lang.StringBuilder r14 = new java.lang.StringBuilder
                r14.<init>()
                java.lang.String r15 = "Failed writing "
                java.lang.StringBuilder r14 = r14.append(r15)
                java.lang.StringBuilder r14 = r14.append(r2)
                java.lang.String r15 = " at index "
                java.lang.StringBuilder r14 = r14.append(r15)
                java.lang.StringBuilder r14 = r14.append(r10)
                java.lang.String r14 = r14.toString()
                r7.<init>(r14)
                throw r7
            L_0x019c:
                int r7 = (int) r10
                r8 = r10
                goto L_0x0068
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.encodeUtf8(java.lang.CharSequence, byte[], int, int):int");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0174, code lost:
            if (java.lang.Character.isSurrogatePair(r4, r21.charAt(r6 + 1)) == false) goto L_0x0176;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void encodeUtf8Direct(java.lang.CharSequence r21, java.nio.ByteBuffer r22) {
            /*
                r20 = this;
                long r2 = com.google.protobuf.UnsafeUtil.addressOffset(r22)
                int r9 = r22.position()
                long r0 = (long) r9
                r16 = r0
                long r10 = r2 + r16
                int r9 = r22.limit()
                long r0 = (long) r9
                r16 = r0
                long r14 = r2 + r16
                int r7 = r21.length()
                long r0 = (long) r7
                r16 = r0
                long r18 = r14 - r10
                int r9 = (r16 > r18 ? 1 : (r16 == r18 ? 0 : -1))
                if (r9 <= 0) goto L_0x0058
                java.lang.ArrayIndexOutOfBoundsException r9 = new java.lang.ArrayIndexOutOfBoundsException
                java.lang.StringBuilder r16 = new java.lang.StringBuilder
                r16.<init>()
                java.lang.String r17 = "Failed writing "
                java.lang.StringBuilder r16 = r16.append(r17)
                int r17 = r7 + -1
                r0 = r21
                r1 = r17
                char r17 = r0.charAt(r1)
                java.lang.StringBuilder r16 = r16.append(r17)
                java.lang.String r17 = " at index "
                java.lang.StringBuilder r16 = r16.append(r17)
                int r17 = r22.limit()
                java.lang.StringBuilder r16 = r16.append(r17)
                java.lang.String r16 = r16.toString()
                r0 = r16
                r9.<init>(r0)
                throw r9
            L_0x0058:
                r6 = 0
                r12 = r10
            L_0x005a:
                if (r6 >= r7) goto L_0x0072
                r0 = r21
                char r4 = r0.charAt(r6)
                r9 = 128(0x80, float:1.794E-43)
                if (r4 >= r9) goto L_0x0072
                r16 = 1
                long r10 = r12 + r16
                byte r9 = (byte) r4
                com.google.protobuf.UnsafeUtil.putByte(r12, r9)
                int r6 = r6 + 1
                r12 = r10
                goto L_0x005a
            L_0x0072:
                if (r6 != r7) goto L_0x0080
                long r16 = r12 - r2
                r0 = r16
                int r9 = (int) r0
                r0 = r22
                r0.position(r9)
                r10 = r12
            L_0x007f:
                return
            L_0x0080:
                if (r6 >= r7) goto L_0x01a7
                r0 = r21
                char r4 = r0.charAt(r6)
                r9 = 128(0x80, float:1.794E-43)
                if (r4 >= r9) goto L_0x009c
                int r9 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
                if (r9 >= 0) goto L_0x009c
                r16 = 1
                long r10 = r12 + r16
                byte r9 = (byte) r4
                com.google.protobuf.UnsafeUtil.putByte(r12, r9)
            L_0x0098:
                int r6 = r6 + 1
                r12 = r10
                goto L_0x0080
            L_0x009c:
                r9 = 2048(0x800, float:2.87E-42)
                if (r4 >= r9) goto L_0x00c2
                r16 = 2
                long r16 = r14 - r16
                int r9 = (r12 > r16 ? 1 : (r12 == r16 ? 0 : -1))
                if (r9 > 0) goto L_0x00c2
                r16 = 1
                long r10 = r12 + r16
                int r9 = r4 >>> 6
                r9 = r9 | 960(0x3c0, float:1.345E-42)
                byte r9 = (byte) r9
                com.google.protobuf.UnsafeUtil.putByte(r12, r9)
                r16 = 1
                long r12 = r10 + r16
                r9 = r4 & 63
                r9 = r9 | 128(0x80, float:1.794E-43)
                byte r9 = (byte) r9
                com.google.protobuf.UnsafeUtil.putByte(r10, r9)
                r10 = r12
                goto L_0x0098
            L_0x00c2:
                r9 = 55296(0xd800, float:7.7486E-41)
                if (r4 < r9) goto L_0x00cc
                r9 = 57343(0xdfff, float:8.0355E-41)
                if (r9 >= r4) goto L_0x00fb
            L_0x00cc:
                r16 = 3
                long r16 = r14 - r16
                int r9 = (r12 > r16 ? 1 : (r12 == r16 ? 0 : -1))
                if (r9 > 0) goto L_0x00fb
                r16 = 1
                long r10 = r12 + r16
                int r9 = r4 >>> 12
                r9 = r9 | 480(0x1e0, float:6.73E-43)
                byte r9 = (byte) r9
                com.google.protobuf.UnsafeUtil.putByte(r12, r9)
                r16 = 1
                long r12 = r10 + r16
                int r9 = r4 >>> 6
                r9 = r9 & 63
                r9 = r9 | 128(0x80, float:1.794E-43)
                byte r9 = (byte) r9
                com.google.protobuf.UnsafeUtil.putByte(r10, r9)
                r16 = 1
                long r10 = r12 + r16
                r9 = r4 & 63
                r9 = r9 | 128(0x80, float:1.794E-43)
                byte r9 = (byte) r9
                com.google.protobuf.UnsafeUtil.putByte(r12, r9)
                goto L_0x0098
            L_0x00fb:
                r16 = 4
                long r16 = r14 - r16
                int r9 = (r12 > r16 ? 1 : (r12 == r16 ? 0 : -1))
                if (r9 > 0) goto L_0x015a
                int r9 = r6 + 1
                if (r9 == r7) goto L_0x0115
                int r6 = r6 + 1
                r0 = r21
                char r8 = r0.charAt(r6)
                boolean r9 = java.lang.Character.isSurrogatePair(r4, r8)
                if (r9 != 0) goto L_0x011f
            L_0x0115:
                com.google.protobuf.Utf8$UnpairedSurrogateException r9 = new com.google.protobuf.Utf8$UnpairedSurrogateException
                int r16 = r6 + -1
                r0 = r16
                r9.<init>(r0, r7)
                throw r9
            L_0x011f:
                int r5 = java.lang.Character.toCodePoint(r4, r8)
                r16 = 1
                long r10 = r12 + r16
                int r9 = r5 >>> 18
                r9 = r9 | 240(0xf0, float:3.36E-43)
                byte r9 = (byte) r9
                com.google.protobuf.UnsafeUtil.putByte(r12, r9)
                r16 = 1
                long r12 = r10 + r16
                int r9 = r5 >>> 12
                r9 = r9 & 63
                r9 = r9 | 128(0x80, float:1.794E-43)
                byte r9 = (byte) r9
                com.google.protobuf.UnsafeUtil.putByte(r10, r9)
                r16 = 1
                long r10 = r12 + r16
                int r9 = r5 >>> 6
                r9 = r9 & 63
                r9 = r9 | 128(0x80, float:1.794E-43)
                byte r9 = (byte) r9
                com.google.protobuf.UnsafeUtil.putByte(r12, r9)
                r16 = 1
                long r12 = r10 + r16
                r9 = r5 & 63
                r9 = r9 | 128(0x80, float:1.794E-43)
                byte r9 = (byte) r9
                com.google.protobuf.UnsafeUtil.putByte(r10, r9)
                r10 = r12
                goto L_0x0098
            L_0x015a:
                r9 = 55296(0xd800, float:7.7486E-41)
                if (r9 > r4) goto L_0x017c
                r9 = 57343(0xdfff, float:8.0355E-41)
                if (r4 > r9) goto L_0x017c
                int r9 = r6 + 1
                if (r9 == r7) goto L_0x0176
                int r9 = r6 + 1
                r0 = r21
                char r9 = r0.charAt(r9)
                boolean r9 = java.lang.Character.isSurrogatePair(r4, r9)
                if (r9 != 0) goto L_0x017c
            L_0x0176:
                com.google.protobuf.Utf8$UnpairedSurrogateException r9 = new com.google.protobuf.Utf8$UnpairedSurrogateException
                r9.<init>(r6, r7)
                throw r9
            L_0x017c:
                java.lang.ArrayIndexOutOfBoundsException r9 = new java.lang.ArrayIndexOutOfBoundsException
                java.lang.StringBuilder r16 = new java.lang.StringBuilder
                r16.<init>()
                java.lang.String r17 = "Failed writing "
                java.lang.StringBuilder r16 = r16.append(r17)
                r0 = r16
                java.lang.StringBuilder r16 = r0.append(r4)
                java.lang.String r17 = " at index "
                java.lang.StringBuilder r16 = r16.append(r17)
                r0 = r16
                java.lang.StringBuilder r16 = r0.append(r12)
                java.lang.String r16 = r16.toString()
                r0 = r16
                r9.<init>(r0)
                throw r9
            L_0x01a7:
                long r16 = r12 - r2
                r0 = r16
                int r9 = (int) r0
                r0 = r22
                r0.position(r9)
                r10 = r12
                goto L_0x007f
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.encodeUtf8Direct(java.lang.CharSequence, java.nio.ByteBuffer):void");
        }

        private static int unsafeEstimateConsecutiveAscii(byte[] bytes, long offset, int maxChars) {
            if (maxChars < 16) {
                return 0;
            }
            int i = 0;
            long offset2 = offset;
            while (i < maxChars) {
                long offset3 = offset2 + 1;
                if (UnsafeUtil.getByte(bytes, offset2) < 0) {
                    return i;
                }
                i++;
                offset2 = offset3;
            }
            return maxChars;
        }

        private static int unsafeEstimateConsecutiveAscii(long address, int maxChars) {
            int remaining = maxChars;
            if (remaining < 16) {
                return 0;
            }
            int unaligned = 8 - (((int) address) & 7);
            int j = unaligned;
            long address2 = address;
            while (j > 0) {
                long address3 = address2 + 1;
                if (UnsafeUtil.getByte(address2) < 0) {
                    return unaligned - j;
                }
                j--;
                address2 = address3;
            }
            int remaining2 = remaining - unaligned;
            long address4 = address2;
            while (remaining2 >= 8 && (UnsafeUtil.getLong(address4) & Utf8.ASCII_MASK_LONG) == 0) {
                address4 += 8;
                remaining2 -= 8;
            }
            return maxChars - remaining2;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0075, code lost:
            return -1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x00b1, code lost:
            return -1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static int partialIsValidUtf8(byte[] r9, long r10, int r12) {
            /*
                int r4 = unsafeEstimateConsecutiveAscii(r9, r10, r12)
                int r12 = r12 - r4
                long r6 = (long) r4
                long r10 = r10 + r6
            L_0x0007:
                r0 = 0
                r2 = r10
            L_0x0009:
                if (r12 <= 0) goto L_0x001a
                r6 = 1
                long r10 = r2 + r6
                byte r0 = com.google.protobuf.UnsafeUtil.getByte(r9, r2)
                if (r0 < 0) goto L_0x0019
                int r12 = r12 + -1
                r2 = r10
                goto L_0x0009
            L_0x0019:
                r2 = r10
            L_0x001a:
                if (r12 != 0) goto L_0x001f
                r0 = 0
                r10 = r2
            L_0x001e:
                return r0
            L_0x001f:
                int r12 = r12 + -1
                r5 = -32
                if (r0 >= r5) goto L_0x003d
                if (r12 != 0) goto L_0x0029
                r10 = r2
                goto L_0x001e
            L_0x0029:
                int r12 = r12 + -1
                r5 = -62
                if (r0 < r5) goto L_0x00b6
                r6 = 1
                long r10 = r2 + r6
                byte r5 = com.google.protobuf.UnsafeUtil.getByte(r9, r2)
                r6 = -65
                if (r5 <= r6) goto L_0x0007
            L_0x003b:
                r0 = -1
                goto L_0x001e
            L_0x003d:
                r5 = -16
                if (r0 >= r5) goto L_0x0079
                r5 = 2
                if (r12 >= r5) goto L_0x004a
                int r0 = unsafeIncompleteStateFor(r9, r0, r2, r12)
                r10 = r2
                goto L_0x001e
            L_0x004a:
                int r12 = r12 + -2
                r6 = 1
                long r10 = r2 + r6
                byte r1 = com.google.protobuf.UnsafeUtil.getByte(r9, r2)
                r5 = -65
                if (r1 > r5) goto L_0x0075
                r5 = -32
                if (r0 != r5) goto L_0x0060
                r5 = -96
                if (r1 < r5) goto L_0x0075
            L_0x0060:
                r5 = -19
                if (r0 != r5) goto L_0x0068
                r5 = -96
                if (r1 >= r5) goto L_0x0075
            L_0x0068:
                r6 = 1
                long r2 = r10 + r6
                byte r5 = com.google.protobuf.UnsafeUtil.getByte(r9, r10)
                r6 = -65
                if (r5 <= r6) goto L_0x0077
                r10 = r2
            L_0x0075:
                r0 = -1
                goto L_0x001e
            L_0x0077:
                r10 = r2
                goto L_0x0007
            L_0x0079:
                r5 = 3
                if (r12 >= r5) goto L_0x0082
                int r0 = unsafeIncompleteStateFor(r9, r0, r2, r12)
                r10 = r2
                goto L_0x001e
            L_0x0082:
                int r12 = r12 + -3
                r6 = 1
                long r10 = r2 + r6
                byte r1 = com.google.protobuf.UnsafeUtil.getByte(r9, r2)
                r5 = -65
                if (r1 > r5) goto L_0x00b1
                int r5 = r0 << 28
                int r6 = r1 + 112
                int r5 = r5 + r6
                int r5 = r5 >> 30
                if (r5 != 0) goto L_0x00b1
                r6 = 1
                long r2 = r10 + r6
                byte r5 = com.google.protobuf.UnsafeUtil.getByte(r9, r10)
                r6 = -65
                if (r5 > r6) goto L_0x00b4
                r6 = 1
                long r10 = r2 + r6
                byte r5 = com.google.protobuf.UnsafeUtil.getByte(r9, r2)
                r6 = -65
                if (r5 <= r6) goto L_0x0007
            L_0x00b1:
                r0 = -1
                goto L_0x001e
            L_0x00b4:
                r10 = r2
                goto L_0x00b1
            L_0x00b6:
                r10 = r2
                goto L_0x003b
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.partialIsValidUtf8(byte[], long, int):int");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
            return -1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:72:?, code lost:
            return -1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static int partialIsValidUtf8(long r16, int r18) {
            /*
                r14 = -32
                r13 = -96
                r7 = -1
                r12 = -65
                r10 = 1
                int r6 = unsafeEstimateConsecutiveAscii(r16, r18)
                long r8 = (long) r6
                long r16 = r16 + r8
                int r18 = r18 - r6
            L_0x0012:
                r4 = 0
                r2 = r16
            L_0x0015:
                if (r18 <= 0) goto L_0x0026
                long r16 = r2 + r10
                byte r4 = com.google.protobuf.UnsafeUtil.getByte(r2)
                if (r4 < 0) goto L_0x0024
                int r18 = r18 + -1
                r2 = r16
                goto L_0x0015
            L_0x0024:
                r2 = r16
            L_0x0026:
                if (r18 != 0) goto L_0x002a
                r4 = 0
            L_0x0029:
                return r4
            L_0x002a:
                int r18 = r18 + -1
                if (r4 >= r14) goto L_0x0040
                if (r18 == 0) goto L_0x0029
                int r18 = r18 + -1
                r8 = -62
                if (r4 < r8) goto L_0x00a7
                long r16 = r2 + r10
                byte r8 = com.google.protobuf.UnsafeUtil.getByte(r2)
                if (r8 <= r12) goto L_0x0012
            L_0x003e:
                r4 = r7
                goto L_0x0029
            L_0x0040:
                r8 = -16
                if (r4 >= r8) goto L_0x0073
                r8 = 2
                r0 = r18
                if (r0 >= r8) goto L_0x0050
                r0 = r18
                int r4 = unsafeIncompleteStateFor(r2, r4, r0)
                goto L_0x0029
            L_0x0050:
                int r18 = r18 + -2
                long r16 = r2 + r10
                byte r5 = com.google.protobuf.UnsafeUtil.getByte(r2)
                if (r5 > r12) goto L_0x006e
                if (r4 != r14) goto L_0x005e
                if (r5 < r13) goto L_0x006e
            L_0x005e:
                r8 = -19
                if (r4 != r8) goto L_0x0064
                if (r5 >= r13) goto L_0x006e
            L_0x0064:
                long r2 = r16 + r10
                byte r8 = com.google.protobuf.UnsafeUtil.getByte(r16)
                if (r8 <= r12) goto L_0x0070
                r16 = r2
            L_0x006e:
                r4 = r7
                goto L_0x0029
            L_0x0070:
                r16 = r2
                goto L_0x0012
            L_0x0073:
                r8 = 3
                r0 = r18
                if (r0 >= r8) goto L_0x007f
                r0 = r18
                int r4 = unsafeIncompleteStateFor(r2, r4, r0)
                goto L_0x0029
            L_0x007f:
                int r18 = r18 + -3
                long r16 = r2 + r10
                byte r5 = com.google.protobuf.UnsafeUtil.getByte(r2)
                if (r5 > r12) goto L_0x00a2
                int r8 = r4 << 28
                int r9 = r5 + 112
                int r8 = r8 + r9
                int r8 = r8 >> 30
                if (r8 != 0) goto L_0x00a2
                long r2 = r16 + r10
                byte r8 = com.google.protobuf.UnsafeUtil.getByte(r16)
                if (r8 > r12) goto L_0x00a4
                long r16 = r2 + r10
                byte r8 = com.google.protobuf.UnsafeUtil.getByte(r2)
                if (r8 <= r12) goto L_0x0012
            L_0x00a2:
                r4 = r7
                goto L_0x0029
            L_0x00a4:
                r16 = r2
                goto L_0x00a2
            L_0x00a7:
                r16 = r2
                goto L_0x003e
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.partialIsValidUtf8(long, int):int");
        }

        private static int unsafeIncompleteStateFor(byte[] bytes, int byte1, long offset, int remaining) {
            switch (remaining) {
                case 0:
                    return Utf8.incompleteStateFor(byte1);
                case 1:
                    return Utf8.incompleteStateFor(byte1, UnsafeUtil.getByte(bytes, offset));
                case 2:
                    return Utf8.incompleteStateFor(byte1, UnsafeUtil.getByte(bytes, offset), UnsafeUtil.getByte(bytes, 1 + offset));
                default:
                    throw new AssertionError();
            }
        }

        private static int unsafeIncompleteStateFor(long address, int byte1, int remaining) {
            switch (remaining) {
                case 0:
                    return Utf8.incompleteStateFor(byte1);
                case 1:
                    return Utf8.incompleteStateFor(byte1, UnsafeUtil.getByte(address));
                case 2:
                    return Utf8.incompleteStateFor(byte1, UnsafeUtil.getByte(address), UnsafeUtil.getByte(1 + address));
                default:
                    throw new AssertionError();
            }
        }
    }

    private static class DecodeUtil {
        private DecodeUtil() {
        }

        /* access modifiers changed from: private */
        public static boolean isOneByte(byte b) {
            return b >= 0;
        }

        /* access modifiers changed from: private */
        public static boolean isTwoBytes(byte b) {
            return b < -32;
        }

        /* access modifiers changed from: private */
        public static boolean isThreeBytes(byte b) {
            return b < -16;
        }

        /* access modifiers changed from: private */
        public static void handleOneByte(byte byte1, char[] resultArr, int resultPos) {
            resultArr[resultPos] = (char) byte1;
        }

        /* access modifiers changed from: private */
        public static void handleTwoBytes(byte byte1, byte byte2, char[] resultArr, int resultPos) throws InvalidProtocolBufferException {
            if (byte1 < -62 || isNotTrailingByte(byte2)) {
                throw InvalidProtocolBufferException.invalidUtf8();
            }
            resultArr[resultPos] = (char) (((byte1 & 31) << 6) | trailingByteValue(byte2));
        }

        /* access modifiers changed from: private */
        public static void handleThreeBytes(byte byte1, byte byte2, byte byte3, char[] resultArr, int resultPos) throws InvalidProtocolBufferException {
            if (isNotTrailingByte(byte2) || ((byte1 == -32 && byte2 < -96) || ((byte1 == -19 && byte2 >= -96) || isNotTrailingByte(byte3)))) {
                throw InvalidProtocolBufferException.invalidUtf8();
            }
            resultArr[resultPos] = (char) (((byte1 & 15) << 12) | (trailingByteValue(byte2) << 6) | trailingByteValue(byte3));
        }

        /* access modifiers changed from: private */
        public static void handleFourBytes(byte byte1, byte byte2, byte byte3, byte byte4, char[] resultArr, int resultPos) throws InvalidProtocolBufferException {
            if (isNotTrailingByte(byte2) || (((byte1 << 28) + (byte2 + 112)) >> 30) != 0 || isNotTrailingByte(byte3) || isNotTrailingByte(byte4)) {
                throw InvalidProtocolBufferException.invalidUtf8();
            }
            int codepoint = ((byte1 & 7) << 18) | (trailingByteValue(byte2) << 12) | (trailingByteValue(byte3) << 6) | trailingByteValue(byte4);
            resultArr[resultPos] = highSurrogate(codepoint);
            resultArr[resultPos + 1] = lowSurrogate(codepoint);
        }

        private static boolean isNotTrailingByte(byte b) {
            return b > -65;
        }

        private static int trailingByteValue(byte b) {
            return b & 63;
        }

        private static char highSurrogate(int codePoint) {
            return (char) (55232 + (codePoint >>> 10));
        }

        private static char lowSurrogate(int codePoint) {
            return (char) (56320 + (codePoint & DJIDiagnosticsError.Camera.INTERNAL_STORAGE_INVALID));
        }
    }

    private Utf8() {
    }
}
