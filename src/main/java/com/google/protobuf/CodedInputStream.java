package com.google.protobuf;

import com.google.protobuf.MessageLite;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.ByteCompanionObject;
import org.bouncycastle.asn1.eac.CertificateBody;

public abstract class CodedInputStream {
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private static final int DEFAULT_RECURSION_LIMIT = 100;
    private static final int DEFAULT_SIZE_LIMIT = Integer.MAX_VALUE;
    int recursionDepth;
    int recursionLimit;
    private boolean shouldDiscardUnknownFields;
    int sizeLimit;
    CodedInputStreamReader wrapper;

    public abstract void checkLastTagWas(int i) throws InvalidProtocolBufferException;

    public abstract void enableAliasing(boolean z);

    public abstract int getBytesUntilLimit();

    public abstract int getLastTag();

    public abstract int getTotalBytesRead();

    public abstract boolean isAtEnd() throws IOException;

    public abstract void popLimit(int i);

    public abstract int pushLimit(int i) throws InvalidProtocolBufferException;

    public abstract boolean readBool() throws IOException;

    public abstract byte[] readByteArray() throws IOException;

    public abstract ByteBuffer readByteBuffer() throws IOException;

    public abstract ByteString readBytes() throws IOException;

    public abstract double readDouble() throws IOException;

    public abstract int readEnum() throws IOException;

    public abstract int readFixed32() throws IOException;

    public abstract long readFixed64() throws IOException;

    public abstract float readFloat() throws IOException;

    public abstract <T extends MessageLite> T readGroup(int i, Parser<T> parser, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    public abstract void readGroup(int i, MessageLite.Builder builder, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    public abstract int readInt32() throws IOException;

    public abstract long readInt64() throws IOException;

    public abstract <T extends MessageLite> T readMessage(Parser<T> parser, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    public abstract void readMessage(MessageLite.Builder builder, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    public abstract byte readRawByte() throws IOException;

    public abstract byte[] readRawBytes(int i) throws IOException;

    public abstract int readRawLittleEndian32() throws IOException;

    public abstract long readRawLittleEndian64() throws IOException;

    public abstract int readRawVarint32() throws IOException;

    public abstract long readRawVarint64() throws IOException;

    /* access modifiers changed from: package-private */
    public abstract long readRawVarint64SlowPath() throws IOException;

    public abstract int readSFixed32() throws IOException;

    public abstract long readSFixed64() throws IOException;

    public abstract int readSInt32() throws IOException;

    public abstract long readSInt64() throws IOException;

    public abstract String readString() throws IOException;

    public abstract String readStringRequireUtf8() throws IOException;

    public abstract int readTag() throws IOException;

    public abstract int readUInt32() throws IOException;

    public abstract long readUInt64() throws IOException;

    @Deprecated
    public abstract void readUnknownGroup(int i, MessageLite.Builder builder) throws IOException;

    public abstract void resetSizeCounter();

    public abstract boolean skipField(int i) throws IOException;

    @Deprecated
    public abstract boolean skipField(int i, CodedOutputStream codedOutputStream) throws IOException;

    public abstract void skipMessage() throws IOException;

    public abstract void skipMessage(CodedOutputStream codedOutputStream) throws IOException;

    public abstract void skipRawBytes(int i) throws IOException;

    public static CodedInputStream newInstance(InputStream input) {
        return newInstance(input, 4096);
    }

    public static CodedInputStream newInstance(InputStream input, int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("bufferSize must be > 0");
        } else if (input == null) {
            return newInstance(Internal.EMPTY_BYTE_ARRAY);
        } else {
            return new StreamDecoder(input, bufferSize);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.CodedInputStream.newInstance(java.lang.Iterable<java.nio.ByteBuffer>, boolean):com.google.protobuf.CodedInputStream
     arg types: [java.lang.Iterable<java.nio.ByteBuffer>, int]
     candidates:
      com.google.protobuf.CodedInputStream.newInstance(java.io.InputStream, int):com.google.protobuf.CodedInputStream
      com.google.protobuf.CodedInputStream.newInstance(java.nio.ByteBuffer, boolean):com.google.protobuf.CodedInputStream
      com.google.protobuf.CodedInputStream.newInstance(java.lang.Iterable<java.nio.ByteBuffer>, boolean):com.google.protobuf.CodedInputStream */
    public static CodedInputStream newInstance(Iterable<ByteBuffer> input) {
        if (!UnsafeDirectNioDecoder.isSupported()) {
            return newInstance(new IterableByteBufferInputStream(input));
        }
        return newInstance(input, false);
    }

    static CodedInputStream newInstance(Iterable<ByteBuffer> bufs, boolean bufferIsImmutable) {
        int flag = 0;
        int totalSize = 0;
        for (ByteBuffer buf : bufs) {
            totalSize += buf.remaining();
            if (buf.hasArray()) {
                flag |= 1;
            } else if (buf.isDirect()) {
                flag |= 2;
            } else {
                flag |= 4;
            }
        }
        if (flag == 2) {
            return new IterableDirectByteBufferDecoder(bufs, totalSize, bufferIsImmutable);
        }
        return newInstance(new IterableByteBufferInputStream(bufs));
    }

    public static CodedInputStream newInstance(byte[] buf) {
        return newInstance(buf, 0, buf.length);
    }

    public static CodedInputStream newInstance(byte[] buf, int off, int len) {
        return newInstance(buf, off, len, false);
    }

    static CodedInputStream newInstance(byte[] buf, int off, int len, boolean bufferIsImmutable) {
        ArrayDecoder result = new ArrayDecoder(buf, off, len, bufferIsImmutable);
        try {
            result.pushLimit(len);
            return result;
        } catch (InvalidProtocolBufferException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: com.google.protobuf.CodedInputStream.newInstance(java.nio.ByteBuffer, boolean):com.google.protobuf.CodedInputStream
     arg types: [java.nio.ByteBuffer, int]
     candidates:
      com.google.protobuf.CodedInputStream.newInstance(java.io.InputStream, int):com.google.protobuf.CodedInputStream
      com.google.protobuf.CodedInputStream.newInstance(java.lang.Iterable<java.nio.ByteBuffer>, boolean):com.google.protobuf.CodedInputStream
      com.google.protobuf.CodedInputStream.newInstance(java.nio.ByteBuffer, boolean):com.google.protobuf.CodedInputStream */
    public static CodedInputStream newInstance(ByteBuffer buf) {
        return newInstance(buf, false);
    }

    static CodedInputStream newInstance(ByteBuffer buf, boolean bufferIsImmutable) {
        if (buf.hasArray()) {
            return newInstance(buf.array(), buf.arrayOffset() + buf.position(), buf.remaining(), bufferIsImmutable);
        }
        if (buf.isDirect() && UnsafeDirectNioDecoder.isSupported()) {
            return new UnsafeDirectNioDecoder(buf, bufferIsImmutable);
        }
        byte[] buffer = new byte[buf.remaining()];
        buf.duplicate().get(buffer);
        return newInstance(buffer, 0, buffer.length, true);
    }

    private CodedInputStream() {
        this.recursionLimit = 100;
        this.sizeLimit = Integer.MAX_VALUE;
        this.shouldDiscardUnknownFields = false;
    }

    public final int setRecursionLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Recursion limit cannot be negative: " + limit);
        }
        int oldLimit = this.recursionLimit;
        this.recursionLimit = limit;
        return oldLimit;
    }

    public final int setSizeLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Size limit cannot be negative: " + limit);
        }
        int oldLimit = this.sizeLimit;
        this.sizeLimit = limit;
        return oldLimit;
    }

    /* access modifiers changed from: package-private */
    public final void discardUnknownFields() {
        this.shouldDiscardUnknownFields = true;
    }

    /* access modifiers changed from: package-private */
    public final void unsetDiscardUnknownFields() {
        this.shouldDiscardUnknownFields = false;
    }

    /* access modifiers changed from: package-private */
    public final boolean shouldDiscardUnknownFields() {
        return this.shouldDiscardUnknownFields;
    }

    public static int decodeZigZag32(int n) {
        return (n >>> 1) ^ (-(n & 1));
    }

    public static long decodeZigZag64(long n) {
        return (n >>> 1) ^ (-(1 & n));
    }

    public static int readRawVarint32(int firstByte, InputStream input) throws IOException {
        if ((firstByte & 128) == 0) {
            return firstByte;
        }
        int result = firstByte & CertificateBody.profileType;
        int offset = 7;
        while (offset < 32) {
            int b = input.read();
            if (b == -1) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            result |= (b & CertificateBody.profileType) << offset;
            if ((b & 128) == 0) {
                return result;
            }
            offset += 7;
        }
        while (offset < 64) {
            int b2 = input.read();
            if (b2 == -1) {
                throw InvalidProtocolBufferException.truncatedMessage();
            } else if ((b2 & 128) == 0) {
                return result;
            } else {
                offset += 7;
            }
        }
        throw InvalidProtocolBufferException.malformedVarint();
    }

    static int readRawVarint32(InputStream input) throws IOException {
        int firstByte = input.read();
        if (firstByte != -1) {
            return readRawVarint32(firstByte, input);
        }
        throw InvalidProtocolBufferException.truncatedMessage();
    }

    private static final class ArrayDecoder extends CodedInputStream {
        private final byte[] buffer;
        private int bufferSizeAfterLimit;
        private int currentLimit;
        private boolean enableAliasing;
        private final boolean immutable;
        private int lastTag;
        private int limit;
        private int pos;
        private int startPos;

        private ArrayDecoder(byte[] buffer2, int offset, int len, boolean immutable2) {
            super();
            this.currentLimit = Integer.MAX_VALUE;
            this.buffer = buffer2;
            this.limit = offset + len;
            this.pos = offset;
            this.startPos = this.pos;
            this.immutable = immutable2;
        }

        public int readTag() throws IOException {
            if (isAtEnd()) {
                this.lastTag = 0;
                return 0;
            }
            this.lastTag = readRawVarint32();
            if (WireFormat.getTagFieldNumber(this.lastTag) != 0) {
                return this.lastTag;
            }
            throw InvalidProtocolBufferException.invalidTag();
        }

        public void checkLastTagWas(int value) throws InvalidProtocolBufferException {
            if (this.lastTag != value) {
                throw InvalidProtocolBufferException.invalidEndTag();
            }
        }

        public int getLastTag() {
            return this.lastTag;
        }

        public boolean skipField(int tag) throws IOException {
            switch (WireFormat.getTagWireType(tag)) {
                case 0:
                    skipRawVarint();
                    return true;
                case 1:
                    skipRawBytes(8);
                    return true;
                case 2:
                    skipRawBytes(readRawVarint32());
                    return true;
                case 3:
                    skipMessage();
                    checkLastTagWas(WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4));
                    return true;
                case 4:
                    return false;
                case 5:
                    skipRawBytes(4);
                    return true;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        /* JADX INFO: Multiple debug info for r2v1 com.google.protobuf.ByteString: [D('value' long), D('value' com.google.protobuf.ByteString)] */
        public boolean skipField(int tag, CodedOutputStream output) throws IOException {
            switch (WireFormat.getTagWireType(tag)) {
                case 0:
                    long value = readInt64();
                    output.writeRawVarint32(tag);
                    output.writeUInt64NoTag(value);
                    return true;
                case 1:
                    long value2 = readRawLittleEndian64();
                    output.writeRawVarint32(tag);
                    output.writeFixed64NoTag(value2);
                    return true;
                case 2:
                    ByteString value3 = readBytes();
                    output.writeRawVarint32(tag);
                    output.writeBytesNoTag(value3);
                    return true;
                case 3:
                    output.writeRawVarint32(tag);
                    skipMessage(output);
                    int endtag = WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4);
                    checkLastTagWas(endtag);
                    output.writeRawVarint32(endtag);
                    return true;
                case 4:
                    return false;
                case 5:
                    int value4 = readRawLittleEndian32();
                    output.writeRawVarint32(tag);
                    output.writeFixed32NoTag(value4);
                    return true;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        public void skipMessage() throws IOException {
            int tag;
            do {
                tag = readTag();
                if (tag == 0) {
                    return;
                }
            } while (skipField(tag));
        }

        public void skipMessage(CodedOutputStream output) throws IOException {
            int tag;
            do {
                tag = readTag();
                if (tag == 0) {
                    return;
                }
            } while (skipField(tag, output));
        }

        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readRawLittleEndian64());
        }

        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readRawLittleEndian32());
        }

        public long readUInt64() throws IOException {
            return readRawVarint64();
        }

        public long readInt64() throws IOException {
            return readRawVarint64();
        }

        public int readInt32() throws IOException {
            return readRawVarint32();
        }

        public long readFixed64() throws IOException {
            return readRawLittleEndian64();
        }

        public int readFixed32() throws IOException {
            return readRawLittleEndian32();
        }

        public boolean readBool() throws IOException {
            return readRawVarint64() != 0;
        }

        public String readString() throws IOException {
            int size = readRawVarint32();
            if (size > 0 && size <= this.limit - this.pos) {
                String result = new String(this.buffer, this.pos, size, Internal.UTF_8);
                this.pos += size;
                return result;
            } else if (size == 0) {
                return "";
            } else {
                if (size < 0) {
                    throw InvalidProtocolBufferException.negativeSize();
                }
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        public String readStringRequireUtf8() throws IOException {
            int size = readRawVarint32();
            if (size > 0 && size <= this.limit - this.pos) {
                String result = Utf8.decodeUtf8(this.buffer, this.pos, size);
                this.pos += size;
                return result;
            } else if (size == 0) {
                return "";
            } else {
                if (size <= 0) {
                    throw InvalidProtocolBufferException.negativeSize();
                }
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        public void readGroup(int fieldNumber, MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            this.recursionDepth++;
            builder.mergeFrom(this, extensionRegistry);
            checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
            this.recursionDepth--;
        }

        public <T extends MessageLite> T readGroup(int fieldNumber, Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            this.recursionDepth++;
            T result = (MessageLite) parser.parsePartialFrom(this, extensionRegistry);
            checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
            this.recursionDepth--;
            return result;
        }

        @Deprecated
        public void readUnknownGroup(int fieldNumber, MessageLite.Builder builder) throws IOException {
            readGroup(fieldNumber, builder, ExtensionRegistryLite.getEmptyRegistry());
        }

        public void readMessage(MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
            int length = readRawVarint32();
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            int oldLimit = pushLimit(length);
            this.recursionDepth++;
            builder.mergeFrom(this, extensionRegistry);
            checkLastTagWas(0);
            this.recursionDepth--;
            popLimit(oldLimit);
        }

        public <T extends MessageLite> T readMessage(Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
            int length = readRawVarint32();
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            int oldLimit = pushLimit(length);
            this.recursionDepth++;
            T result = (MessageLite) parser.parsePartialFrom(this, extensionRegistry);
            checkLastTagWas(0);
            this.recursionDepth--;
            popLimit(oldLimit);
            return result;
        }

        public ByteString readBytes() throws IOException {
            ByteString result;
            int size = readRawVarint32();
            if (size > 0 && size <= this.limit - this.pos) {
                if (!this.immutable || !this.enableAliasing) {
                    result = ByteString.copyFrom(this.buffer, this.pos, size);
                } else {
                    result = ByteString.wrap(this.buffer, this.pos, size);
                }
                this.pos += size;
                return result;
            } else if (size == 0) {
                return ByteString.EMPTY;
            } else {
                return ByteString.wrap(readRawBytes(size));
            }
        }

        public byte[] readByteArray() throws IOException {
            return readRawBytes(readRawVarint32());
        }

        public ByteBuffer readByteBuffer() throws IOException {
            ByteBuffer result;
            int size = readRawVarint32();
            if (size > 0 && size <= this.limit - this.pos) {
                if (this.immutable || !this.enableAliasing) {
                    result = ByteBuffer.wrap(Arrays.copyOfRange(this.buffer, this.pos, this.pos + size));
                } else {
                    result = ByteBuffer.wrap(this.buffer, this.pos, size).slice();
                }
                this.pos += size;
                return result;
            } else if (size == 0) {
                return Internal.EMPTY_BYTE_BUFFER;
            } else {
                if (size < 0) {
                    throw InvalidProtocolBufferException.negativeSize();
                }
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        public int readUInt32() throws IOException {
            return readRawVarint32();
        }

        public int readEnum() throws IOException {
            return readRawVarint32();
        }

        public int readSFixed32() throws IOException {
            return readRawLittleEndian32();
        }

        public long readSFixed64() throws IOException {
            return readRawLittleEndian64();
        }

        public int readSInt32() throws IOException {
            return decodeZigZag32(readRawVarint32());
        }

        public long readSInt64() throws IOException {
            return decodeZigZag64(readRawVarint64());
        }

        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0073, code lost:
            if (r0[r2] < 0) goto L_0x0006;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int readRawVarint32() throws java.io.IOException {
            /*
                r8 = this;
                int r1 = r8.pos
                int r5 = r8.limit
                if (r5 != r1) goto L_0x000c
            L_0x0006:
                long r6 = r8.readRawVarint64SlowPath()
                int r3 = (int) r6
            L_0x000b:
                return r3
            L_0x000c:
                byte[] r0 = r8.buffer
                int r2 = r1 + 1
                byte r3 = r0[r1]
                if (r3 < 0) goto L_0x0018
                r8.pos = r2
                r1 = r2
                goto L_0x000b
            L_0x0018:
                int r5 = r8.limit
                int r5 = r5 - r2
                r6 = 9
                if (r5 >= r6) goto L_0x0021
                r1 = r2
                goto L_0x0006
            L_0x0021:
                int r1 = r2 + 1
                byte r5 = r0[r2]
                int r5 = r5 << 7
                r3 = r3 ^ r5
                if (r3 >= 0) goto L_0x002f
                r3 = r3 ^ -128(0xffffffffffffff80, float:NaN)
            L_0x002c:
                r8.pos = r1
                goto L_0x000b
            L_0x002f:
                int r2 = r1 + 1
                byte r5 = r0[r1]
                int r5 = r5 << 14
                r3 = r3 ^ r5
                if (r3 < 0) goto L_0x003c
                r3 = r3 ^ 16256(0x3f80, float:2.278E-41)
                r1 = r2
                goto L_0x002c
            L_0x003c:
                int r1 = r2 + 1
                byte r5 = r0[r2]
                int r5 = r5 << 21
                r3 = r3 ^ r5
                if (r3 >= 0) goto L_0x004a
                r5 = -2080896(0xffffffffffe03f80, float:NaN)
                r3 = r3 ^ r5
                goto L_0x002c
            L_0x004a:
                int r2 = r1 + 1
                byte r4 = r0[r1]
                int r5 = r4 << 28
                r3 = r3 ^ r5
                r5 = 266354560(0xfe03f80, float:2.2112565E-29)
                r3 = r3 ^ r5
                if (r4 >= 0) goto L_0x0076
                int r1 = r2 + 1
                byte r5 = r0[r2]
                if (r5 >= 0) goto L_0x002c
                int r2 = r1 + 1
                byte r5 = r0[r1]
                if (r5 >= 0) goto L_0x0076
                int r1 = r2 + 1
                byte r5 = r0[r2]
                if (r5 >= 0) goto L_0x002c
                int r2 = r1 + 1
                byte r5 = r0[r1]
                if (r5 >= 0) goto L_0x0076
                int r1 = r2 + 1
                byte r5 = r0[r2]
                if (r5 >= 0) goto L_0x002c
                goto L_0x0006
            L_0x0076:
                r1 = r2
                goto L_0x002c
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.CodedInputStream.ArrayDecoder.readRawVarint32():int");
        }

        private void skipRawVarint() throws IOException {
            if (this.limit - this.pos >= 10) {
                skipRawVarintFastPath();
            } else {
                skipRawVarintSlowPath();
            }
        }

        private void skipRawVarintFastPath() throws IOException {
            int i = 0;
            while (i < 10) {
                byte[] bArr = this.buffer;
                int i2 = this.pos;
                this.pos = i2 + 1;
                if (bArr[i2] < 0) {
                    i++;
                } else {
                    return;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        private void skipRawVarintSlowPath() throws IOException {
            int i = 0;
            while (i < 10) {
                if (readRawByte() < 0) {
                    i++;
                } else {
                    return;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:34:0x00bb, code lost:
            if (((long) r0[r2]) < 0) goto L_0x0008;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public long readRawVarint64() throws java.io.IOException {
            /*
                r14 = this;
                r12 = 0
                int r1 = r14.pos
                int r6 = r14.limit
                if (r6 != r1) goto L_0x000d
            L_0x0008:
                long r4 = r14.readRawVarint64SlowPath()
            L_0x000c:
                return r4
            L_0x000d:
                byte[] r0 = r14.buffer
                int r2 = r1 + 1
                byte r3 = r0[r1]
                if (r3 < 0) goto L_0x001a
                r14.pos = r2
                long r4 = (long) r3
                r1 = r2
                goto L_0x000c
            L_0x001a:
                int r6 = r14.limit
                int r6 = r6 - r2
                r7 = 9
                if (r6 >= r7) goto L_0x0023
                r1 = r2
                goto L_0x0008
            L_0x0023:
                int r1 = r2 + 1
                byte r6 = r0[r2]
                int r6 = r6 << 7
                r3 = r3 ^ r6
                if (r3 >= 0) goto L_0x0032
                r6 = r3 ^ -128(0xffffffffffffff80, float:NaN)
                long r4 = (long) r6
            L_0x002f:
                r14.pos = r1
                goto L_0x000c
            L_0x0032:
                int r2 = r1 + 1
                byte r6 = r0[r1]
                int r6 = r6 << 14
                r3 = r3 ^ r6
                if (r3 < 0) goto L_0x0040
                r6 = r3 ^ 16256(0x3f80, float:2.278E-41)
                long r4 = (long) r6
                r1 = r2
                goto L_0x002f
            L_0x0040:
                int r1 = r2 + 1
                byte r6 = r0[r2]
                int r6 = r6 << 21
                r3 = r3 ^ r6
                if (r3 >= 0) goto L_0x004f
                r6 = -2080896(0xffffffffffe03f80, float:NaN)
                r6 = r6 ^ r3
                long r4 = (long) r6
                goto L_0x002f
            L_0x004f:
                long r6 = (long) r3
                int r2 = r1 + 1
                byte r8 = r0[r1]
                long r8 = (long) r8
                r10 = 28
                long r8 = r8 << r10
                long r4 = r6 ^ r8
                int r6 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r6 < 0) goto L_0x0064
                r6 = 266354560(0xfe03f80, double:1.315966377E-315)
                long r4 = r4 ^ r6
                r1 = r2
                goto L_0x002f
            L_0x0064:
                int r1 = r2 + 1
                byte r6 = r0[r2]
                long r6 = (long) r6
                r8 = 35
                long r6 = r6 << r8
                long r4 = r4 ^ r6
                int r6 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r6 >= 0) goto L_0x0078
                r6 = -34093383808(0xfffffff80fe03f80, double:NaN)
                long r4 = r4 ^ r6
                goto L_0x002f
            L_0x0078:
                int r2 = r1 + 1
                byte r6 = r0[r1]
                long r6 = (long) r6
                r8 = 42
                long r6 = r6 << r8
                long r4 = r4 ^ r6
                int r6 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r6 < 0) goto L_0x008d
                r6 = 4363953127296(0x3f80fe03f80, double:2.1560793202584E-311)
                long r4 = r4 ^ r6
                r1 = r2
                goto L_0x002f
            L_0x008d:
                int r1 = r2 + 1
                byte r6 = r0[r2]
                long r6 = (long) r6
                r8 = 49
                long r6 = r6 << r8
                long r4 = r4 ^ r6
                int r6 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r6 >= 0) goto L_0x00a1
                r6 = -558586000294016(0xfffe03f80fe03f80, double:NaN)
                long r4 = r4 ^ r6
                goto L_0x002f
            L_0x00a1:
                int r2 = r1 + 1
                byte r6 = r0[r1]
                long r6 = (long) r6
                r8 = 56
                long r6 = r6 << r8
                long r4 = r4 ^ r6
                r6 = 71499008037633920(0xfe03f80fe03f80, double:6.838959413692434E-304)
                long r4 = r4 ^ r6
                int r6 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r6 >= 0) goto L_0x00bf
                int r1 = r2 + 1
                byte r6 = r0[r2]
                long r6 = (long) r6
                int r6 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
                if (r6 >= 0) goto L_0x002f
                goto L_0x0008
            L_0x00bf:
                r1 = r2
                goto L_0x002f
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.CodedInputStream.ArrayDecoder.readRawVarint64():long");
        }

        /* access modifiers changed from: package-private */
        public long readRawVarint64SlowPath() throws IOException {
            long result = 0;
            for (int shift = 0; shift < 64; shift += 7) {
                byte b = readRawByte();
                result |= ((long) (b & ByteCompanionObject.MAX_VALUE)) << shift;
                if ((b & 128) == 0) {
                    return result;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        public int readRawLittleEndian32() throws IOException {
            int tempPos = this.pos;
            if (this.limit - tempPos < 4) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            byte[] buffer2 = this.buffer;
            this.pos = tempPos + 4;
            return (buffer2[tempPos] & 255) | ((buffer2[tempPos + 1] & 255) << 8) | ((buffer2[tempPos + 2] & 255) << Tnaf.POW_2_WIDTH) | ((buffer2[tempPos + 3] & 255) << 24);
        }

        public long readRawLittleEndian64() throws IOException {
            int tempPos = this.pos;
            if (this.limit - tempPos < 8) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            byte[] buffer2 = this.buffer;
            this.pos = tempPos + 8;
            return (((long) buffer2[tempPos]) & 255) | ((((long) buffer2[tempPos + 1]) & 255) << 8) | ((((long) buffer2[tempPos + 2]) & 255) << 16) | ((((long) buffer2[tempPos + 3]) & 255) << 24) | ((((long) buffer2[tempPos + 4]) & 255) << 32) | ((((long) buffer2[tempPos + 5]) & 255) << 40) | ((((long) buffer2[tempPos + 6]) & 255) << 48) | ((((long) buffer2[tempPos + 7]) & 255) << 56);
        }

        public void enableAliasing(boolean enabled) {
            this.enableAliasing = enabled;
        }

        public void resetSizeCounter() {
            this.startPos = this.pos;
        }

        public int pushLimit(int byteLimit) throws InvalidProtocolBufferException {
            if (byteLimit < 0) {
                throw InvalidProtocolBufferException.negativeSize();
            }
            int byteLimit2 = byteLimit + getTotalBytesRead();
            int oldLimit = this.currentLimit;
            if (byteLimit2 > oldLimit) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            this.currentLimit = byteLimit2;
            recomputeBufferSizeAfterLimit();
            return oldLimit;
        }

        private void recomputeBufferSizeAfterLimit() {
            this.limit += this.bufferSizeAfterLimit;
            int bufferEnd = this.limit - this.startPos;
            if (bufferEnd > this.currentLimit) {
                this.bufferSizeAfterLimit = bufferEnd - this.currentLimit;
                this.limit -= this.bufferSizeAfterLimit;
                return;
            }
            this.bufferSizeAfterLimit = 0;
        }

        public void popLimit(int oldLimit) {
            this.currentLimit = oldLimit;
            recomputeBufferSizeAfterLimit();
        }

        public int getBytesUntilLimit() {
            if (this.currentLimit == Integer.MAX_VALUE) {
                return -1;
            }
            return this.currentLimit - getTotalBytesRead();
        }

        public boolean isAtEnd() throws IOException {
            return this.pos == this.limit;
        }

        public int getTotalBytesRead() {
            return this.pos - this.startPos;
        }

        public byte readRawByte() throws IOException {
            if (this.pos == this.limit) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            byte[] bArr = this.buffer;
            int i = this.pos;
            this.pos = i + 1;
            return bArr[i];
        }

        public byte[] readRawBytes(int length) throws IOException {
            if (length > 0 && length <= this.limit - this.pos) {
                int tempPos = this.pos;
                this.pos += length;
                return Arrays.copyOfRange(this.buffer, tempPos, this.pos);
            } else if (length > 0) {
                throw InvalidProtocolBufferException.truncatedMessage();
            } else if (length == 0) {
                return Internal.EMPTY_BYTE_ARRAY;
            } else {
                throw InvalidProtocolBufferException.negativeSize();
            }
        }

        public void skipRawBytes(int length) throws IOException {
            if (length >= 0 && length <= this.limit - this.pos) {
                this.pos += length;
            } else if (length < 0) {
                throw InvalidProtocolBufferException.negativeSize();
            } else {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }
    }

    private static final class UnsafeDirectNioDecoder extends CodedInputStream {
        private final long address;
        private final ByteBuffer buffer;
        private int bufferSizeAfterLimit;
        private int currentLimit;
        private boolean enableAliasing;
        private final boolean immutable;
        private int lastTag;
        private long limit;
        private long pos;
        private long startPos;

        static boolean isSupported() {
            return UnsafeUtil.hasUnsafeByteBufferOperations();
        }

        private UnsafeDirectNioDecoder(ByteBuffer buffer2, boolean immutable2) {
            super();
            this.currentLimit = Integer.MAX_VALUE;
            this.buffer = buffer2;
            this.address = UnsafeUtil.addressOffset(buffer2);
            this.limit = this.address + ((long) buffer2.limit());
            this.pos = this.address + ((long) buffer2.position());
            this.startPos = this.pos;
            this.immutable = immutable2;
        }

        public int readTag() throws IOException {
            if (isAtEnd()) {
                this.lastTag = 0;
                return 0;
            }
            this.lastTag = readRawVarint32();
            if (WireFormat.getTagFieldNumber(this.lastTag) != 0) {
                return this.lastTag;
            }
            throw InvalidProtocolBufferException.invalidTag();
        }

        public void checkLastTagWas(int value) throws InvalidProtocolBufferException {
            if (this.lastTag != value) {
                throw InvalidProtocolBufferException.invalidEndTag();
            }
        }

        public int getLastTag() {
            return this.lastTag;
        }

        public boolean skipField(int tag) throws IOException {
            switch (WireFormat.getTagWireType(tag)) {
                case 0:
                    skipRawVarint();
                    return true;
                case 1:
                    skipRawBytes(8);
                    return true;
                case 2:
                    skipRawBytes(readRawVarint32());
                    return true;
                case 3:
                    skipMessage();
                    checkLastTagWas(WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4));
                    return true;
                case 4:
                    return false;
                case 5:
                    skipRawBytes(4);
                    return true;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        /* JADX INFO: Multiple debug info for r2v1 com.google.protobuf.ByteString: [D('value' long), D('value' com.google.protobuf.ByteString)] */
        public boolean skipField(int tag, CodedOutputStream output) throws IOException {
            switch (WireFormat.getTagWireType(tag)) {
                case 0:
                    long value = readInt64();
                    output.writeRawVarint32(tag);
                    output.writeUInt64NoTag(value);
                    return true;
                case 1:
                    long value2 = readRawLittleEndian64();
                    output.writeRawVarint32(tag);
                    output.writeFixed64NoTag(value2);
                    return true;
                case 2:
                    ByteString value3 = readBytes();
                    output.writeRawVarint32(tag);
                    output.writeBytesNoTag(value3);
                    return true;
                case 3:
                    output.writeRawVarint32(tag);
                    skipMessage(output);
                    int endtag = WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4);
                    checkLastTagWas(endtag);
                    output.writeRawVarint32(endtag);
                    return true;
                case 4:
                    return false;
                case 5:
                    int value4 = readRawLittleEndian32();
                    output.writeRawVarint32(tag);
                    output.writeFixed32NoTag(value4);
                    return true;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        public void skipMessage() throws IOException {
            int tag;
            do {
                tag = readTag();
                if (tag == 0) {
                    return;
                }
            } while (skipField(tag));
        }

        public void skipMessage(CodedOutputStream output) throws IOException {
            int tag;
            do {
                tag = readTag();
                if (tag == 0) {
                    return;
                }
            } while (skipField(tag, output));
        }

        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readRawLittleEndian64());
        }

        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readRawLittleEndian32());
        }

        public long readUInt64() throws IOException {
            return readRawVarint64();
        }

        public long readInt64() throws IOException {
            return readRawVarint64();
        }

        public int readInt32() throws IOException {
            return readRawVarint32();
        }

        public long readFixed64() throws IOException {
            return readRawLittleEndian64();
        }

        public int readFixed32() throws IOException {
            return readRawLittleEndian32();
        }

        public boolean readBool() throws IOException {
            return readRawVarint64() != 0;
        }

        public String readString() throws IOException {
            int size = readRawVarint32();
            if (size > 0 && size <= remaining()) {
                byte[] bytes = new byte[size];
                UnsafeUtil.copyMemory(this.pos, bytes, 0, (long) size);
                String result = new String(bytes, Internal.UTF_8);
                this.pos += (long) size;
                return result;
            } else if (size == 0) {
                return "";
            } else {
                if (size < 0) {
                    throw InvalidProtocolBufferException.negativeSize();
                }
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        public String readStringRequireUtf8() throws IOException {
            int size = readRawVarint32();
            if (size > 0 && size <= remaining()) {
                String result = Utf8.decodeUtf8(this.buffer, bufferPos(this.pos), size);
                this.pos += (long) size;
                return result;
            } else if (size == 0) {
                return "";
            } else {
                if (size <= 0) {
                    throw InvalidProtocolBufferException.negativeSize();
                }
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        public void readGroup(int fieldNumber, MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            this.recursionDepth++;
            builder.mergeFrom(this, extensionRegistry);
            checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
            this.recursionDepth--;
        }

        public <T extends MessageLite> T readGroup(int fieldNumber, Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            this.recursionDepth++;
            T result = (MessageLite) parser.parsePartialFrom(this, extensionRegistry);
            checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
            this.recursionDepth--;
            return result;
        }

        @Deprecated
        public void readUnknownGroup(int fieldNumber, MessageLite.Builder builder) throws IOException {
            readGroup(fieldNumber, builder, ExtensionRegistryLite.getEmptyRegistry());
        }

        public void readMessage(MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
            int length = readRawVarint32();
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            int oldLimit = pushLimit(length);
            this.recursionDepth++;
            builder.mergeFrom(this, extensionRegistry);
            checkLastTagWas(0);
            this.recursionDepth--;
            popLimit(oldLimit);
        }

        public <T extends MessageLite> T readMessage(Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
            int length = readRawVarint32();
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            int oldLimit = pushLimit(length);
            this.recursionDepth++;
            T result = (MessageLite) parser.parsePartialFrom(this, extensionRegistry);
            checkLastTagWas(0);
            this.recursionDepth--;
            popLimit(oldLimit);
            return result;
        }

        public ByteString readBytes() throws IOException {
            int size = readRawVarint32();
            if (size <= 0 || size > remaining()) {
                if (size == 0) {
                    return ByteString.EMPTY;
                }
                if (size < 0) {
                    throw InvalidProtocolBufferException.negativeSize();
                }
                throw InvalidProtocolBufferException.truncatedMessage();
            } else if (!this.immutable || !this.enableAliasing) {
                byte[] bytes = new byte[size];
                UnsafeUtil.copyMemory(this.pos, bytes, 0, (long) size);
                this.pos += (long) size;
                return ByteString.wrap(bytes);
            } else {
                ByteBuffer result = slice(this.pos, this.pos + ((long) size));
                this.pos += (long) size;
                return ByteString.wrap(result);
            }
        }

        public byte[] readByteArray() throws IOException {
            return readRawBytes(readRawVarint32());
        }

        public ByteBuffer readByteBuffer() throws IOException {
            int size = readRawVarint32();
            if (size <= 0 || size > remaining()) {
                if (size == 0) {
                    return Internal.EMPTY_BYTE_BUFFER;
                }
                if (size < 0) {
                    throw InvalidProtocolBufferException.negativeSize();
                }
                throw InvalidProtocolBufferException.truncatedMessage();
            } else if (this.immutable || !this.enableAliasing) {
                byte[] bytes = new byte[size];
                UnsafeUtil.copyMemory(this.pos, bytes, 0, (long) size);
                this.pos += (long) size;
                return ByteBuffer.wrap(bytes);
            } else {
                ByteBuffer result = slice(this.pos, this.pos + ((long) size));
                this.pos += (long) size;
                return result;
            }
        }

        public int readUInt32() throws IOException {
            return readRawVarint32();
        }

        public int readEnum() throws IOException {
            return readRawVarint32();
        }

        public int readSFixed32() throws IOException {
            return readRawLittleEndian32();
        }

        public long readSFixed64() throws IOException {
            return readRawLittleEndian64();
        }

        public int readSInt32() throws IOException {
            return decodeZigZag32(readRawVarint32());
        }

        public long readSInt64() throws IOException {
            return decodeZigZag64(readRawVarint64());
        }

        /* JADX WARNING: Code restructure failed: missing block: B:30:0x008b, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r2) < 0) goto L_0x000a;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int readRawVarint32() throws java.io.IOException {
            /*
                r12 = this;
                r10 = 1
                long r0 = r12.pos
                long r6 = r12.limit
                int r6 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
                if (r6 != 0) goto L_0x0010
            L_0x000a:
                long r6 = r12.readRawVarint64SlowPath()
                int r4 = (int) r6
            L_0x000f:
                return r4
            L_0x0010:
                long r2 = r0 + r10
                byte r4 = com.google.protobuf.UnsafeUtil.getByte(r0)
                if (r4 < 0) goto L_0x001c
                r12.pos = r2
                r0 = r2
                goto L_0x000f
            L_0x001c:
                long r6 = r12.limit
                long r6 = r6 - r2
                r8 = 9
                int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r6 >= 0) goto L_0x0027
                r0 = r2
                goto L_0x000a
            L_0x0027:
                long r0 = r2 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r2)
                int r6 = r6 << 7
                r4 = r4 ^ r6
                if (r4 >= 0) goto L_0x0037
                r4 = r4 ^ -128(0xffffffffffffff80, float:NaN)
            L_0x0034:
                r12.pos = r0
                goto L_0x000f
            L_0x0037:
                long r2 = r0 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r0)
                int r6 = r6 << 14
                r4 = r4 ^ r6
                if (r4 < 0) goto L_0x0046
                r4 = r4 ^ 16256(0x3f80, float:2.278E-41)
                r0 = r2
                goto L_0x0034
            L_0x0046:
                long r0 = r2 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r2)
                int r6 = r6 << 21
                r4 = r4 ^ r6
                if (r4 >= 0) goto L_0x0056
                r6 = -2080896(0xffffffffffe03f80, float:NaN)
                r4 = r4 ^ r6
                goto L_0x0034
            L_0x0056:
                long r2 = r0 + r10
                byte r5 = com.google.protobuf.UnsafeUtil.getByte(r0)
                int r6 = r5 << 28
                r4 = r4 ^ r6
                r6 = 266354560(0xfe03f80, float:2.2112565E-29)
                r4 = r4 ^ r6
                if (r5 >= 0) goto L_0x008f
                long r0 = r2 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r2)
                if (r6 >= 0) goto L_0x0034
                long r2 = r0 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r0)
                if (r6 >= 0) goto L_0x008f
                long r0 = r2 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r2)
                if (r6 >= 0) goto L_0x0034
                long r2 = r0 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r0)
                if (r6 >= 0) goto L_0x008f
                long r0 = r2 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r2)
                if (r6 >= 0) goto L_0x0034
                goto L_0x000a
            L_0x008f:
                r0 = r2
                goto L_0x0034
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.CodedInputStream.UnsafeDirectNioDecoder.readRawVarint32():int");
        }

        private void skipRawVarint() throws IOException {
            if (remaining() >= 10) {
                skipRawVarintFastPath();
            } else {
                skipRawVarintSlowPath();
            }
        }

        private void skipRawVarintFastPath() throws IOException {
            int i = 0;
            while (i < 10) {
                long j = this.pos;
                this.pos = 1 + j;
                if (UnsafeUtil.getByte(j) < 0) {
                    i++;
                } else {
                    return;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        private void skipRawVarintSlowPath() throws IOException {
            int i = 0;
            while (i < 10) {
                if (readRawByte() < 0) {
                    i++;
                } else {
                    return;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:34:0x00f0, code lost:
            if (((long) com.google.protobuf.UnsafeUtil.getByte(r2)) < 0) goto L_0x0008;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public long readRawVarint64() throws java.io.IOException {
            /*
                r12 = this;
                long r0 = r12.pos
                long r8 = r12.limit
                int r7 = (r8 > r0 ? 1 : (r8 == r0 ? 0 : -1))
                if (r7 != 0) goto L_0x000d
            L_0x0008:
                long r4 = r12.readRawVarint64SlowPath()
            L_0x000c:
                return r4
            L_0x000d:
                r8 = 1
                long r2 = r0 + r8
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r0)
                if (r6 < 0) goto L_0x001c
                r12.pos = r2
                long r4 = (long) r6
                r0 = r2
                goto L_0x000c
            L_0x001c:
                long r8 = r12.limit
                long r8 = r8 - r2
                r10 = 9
                int r7 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                if (r7 >= 0) goto L_0x0027
                r0 = r2
                goto L_0x0008
            L_0x0027:
                r8 = 1
                long r0 = r2 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r2)
                int r7 = r7 << 7
                r6 = r6 ^ r7
                if (r6 >= 0) goto L_0x003a
                r7 = r6 ^ -128(0xffffffffffffff80, float:NaN)
                long r4 = (long) r7
            L_0x0037:
                r12.pos = r0
                goto L_0x000c
            L_0x003a:
                r8 = 1
                long r2 = r0 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r0)
                int r7 = r7 << 14
                r6 = r6 ^ r7
                if (r6 < 0) goto L_0x004c
                r7 = r6 ^ 16256(0x3f80, float:2.278E-41)
                long r4 = (long) r7
                r0 = r2
                goto L_0x0037
            L_0x004c:
                r8 = 1
                long r0 = r2 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r2)
                int r7 = r7 << 21
                r6 = r6 ^ r7
                if (r6 >= 0) goto L_0x005f
                r7 = -2080896(0xffffffffffe03f80, float:NaN)
                r7 = r7 ^ r6
                long r4 = (long) r7
                goto L_0x0037
            L_0x005f:
                long r8 = (long) r6
                r10 = 1
                long r2 = r0 + r10
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r0)
                long r10 = (long) r7
                r7 = 28
                long r10 = r10 << r7
                long r4 = r8 ^ r10
                r8 = 0
                int r7 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r7 < 0) goto L_0x007a
                r8 = 266354560(0xfe03f80, double:1.315966377E-315)
                long r4 = r4 ^ r8
                r0 = r2
                goto L_0x0037
            L_0x007a:
                r8 = 1
                long r0 = r2 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r2)
                long r8 = (long) r7
                r7 = 35
                long r8 = r8 << r7
                long r4 = r4 ^ r8
                r8 = 0
                int r7 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r7 >= 0) goto L_0x0094
                r8 = -34093383808(0xfffffff80fe03f80, double:NaN)
                long r4 = r4 ^ r8
                goto L_0x0037
            L_0x0094:
                r8 = 1
                long r2 = r0 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r0)
                long r8 = (long) r7
                r7 = 42
                long r8 = r8 << r7
                long r4 = r4 ^ r8
                r8 = 0
                int r7 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r7 < 0) goto L_0x00af
                r8 = 4363953127296(0x3f80fe03f80, double:2.1560793202584E-311)
                long r4 = r4 ^ r8
                r0 = r2
                goto L_0x0037
            L_0x00af:
                r8 = 1
                long r0 = r2 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r2)
                long r8 = (long) r7
                r7 = 49
                long r8 = r8 << r7
                long r4 = r4 ^ r8
                r8 = 0
                int r7 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r7 >= 0) goto L_0x00ca
                r8 = -558586000294016(0xfffe03f80fe03f80, double:NaN)
                long r4 = r4 ^ r8
                goto L_0x0037
            L_0x00ca:
                r8 = 1
                long r2 = r0 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r0)
                long r8 = (long) r7
                r7 = 56
                long r8 = r8 << r7
                long r4 = r4 ^ r8
                r8 = 71499008037633920(0xfe03f80fe03f80, double:6.838959413692434E-304)
                long r4 = r4 ^ r8
                r8 = 0
                int r7 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r7 >= 0) goto L_0x00f4
                r8 = 1
                long r0 = r2 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r2)
                long r8 = (long) r7
                r10 = 0
                int r7 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                if (r7 >= 0) goto L_0x0037
                goto L_0x0008
            L_0x00f4:
                r0 = r2
                goto L_0x0037
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.CodedInputStream.UnsafeDirectNioDecoder.readRawVarint64():long");
        }

        /* access modifiers changed from: package-private */
        public long readRawVarint64SlowPath() throws IOException {
            long result = 0;
            for (int shift = 0; shift < 64; shift += 7) {
                byte b = readRawByte();
                result |= ((long) (b & ByteCompanionObject.MAX_VALUE)) << shift;
                if ((b & 128) == 0) {
                    return result;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        public int readRawLittleEndian32() throws IOException {
            long tempPos = this.pos;
            if (this.limit - tempPos < 4) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            this.pos = tempPos + 4;
            return (UnsafeUtil.getByte(tempPos) & 255) | ((UnsafeUtil.getByte(1 + tempPos) & 255) << 8) | ((UnsafeUtil.getByte(2 + tempPos) & 255) << Tnaf.POW_2_WIDTH) | ((UnsafeUtil.getByte(3 + tempPos) & 255) << 24);
        }

        public long readRawLittleEndian64() throws IOException {
            long tempPos = this.pos;
            if (this.limit - tempPos < 8) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            this.pos = tempPos + 8;
            return (((long) UnsafeUtil.getByte(tempPos)) & 255) | ((((long) UnsafeUtil.getByte(1 + tempPos)) & 255) << 8) | ((((long) UnsafeUtil.getByte(2 + tempPos)) & 255) << 16) | ((((long) UnsafeUtil.getByte(3 + tempPos)) & 255) << 24) | ((((long) UnsafeUtil.getByte(4 + tempPos)) & 255) << 32) | ((((long) UnsafeUtil.getByte(5 + tempPos)) & 255) << 40) | ((((long) UnsafeUtil.getByte(6 + tempPos)) & 255) << 48) | ((((long) UnsafeUtil.getByte(7 + tempPos)) & 255) << 56);
        }

        public void enableAliasing(boolean enabled) {
            this.enableAliasing = enabled;
        }

        public void resetSizeCounter() {
            this.startPos = this.pos;
        }

        public int pushLimit(int byteLimit) throws InvalidProtocolBufferException {
            if (byteLimit < 0) {
                throw InvalidProtocolBufferException.negativeSize();
            }
            int byteLimit2 = byteLimit + getTotalBytesRead();
            int oldLimit = this.currentLimit;
            if (byteLimit2 > oldLimit) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            this.currentLimit = byteLimit2;
            recomputeBufferSizeAfterLimit();
            return oldLimit;
        }

        public void popLimit(int oldLimit) {
            this.currentLimit = oldLimit;
            recomputeBufferSizeAfterLimit();
        }

        public int getBytesUntilLimit() {
            if (this.currentLimit == Integer.MAX_VALUE) {
                return -1;
            }
            return this.currentLimit - getTotalBytesRead();
        }

        public boolean isAtEnd() throws IOException {
            return this.pos == this.limit;
        }

        public int getTotalBytesRead() {
            return (int) (this.pos - this.startPos);
        }

        public byte readRawByte() throws IOException {
            if (this.pos == this.limit) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            long j = this.pos;
            this.pos = 1 + j;
            return UnsafeUtil.getByte(j);
        }

        public byte[] readRawBytes(int length) throws IOException {
            if (length >= 0 && length <= remaining()) {
                byte[] bytes = new byte[length];
                slice(this.pos, this.pos + ((long) length)).get(bytes);
                this.pos += (long) length;
                return bytes;
            } else if (length > 0) {
                throw InvalidProtocolBufferException.truncatedMessage();
            } else if (length == 0) {
                return Internal.EMPTY_BYTE_ARRAY;
            } else {
                throw InvalidProtocolBufferException.negativeSize();
            }
        }

        public void skipRawBytes(int length) throws IOException {
            if (length >= 0 && length <= remaining()) {
                this.pos += (long) length;
            } else if (length < 0) {
                throw InvalidProtocolBufferException.negativeSize();
            } else {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        private void recomputeBufferSizeAfterLimit() {
            this.limit += (long) this.bufferSizeAfterLimit;
            int bufferEnd = (int) (this.limit - this.startPos);
            if (bufferEnd > this.currentLimit) {
                this.bufferSizeAfterLimit = bufferEnd - this.currentLimit;
                this.limit -= (long) this.bufferSizeAfterLimit;
                return;
            }
            this.bufferSizeAfterLimit = 0;
        }

        private int remaining() {
            return (int) (this.limit - this.pos);
        }

        private int bufferPos(long pos2) {
            return (int) (pos2 - this.address);
        }

        private ByteBuffer slice(long begin, long end) throws IOException {
            int prevPos = this.buffer.position();
            int prevLimit = this.buffer.limit();
            try {
                this.buffer.position(bufferPos(begin));
                this.buffer.limit(bufferPos(end));
                ByteBuffer slice = this.buffer.slice();
                this.buffer.position(prevPos);
                this.buffer.limit(prevLimit);
                return slice;
            } catch (IllegalArgumentException e) {
                throw InvalidProtocolBufferException.truncatedMessage();
            } catch (Throwable th) {
                this.buffer.position(prevPos);
                this.buffer.limit(prevLimit);
                throw th;
            }
        }
    }

    private static final class StreamDecoder extends CodedInputStream {
        /* access modifiers changed from: private */
        public final byte[] buffer;
        private int bufferSize;
        private int bufferSizeAfterLimit;
        private int currentLimit;
        private final InputStream input;
        private int lastTag;
        /* access modifiers changed from: private */
        public int pos;
        private RefillCallback refillCallback;
        private int totalBytesRetired;

        private interface RefillCallback {
            void onRefill();
        }

        private StreamDecoder(InputStream input2, int bufferSize2) {
            super();
            this.currentLimit = Integer.MAX_VALUE;
            this.refillCallback = null;
            Internal.checkNotNull(input2, "input");
            this.input = input2;
            this.buffer = new byte[bufferSize2];
            this.bufferSize = 0;
            this.pos = 0;
            this.totalBytesRetired = 0;
        }

        public int readTag() throws IOException {
            if (isAtEnd()) {
                this.lastTag = 0;
                return 0;
            }
            this.lastTag = readRawVarint32();
            if (WireFormat.getTagFieldNumber(this.lastTag) != 0) {
                return this.lastTag;
            }
            throw InvalidProtocolBufferException.invalidTag();
        }

        public void checkLastTagWas(int value) throws InvalidProtocolBufferException {
            if (this.lastTag != value) {
                throw InvalidProtocolBufferException.invalidEndTag();
            }
        }

        public int getLastTag() {
            return this.lastTag;
        }

        public boolean skipField(int tag) throws IOException {
            switch (WireFormat.getTagWireType(tag)) {
                case 0:
                    skipRawVarint();
                    return true;
                case 1:
                    skipRawBytes(8);
                    return true;
                case 2:
                    skipRawBytes(readRawVarint32());
                    return true;
                case 3:
                    skipMessage();
                    checkLastTagWas(WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4));
                    return true;
                case 4:
                    return false;
                case 5:
                    skipRawBytes(4);
                    return true;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        /* JADX INFO: Multiple debug info for r2v1 com.google.protobuf.ByteString: [D('value' long), D('value' com.google.protobuf.ByteString)] */
        public boolean skipField(int tag, CodedOutputStream output) throws IOException {
            switch (WireFormat.getTagWireType(tag)) {
                case 0:
                    long value = readInt64();
                    output.writeRawVarint32(tag);
                    output.writeUInt64NoTag(value);
                    return true;
                case 1:
                    long value2 = readRawLittleEndian64();
                    output.writeRawVarint32(tag);
                    output.writeFixed64NoTag(value2);
                    return true;
                case 2:
                    ByteString value3 = readBytes();
                    output.writeRawVarint32(tag);
                    output.writeBytesNoTag(value3);
                    return true;
                case 3:
                    output.writeRawVarint32(tag);
                    skipMessage(output);
                    int endtag = WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4);
                    checkLastTagWas(endtag);
                    output.writeRawVarint32(endtag);
                    return true;
                case 4:
                    return false;
                case 5:
                    int value4 = readRawLittleEndian32();
                    output.writeRawVarint32(tag);
                    output.writeFixed32NoTag(value4);
                    return true;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        public void skipMessage() throws IOException {
            int tag;
            do {
                tag = readTag();
                if (tag == 0) {
                    return;
                }
            } while (skipField(tag));
        }

        public void skipMessage(CodedOutputStream output) throws IOException {
            int tag;
            do {
                tag = readTag();
                if (tag == 0) {
                    return;
                }
            } while (skipField(tag, output));
        }

        private class SkippedDataSink implements RefillCallback {
            private ByteArrayOutputStream byteArrayStream;
            private int lastPos = StreamDecoder.this.pos;

            private SkippedDataSink() {
            }

            public void onRefill() {
                if (this.byteArrayStream == null) {
                    this.byteArrayStream = new ByteArrayOutputStream();
                }
                this.byteArrayStream.write(StreamDecoder.this.buffer, this.lastPos, StreamDecoder.this.pos - this.lastPos);
                this.lastPos = 0;
            }

            /* access modifiers changed from: package-private */
            public ByteBuffer getSkippedData() {
                if (this.byteArrayStream == null) {
                    return ByteBuffer.wrap(StreamDecoder.this.buffer, this.lastPos, StreamDecoder.this.pos - this.lastPos);
                }
                this.byteArrayStream.write(StreamDecoder.this.buffer, this.lastPos, StreamDecoder.this.pos);
                return ByteBuffer.wrap(this.byteArrayStream.toByteArray());
            }
        }

        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readRawLittleEndian64());
        }

        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readRawLittleEndian32());
        }

        public long readUInt64() throws IOException {
            return readRawVarint64();
        }

        public long readInt64() throws IOException {
            return readRawVarint64();
        }

        public int readInt32() throws IOException {
            return readRawVarint32();
        }

        public long readFixed64() throws IOException {
            return readRawLittleEndian64();
        }

        public int readFixed32() throws IOException {
            return readRawLittleEndian32();
        }

        public boolean readBool() throws IOException {
            return readRawVarint64() != 0;
        }

        public String readString() throws IOException {
            int size = readRawVarint32();
            if (size > 0 && size <= this.bufferSize - this.pos) {
                String result = new String(this.buffer, this.pos, size, Internal.UTF_8);
                this.pos += size;
                return result;
            } else if (size == 0) {
                return "";
            } else {
                if (size > this.bufferSize) {
                    return new String(readRawBytesSlowPath(size, false), Internal.UTF_8);
                }
                refillBuffer(size);
                String result2 = new String(this.buffer, this.pos, size, Internal.UTF_8);
                this.pos += size;
                return result2;
            }
        }

        public String readStringRequireUtf8() throws IOException {
            byte[] bytes;
            int tempPos;
            int size = readRawVarint32();
            int oldPos = this.pos;
            if (size <= this.bufferSize - oldPos && size > 0) {
                bytes = this.buffer;
                this.pos = oldPos + size;
                tempPos = oldPos;
            } else if (size == 0) {
                return "";
            } else {
                if (size <= this.bufferSize) {
                    refillBuffer(size);
                    bytes = this.buffer;
                    tempPos = 0;
                    this.pos = 0 + size;
                } else {
                    bytes = readRawBytesSlowPath(size, false);
                    tempPos = 0;
                }
            }
            return Utf8.decodeUtf8(bytes, tempPos, size);
        }

        public void readGroup(int fieldNumber, MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            this.recursionDepth++;
            builder.mergeFrom(this, extensionRegistry);
            checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
            this.recursionDepth--;
        }

        public <T extends MessageLite> T readGroup(int fieldNumber, Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            this.recursionDepth++;
            T result = (MessageLite) parser.parsePartialFrom(this, extensionRegistry);
            checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
            this.recursionDepth--;
            return result;
        }

        @Deprecated
        public void readUnknownGroup(int fieldNumber, MessageLite.Builder builder) throws IOException {
            readGroup(fieldNumber, builder, ExtensionRegistryLite.getEmptyRegistry());
        }

        public void readMessage(MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
            int length = readRawVarint32();
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            int oldLimit = pushLimit(length);
            this.recursionDepth++;
            builder.mergeFrom(this, extensionRegistry);
            checkLastTagWas(0);
            this.recursionDepth--;
            popLimit(oldLimit);
        }

        public <T extends MessageLite> T readMessage(Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
            int length = readRawVarint32();
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            int oldLimit = pushLimit(length);
            this.recursionDepth++;
            T result = (MessageLite) parser.parsePartialFrom(this, extensionRegistry);
            checkLastTagWas(0);
            this.recursionDepth--;
            popLimit(oldLimit);
            return result;
        }

        public ByteString readBytes() throws IOException {
            int size = readRawVarint32();
            if (size <= this.bufferSize - this.pos && size > 0) {
                ByteString result = ByteString.copyFrom(this.buffer, this.pos, size);
                this.pos += size;
                return result;
            } else if (size == 0) {
                return ByteString.EMPTY;
            } else {
                return readBytesSlowPath(size);
            }
        }

        public byte[] readByteArray() throws IOException {
            int size = readRawVarint32();
            if (size > this.bufferSize - this.pos || size <= 0) {
                return readRawBytesSlowPath(size, false);
            }
            byte[] result = Arrays.copyOfRange(this.buffer, this.pos, this.pos + size);
            this.pos += size;
            return result;
        }

        public ByteBuffer readByteBuffer() throws IOException {
            int size = readRawVarint32();
            if (size <= this.bufferSize - this.pos && size > 0) {
                ByteBuffer result = ByteBuffer.wrap(Arrays.copyOfRange(this.buffer, this.pos, this.pos + size));
                this.pos += size;
                return result;
            } else if (size == 0) {
                return Internal.EMPTY_BYTE_BUFFER;
            } else {
                return ByteBuffer.wrap(readRawBytesSlowPath(size, true));
            }
        }

        public int readUInt32() throws IOException {
            return readRawVarint32();
        }

        public int readEnum() throws IOException {
            return readRawVarint32();
        }

        public int readSFixed32() throws IOException {
            return readRawLittleEndian32();
        }

        public long readSFixed64() throws IOException {
            return readRawLittleEndian64();
        }

        public int readSInt32() throws IOException {
            return decodeZigZag32(readRawVarint32());
        }

        public long readSInt64() throws IOException {
            return decodeZigZag64(readRawVarint64());
        }

        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0073, code lost:
            if (r0[r2] < 0) goto L_0x0006;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int readRawVarint32() throws java.io.IOException {
            /*
                r8 = this;
                int r1 = r8.pos
                int r5 = r8.bufferSize
                if (r5 != r1) goto L_0x000c
            L_0x0006:
                long r6 = r8.readRawVarint64SlowPath()
                int r3 = (int) r6
            L_0x000b:
                return r3
            L_0x000c:
                byte[] r0 = r8.buffer
                int r2 = r1 + 1
                byte r3 = r0[r1]
                if (r3 < 0) goto L_0x0018
                r8.pos = r2
                r1 = r2
                goto L_0x000b
            L_0x0018:
                int r5 = r8.bufferSize
                int r5 = r5 - r2
                r6 = 9
                if (r5 >= r6) goto L_0x0021
                r1 = r2
                goto L_0x0006
            L_0x0021:
                int r1 = r2 + 1
                byte r5 = r0[r2]
                int r5 = r5 << 7
                r3 = r3 ^ r5
                if (r3 >= 0) goto L_0x002f
                r3 = r3 ^ -128(0xffffffffffffff80, float:NaN)
            L_0x002c:
                r8.pos = r1
                goto L_0x000b
            L_0x002f:
                int r2 = r1 + 1
                byte r5 = r0[r1]
                int r5 = r5 << 14
                r3 = r3 ^ r5
                if (r3 < 0) goto L_0x003c
                r3 = r3 ^ 16256(0x3f80, float:2.278E-41)
                r1 = r2
                goto L_0x002c
            L_0x003c:
                int r1 = r2 + 1
                byte r5 = r0[r2]
                int r5 = r5 << 21
                r3 = r3 ^ r5
                if (r3 >= 0) goto L_0x004a
                r5 = -2080896(0xffffffffffe03f80, float:NaN)
                r3 = r3 ^ r5
                goto L_0x002c
            L_0x004a:
                int r2 = r1 + 1
                byte r4 = r0[r1]
                int r5 = r4 << 28
                r3 = r3 ^ r5
                r5 = 266354560(0xfe03f80, float:2.2112565E-29)
                r3 = r3 ^ r5
                if (r4 >= 0) goto L_0x0076
                int r1 = r2 + 1
                byte r5 = r0[r2]
                if (r5 >= 0) goto L_0x002c
                int r2 = r1 + 1
                byte r5 = r0[r1]
                if (r5 >= 0) goto L_0x0076
                int r1 = r2 + 1
                byte r5 = r0[r2]
                if (r5 >= 0) goto L_0x002c
                int r2 = r1 + 1
                byte r5 = r0[r1]
                if (r5 >= 0) goto L_0x0076
                int r1 = r2 + 1
                byte r5 = r0[r2]
                if (r5 >= 0) goto L_0x002c
                goto L_0x0006
            L_0x0076:
                r1 = r2
                goto L_0x002c
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.CodedInputStream.StreamDecoder.readRawVarint32():int");
        }

        private void skipRawVarint() throws IOException {
            if (this.bufferSize - this.pos >= 10) {
                skipRawVarintFastPath();
            } else {
                skipRawVarintSlowPath();
            }
        }

        private void skipRawVarintFastPath() throws IOException {
            int i = 0;
            while (i < 10) {
                byte[] bArr = this.buffer;
                int i2 = this.pos;
                this.pos = i2 + 1;
                if (bArr[i2] < 0) {
                    i++;
                } else {
                    return;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        private void skipRawVarintSlowPath() throws IOException {
            int i = 0;
            while (i < 10) {
                if (readRawByte() < 0) {
                    i++;
                } else {
                    return;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:34:0x00bb, code lost:
            if (((long) r0[r2]) < 0) goto L_0x0008;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public long readRawVarint64() throws java.io.IOException {
            /*
                r14 = this;
                r12 = 0
                int r1 = r14.pos
                int r6 = r14.bufferSize
                if (r6 != r1) goto L_0x000d
            L_0x0008:
                long r4 = r14.readRawVarint64SlowPath()
            L_0x000c:
                return r4
            L_0x000d:
                byte[] r0 = r14.buffer
                int r2 = r1 + 1
                byte r3 = r0[r1]
                if (r3 < 0) goto L_0x001a
                r14.pos = r2
                long r4 = (long) r3
                r1 = r2
                goto L_0x000c
            L_0x001a:
                int r6 = r14.bufferSize
                int r6 = r6 - r2
                r7 = 9
                if (r6 >= r7) goto L_0x0023
                r1 = r2
                goto L_0x0008
            L_0x0023:
                int r1 = r2 + 1
                byte r6 = r0[r2]
                int r6 = r6 << 7
                r3 = r3 ^ r6
                if (r3 >= 0) goto L_0x0032
                r6 = r3 ^ -128(0xffffffffffffff80, float:NaN)
                long r4 = (long) r6
            L_0x002f:
                r14.pos = r1
                goto L_0x000c
            L_0x0032:
                int r2 = r1 + 1
                byte r6 = r0[r1]
                int r6 = r6 << 14
                r3 = r3 ^ r6
                if (r3 < 0) goto L_0x0040
                r6 = r3 ^ 16256(0x3f80, float:2.278E-41)
                long r4 = (long) r6
                r1 = r2
                goto L_0x002f
            L_0x0040:
                int r1 = r2 + 1
                byte r6 = r0[r2]
                int r6 = r6 << 21
                r3 = r3 ^ r6
                if (r3 >= 0) goto L_0x004f
                r6 = -2080896(0xffffffffffe03f80, float:NaN)
                r6 = r6 ^ r3
                long r4 = (long) r6
                goto L_0x002f
            L_0x004f:
                long r6 = (long) r3
                int r2 = r1 + 1
                byte r8 = r0[r1]
                long r8 = (long) r8
                r10 = 28
                long r8 = r8 << r10
                long r4 = r6 ^ r8
                int r6 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r6 < 0) goto L_0x0064
                r6 = 266354560(0xfe03f80, double:1.315966377E-315)
                long r4 = r4 ^ r6
                r1 = r2
                goto L_0x002f
            L_0x0064:
                int r1 = r2 + 1
                byte r6 = r0[r2]
                long r6 = (long) r6
                r8 = 35
                long r6 = r6 << r8
                long r4 = r4 ^ r6
                int r6 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r6 >= 0) goto L_0x0078
                r6 = -34093383808(0xfffffff80fe03f80, double:NaN)
                long r4 = r4 ^ r6
                goto L_0x002f
            L_0x0078:
                int r2 = r1 + 1
                byte r6 = r0[r1]
                long r6 = (long) r6
                r8 = 42
                long r6 = r6 << r8
                long r4 = r4 ^ r6
                int r6 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r6 < 0) goto L_0x008d
                r6 = 4363953127296(0x3f80fe03f80, double:2.1560793202584E-311)
                long r4 = r4 ^ r6
                r1 = r2
                goto L_0x002f
            L_0x008d:
                int r1 = r2 + 1
                byte r6 = r0[r2]
                long r6 = (long) r6
                r8 = 49
                long r6 = r6 << r8
                long r4 = r4 ^ r6
                int r6 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r6 >= 0) goto L_0x00a1
                r6 = -558586000294016(0xfffe03f80fe03f80, double:NaN)
                long r4 = r4 ^ r6
                goto L_0x002f
            L_0x00a1:
                int r2 = r1 + 1
                byte r6 = r0[r1]
                long r6 = (long) r6
                r8 = 56
                long r6 = r6 << r8
                long r4 = r4 ^ r6
                r6 = 71499008037633920(0xfe03f80fe03f80, double:6.838959413692434E-304)
                long r4 = r4 ^ r6
                int r6 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r6 >= 0) goto L_0x00bf
                int r1 = r2 + 1
                byte r6 = r0[r2]
                long r6 = (long) r6
                int r6 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
                if (r6 >= 0) goto L_0x002f
                goto L_0x0008
            L_0x00bf:
                r1 = r2
                goto L_0x002f
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.CodedInputStream.StreamDecoder.readRawVarint64():long");
        }

        /* access modifiers changed from: package-private */
        public long readRawVarint64SlowPath() throws IOException {
            long result = 0;
            for (int shift = 0; shift < 64; shift += 7) {
                byte b = readRawByte();
                result |= ((long) (b & ByteCompanionObject.MAX_VALUE)) << shift;
                if ((b & 128) == 0) {
                    return result;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        public int readRawLittleEndian32() throws IOException {
            int tempPos = this.pos;
            if (this.bufferSize - tempPos < 4) {
                refillBuffer(4);
                tempPos = this.pos;
            }
            byte[] buffer2 = this.buffer;
            this.pos = tempPos + 4;
            return (buffer2[tempPos] & 255) | ((buffer2[tempPos + 1] & 255) << 8) | ((buffer2[tempPos + 2] & 255) << Tnaf.POW_2_WIDTH) | ((buffer2[tempPos + 3] & 255) << 24);
        }

        public long readRawLittleEndian64() throws IOException {
            int tempPos = this.pos;
            if (this.bufferSize - tempPos < 8) {
                refillBuffer(8);
                tempPos = this.pos;
            }
            byte[] buffer2 = this.buffer;
            this.pos = tempPos + 8;
            return (((long) buffer2[tempPos]) & 255) | ((((long) buffer2[tempPos + 1]) & 255) << 8) | ((((long) buffer2[tempPos + 2]) & 255) << 16) | ((((long) buffer2[tempPos + 3]) & 255) << 24) | ((((long) buffer2[tempPos + 4]) & 255) << 32) | ((((long) buffer2[tempPos + 5]) & 255) << 40) | ((((long) buffer2[tempPos + 6]) & 255) << 48) | ((((long) buffer2[tempPos + 7]) & 255) << 56);
        }

        public void enableAliasing(boolean enabled) {
        }

        public void resetSizeCounter() {
            this.totalBytesRetired = -this.pos;
        }

        public int pushLimit(int byteLimit) throws InvalidProtocolBufferException {
            if (byteLimit < 0) {
                throw InvalidProtocolBufferException.negativeSize();
            }
            int byteLimit2 = byteLimit + this.totalBytesRetired + this.pos;
            int oldLimit = this.currentLimit;
            if (byteLimit2 > oldLimit) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            this.currentLimit = byteLimit2;
            recomputeBufferSizeAfterLimit();
            return oldLimit;
        }

        private void recomputeBufferSizeAfterLimit() {
            this.bufferSize += this.bufferSizeAfterLimit;
            int bufferEnd = this.totalBytesRetired + this.bufferSize;
            if (bufferEnd > this.currentLimit) {
                this.bufferSizeAfterLimit = bufferEnd - this.currentLimit;
                this.bufferSize -= this.bufferSizeAfterLimit;
                return;
            }
            this.bufferSizeAfterLimit = 0;
        }

        public void popLimit(int oldLimit) {
            this.currentLimit = oldLimit;
            recomputeBufferSizeAfterLimit();
        }

        public int getBytesUntilLimit() {
            if (this.currentLimit == Integer.MAX_VALUE) {
                return -1;
            }
            return this.currentLimit - (this.totalBytesRetired + this.pos);
        }

        public boolean isAtEnd() throws IOException {
            return this.pos == this.bufferSize && !tryRefillBuffer(1);
        }

        public int getTotalBytesRead() {
            return this.totalBytesRetired + this.pos;
        }

        private void refillBuffer(int n) throws IOException {
            if (tryRefillBuffer(n)) {
                return;
            }
            if (n > (this.sizeLimit - this.totalBytesRetired) - this.pos) {
                throw InvalidProtocolBufferException.sizeLimitExceeded();
            }
            throw InvalidProtocolBufferException.truncatedMessage();
        }

        private boolean tryRefillBuffer(int n) throws IOException {
            if (this.pos + n <= this.bufferSize) {
                throw new IllegalStateException("refillBuffer() called when " + n + " bytes were already available in buffer");
            } else if (n > (this.sizeLimit - this.totalBytesRetired) - this.pos || this.totalBytesRetired + this.pos + n > this.currentLimit) {
                return false;
            } else {
                if (this.refillCallback != null) {
                    this.refillCallback.onRefill();
                }
                int tempPos = this.pos;
                if (tempPos > 0) {
                    if (this.bufferSize > tempPos) {
                        System.arraycopy(this.buffer, tempPos, this.buffer, 0, this.bufferSize - tempPos);
                    }
                    this.totalBytesRetired += tempPos;
                    this.bufferSize -= tempPos;
                    this.pos = 0;
                }
                int bytesRead = this.input.read(this.buffer, this.bufferSize, Math.min(this.buffer.length - this.bufferSize, (this.sizeLimit - this.totalBytesRetired) - this.bufferSize));
                if (bytesRead == 0 || bytesRead < -1 || bytesRead > this.buffer.length) {
                    throw new IllegalStateException(this.input.getClass() + "#read(byte[]) returned invalid result: " + bytesRead + "\nThe InputStream implementation is buggy.");
                } else if (bytesRead <= 0) {
                    return false;
                } else {
                    this.bufferSize += bytesRead;
                    recomputeBufferSizeAfterLimit();
                    if (this.bufferSize >= n) {
                        return true;
                    }
                    return tryRefillBuffer(n);
                }
            }
        }

        public byte readRawByte() throws IOException {
            if (this.pos == this.bufferSize) {
                refillBuffer(1);
            }
            byte[] bArr = this.buffer;
            int i = this.pos;
            this.pos = i + 1;
            return bArr[i];
        }

        public byte[] readRawBytes(int size) throws IOException {
            int tempPos = this.pos;
            if (size > this.bufferSize - tempPos || size <= 0) {
                return readRawBytesSlowPath(size, false);
            }
            this.pos = tempPos + size;
            return Arrays.copyOfRange(this.buffer, tempPos, tempPos + size);
        }

        private byte[] readRawBytesSlowPath(int size, boolean ensureNoLeakedReferences) throws IOException {
            byte[] result = readRawBytesSlowPathOneChunk(size);
            if (result == null) {
                int originalBufferPos = this.pos;
                int bufferedBytes = this.bufferSize - this.pos;
                this.totalBytesRetired += this.bufferSize;
                this.pos = 0;
                this.bufferSize = 0;
                List<byte[]> chunks = readRawBytesSlowPathRemainingChunks(size - bufferedBytes);
                byte[] bytes = new byte[size];
                System.arraycopy(this.buffer, originalBufferPos, bytes, 0, bufferedBytes);
                int tempPos = bufferedBytes;
                for (byte[] chunk : chunks) {
                    System.arraycopy(chunk, 0, bytes, tempPos, chunk.length);
                    tempPos += chunk.length;
                }
                return bytes;
            } else if (ensureNoLeakedReferences) {
                return (byte[]) result.clone();
            } else {
                return result;
            }
        }

        private byte[] readRawBytesSlowPathOneChunk(int size) throws IOException {
            if (size == 0) {
                return Internal.EMPTY_BYTE_ARRAY;
            }
            if (size < 0) {
                throw InvalidProtocolBufferException.negativeSize();
            }
            int currentMessageSize = this.totalBytesRetired + this.pos + size;
            if (currentMessageSize - this.sizeLimit > 0) {
                throw InvalidProtocolBufferException.sizeLimitExceeded();
            } else if (currentMessageSize > this.currentLimit) {
                skipRawBytes((this.currentLimit - this.totalBytesRetired) - this.pos);
                throw InvalidProtocolBufferException.truncatedMessage();
            } else {
                int bufferedBytes = this.bufferSize - this.pos;
                int sizeLeft = size - bufferedBytes;
                if (sizeLeft >= 4096 && sizeLeft > this.input.available()) {
                    return null;
                }
                byte[] bytes = new byte[size];
                System.arraycopy(this.buffer, this.pos, bytes, 0, bufferedBytes);
                this.totalBytesRetired += this.bufferSize;
                this.pos = 0;
                this.bufferSize = 0;
                int tempPos = bufferedBytes;
                while (tempPos < bytes.length) {
                    int n = this.input.read(bytes, tempPos, size - tempPos);
                    if (n == -1) {
                        throw InvalidProtocolBufferException.truncatedMessage();
                    }
                    this.totalBytesRetired += n;
                    tempPos += n;
                }
                return bytes;
            }
        }

        private List<byte[]> readRawBytesSlowPathRemainingChunks(int sizeLeft) throws IOException {
            List<byte[]> chunks = new ArrayList<>();
            while (sizeLeft > 0) {
                byte[] chunk = new byte[Math.min(sizeLeft, 4096)];
                int tempPos = 0;
                while (tempPos < chunk.length) {
                    int n = this.input.read(chunk, tempPos, chunk.length - tempPos);
                    if (n == -1) {
                        throw InvalidProtocolBufferException.truncatedMessage();
                    }
                    this.totalBytesRetired += n;
                    tempPos += n;
                }
                sizeLeft -= chunk.length;
                chunks.add(chunk);
            }
            return chunks;
        }

        private ByteString readBytesSlowPath(int size) throws IOException {
            byte[] result = readRawBytesSlowPathOneChunk(size);
            if (result != null) {
                return ByteString.copyFrom(result);
            }
            int originalBufferPos = this.pos;
            int bufferedBytes = this.bufferSize - this.pos;
            this.totalBytesRetired += this.bufferSize;
            this.pos = 0;
            this.bufferSize = 0;
            List<byte[]> chunks = readRawBytesSlowPathRemainingChunks(size - bufferedBytes);
            byte[] bytes = new byte[size];
            System.arraycopy(this.buffer, originalBufferPos, bytes, 0, bufferedBytes);
            int tempPos = bufferedBytes;
            for (byte[] chunk : chunks) {
                System.arraycopy(chunk, 0, bytes, tempPos, chunk.length);
                tempPos += chunk.length;
            }
            return ByteString.wrap(bytes);
        }

        public void skipRawBytes(int size) throws IOException {
            if (size > this.bufferSize - this.pos || size < 0) {
                skipRawBytesSlowPath(size);
            } else {
                this.pos += size;
            }
        }

        private void skipRawBytesSlowPath(int size) throws IOException {
            if (size < 0) {
                throw InvalidProtocolBufferException.negativeSize();
            } else if (this.totalBytesRetired + this.pos + size > this.currentLimit) {
                skipRawBytes((this.currentLimit - this.totalBytesRetired) - this.pos);
                throw InvalidProtocolBufferException.truncatedMessage();
            } else {
                int totalSkipped = 0;
                if (this.refillCallback == null) {
                    this.totalBytesRetired += this.pos;
                    totalSkipped = this.bufferSize - this.pos;
                    this.bufferSize = 0;
                    this.pos = 0;
                    while (totalSkipped < size) {
                        int toSkip = size - totalSkipped;
                        try {
                            long skipped = this.input.skip((long) toSkip);
                            if (skipped < 0 || skipped > ((long) toSkip)) {
                                throw new IllegalStateException(this.input.getClass() + "#skip returned invalid result: " + skipped + "\nThe InputStream implementation is buggy.");
                            } else if (skipped == 0) {
                                break;
                            } else {
                                totalSkipped += (int) skipped;
                            }
                        } catch (Throwable th) {
                            this.totalBytesRetired += totalSkipped;
                            recomputeBufferSizeAfterLimit();
                            throw th;
                        }
                    }
                    this.totalBytesRetired += totalSkipped;
                    recomputeBufferSizeAfterLimit();
                }
                if (totalSkipped < size) {
                    int tempPos = this.bufferSize - this.pos;
                    this.pos = this.bufferSize;
                    refillBuffer(1);
                    while (size - tempPos > this.bufferSize) {
                        tempPos += this.bufferSize;
                        this.pos = this.bufferSize;
                        refillBuffer(1);
                    }
                    this.pos = size - tempPos;
                }
            }
        }
    }

    private static final class IterableDirectByteBufferDecoder extends CodedInputStream {
        private int bufferSizeAfterCurrentLimit;
        private long currentAddress;
        private ByteBuffer currentByteBuffer;
        private long currentByteBufferLimit;
        private long currentByteBufferPos;
        private long currentByteBufferStartPos;
        private int currentLimit;
        private boolean enableAliasing;
        private boolean immutable;
        private Iterable<ByteBuffer> input;
        private Iterator<ByteBuffer> iterator;
        private int lastTag;
        private int startOffset;
        private int totalBufferSize;
        private int totalBytesRead;

        private IterableDirectByteBufferDecoder(Iterable<ByteBuffer> inputBufs, int size, boolean immutableFlag) {
            super();
            this.currentLimit = Integer.MAX_VALUE;
            this.totalBufferSize = size;
            this.input = inputBufs;
            this.iterator = this.input.iterator();
            this.immutable = immutableFlag;
            this.totalBytesRead = 0;
            this.startOffset = 0;
            if (size == 0) {
                this.currentByteBuffer = Internal.EMPTY_BYTE_BUFFER;
                this.currentByteBufferPos = 0;
                this.currentByteBufferStartPos = 0;
                this.currentByteBufferLimit = 0;
                this.currentAddress = 0;
                return;
            }
            tryGetNextByteBuffer();
        }

        private void getNextByteBuffer() throws InvalidProtocolBufferException {
            if (!this.iterator.hasNext()) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            tryGetNextByteBuffer();
        }

        private void tryGetNextByteBuffer() {
            this.currentByteBuffer = this.iterator.next();
            this.totalBytesRead += (int) (this.currentByteBufferPos - this.currentByteBufferStartPos);
            this.currentByteBufferPos = (long) this.currentByteBuffer.position();
            this.currentByteBufferStartPos = this.currentByteBufferPos;
            this.currentByteBufferLimit = (long) this.currentByteBuffer.limit();
            this.currentAddress = UnsafeUtil.addressOffset(this.currentByteBuffer);
            this.currentByteBufferPos += this.currentAddress;
            this.currentByteBufferStartPos += this.currentAddress;
            this.currentByteBufferLimit += this.currentAddress;
        }

        public int readTag() throws IOException {
            if (isAtEnd()) {
                this.lastTag = 0;
                return 0;
            }
            this.lastTag = readRawVarint32();
            if (WireFormat.getTagFieldNumber(this.lastTag) != 0) {
                return this.lastTag;
            }
            throw InvalidProtocolBufferException.invalidTag();
        }

        public void checkLastTagWas(int value) throws InvalidProtocolBufferException {
            if (this.lastTag != value) {
                throw InvalidProtocolBufferException.invalidEndTag();
            }
        }

        public int getLastTag() {
            return this.lastTag;
        }

        public boolean skipField(int tag) throws IOException {
            switch (WireFormat.getTagWireType(tag)) {
                case 0:
                    skipRawVarint();
                    return true;
                case 1:
                    skipRawBytes(8);
                    return true;
                case 2:
                    skipRawBytes(readRawVarint32());
                    return true;
                case 3:
                    skipMessage();
                    checkLastTagWas(WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4));
                    return true;
                case 4:
                    return false;
                case 5:
                    skipRawBytes(4);
                    return true;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        /* JADX INFO: Multiple debug info for r2v1 com.google.protobuf.ByteString: [D('value' long), D('value' com.google.protobuf.ByteString)] */
        public boolean skipField(int tag, CodedOutputStream output) throws IOException {
            switch (WireFormat.getTagWireType(tag)) {
                case 0:
                    long value = readInt64();
                    output.writeRawVarint32(tag);
                    output.writeUInt64NoTag(value);
                    return true;
                case 1:
                    long value2 = readRawLittleEndian64();
                    output.writeRawVarint32(tag);
                    output.writeFixed64NoTag(value2);
                    return true;
                case 2:
                    ByteString value3 = readBytes();
                    output.writeRawVarint32(tag);
                    output.writeBytesNoTag(value3);
                    return true;
                case 3:
                    output.writeRawVarint32(tag);
                    skipMessage(output);
                    int endtag = WireFormat.makeTag(WireFormat.getTagFieldNumber(tag), 4);
                    checkLastTagWas(endtag);
                    output.writeRawVarint32(endtag);
                    return true;
                case 4:
                    return false;
                case 5:
                    int value4 = readRawLittleEndian32();
                    output.writeRawVarint32(tag);
                    output.writeFixed32NoTag(value4);
                    return true;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        public void skipMessage() throws IOException {
            int tag;
            do {
                tag = readTag();
                if (tag == 0) {
                    return;
                }
            } while (skipField(tag));
        }

        public void skipMessage(CodedOutputStream output) throws IOException {
            int tag;
            do {
                tag = readTag();
                if (tag == 0) {
                    return;
                }
            } while (skipField(tag, output));
        }

        public double readDouble() throws IOException {
            return Double.longBitsToDouble(readRawLittleEndian64());
        }

        public float readFloat() throws IOException {
            return Float.intBitsToFloat(readRawLittleEndian32());
        }

        public long readUInt64() throws IOException {
            return readRawVarint64();
        }

        public long readInt64() throws IOException {
            return readRawVarint64();
        }

        public int readInt32() throws IOException {
            return readRawVarint32();
        }

        public long readFixed64() throws IOException {
            return readRawLittleEndian64();
        }

        public int readFixed32() throws IOException {
            return readRawLittleEndian32();
        }

        public boolean readBool() throws IOException {
            return readRawVarint64() != 0;
        }

        public String readString() throws IOException {
            int size = readRawVarint32();
            if (size > 0 && ((long) size) <= this.currentByteBufferLimit - this.currentByteBufferPos) {
                byte[] bytes = new byte[size];
                UnsafeUtil.copyMemory(this.currentByteBufferPos, bytes, 0, (long) size);
                String result = new String(bytes, Internal.UTF_8);
                this.currentByteBufferPos += (long) size;
                return result;
            } else if (size > 0 && size <= remaining()) {
                byte[] bytes2 = new byte[size];
                readRawBytesTo(bytes2, 0, size);
                return new String(bytes2, Internal.UTF_8);
            } else if (size == 0) {
                return "";
            } else {
                if (size < 0) {
                    throw InvalidProtocolBufferException.negativeSize();
                }
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        public String readStringRequireUtf8() throws IOException {
            int size = readRawVarint32();
            if (size > 0 && ((long) size) <= this.currentByteBufferLimit - this.currentByteBufferPos) {
                String result = Utf8.decodeUtf8(this.currentByteBuffer, (int) (this.currentByteBufferPos - this.currentByteBufferStartPos), size);
                this.currentByteBufferPos += (long) size;
                return result;
            } else if (size >= 0 && size <= remaining()) {
                byte[] bytes = new byte[size];
                readRawBytesTo(bytes, 0, size);
                return Utf8.decodeUtf8(bytes, 0, size);
            } else if (size == 0) {
                return "";
            } else {
                if (size <= 0) {
                    throw InvalidProtocolBufferException.negativeSize();
                }
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        public void readGroup(int fieldNumber, MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            this.recursionDepth++;
            builder.mergeFrom(this, extensionRegistry);
            checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
            this.recursionDepth--;
        }

        public <T extends MessageLite> T readGroup(int fieldNumber, Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            this.recursionDepth++;
            T result = (MessageLite) parser.parsePartialFrom(this, extensionRegistry);
            checkLastTagWas(WireFormat.makeTag(fieldNumber, 4));
            this.recursionDepth--;
            return result;
        }

        @Deprecated
        public void readUnknownGroup(int fieldNumber, MessageLite.Builder builder) throws IOException {
            readGroup(fieldNumber, builder, ExtensionRegistryLite.getEmptyRegistry());
        }

        public void readMessage(MessageLite.Builder builder, ExtensionRegistryLite extensionRegistry) throws IOException {
            int length = readRawVarint32();
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            int oldLimit = pushLimit(length);
            this.recursionDepth++;
            builder.mergeFrom(this, extensionRegistry);
            checkLastTagWas(0);
            this.recursionDepth--;
            popLimit(oldLimit);
        }

        public <T extends MessageLite> T readMessage(Parser<T> parser, ExtensionRegistryLite extensionRegistry) throws IOException {
            int length = readRawVarint32();
            if (this.recursionDepth >= this.recursionLimit) {
                throw InvalidProtocolBufferException.recursionLimitExceeded();
            }
            int oldLimit = pushLimit(length);
            this.recursionDepth++;
            T result = (MessageLite) parser.parsePartialFrom(this, extensionRegistry);
            checkLastTagWas(0);
            this.recursionDepth--;
            popLimit(oldLimit);
            return result;
        }

        public ByteString readBytes() throws IOException {
            int size = readRawVarint32();
            if (size <= 0 || ((long) size) > this.currentByteBufferLimit - this.currentByteBufferPos) {
                if (size > 0 && size <= remaining()) {
                    byte[] temp = new byte[size];
                    readRawBytesTo(temp, 0, size);
                    return ByteString.wrap(temp);
                } else if (size == 0) {
                    return ByteString.EMPTY;
                } else {
                    if (size < 0) {
                        throw InvalidProtocolBufferException.negativeSize();
                    }
                    throw InvalidProtocolBufferException.truncatedMessage();
                }
            } else if (!this.immutable || !this.enableAliasing) {
                byte[] bytes = new byte[size];
                UnsafeUtil.copyMemory(this.currentByteBufferPos, bytes, 0, (long) size);
                this.currentByteBufferPos += (long) size;
                return ByteString.wrap(bytes);
            } else {
                int idx = (int) (this.currentByteBufferPos - this.currentAddress);
                ByteString result = ByteString.wrap(slice(idx, idx + size));
                this.currentByteBufferPos += (long) size;
                return result;
            }
        }

        public byte[] readByteArray() throws IOException {
            return readRawBytes(readRawVarint32());
        }

        public ByteBuffer readByteBuffer() throws IOException {
            int size = readRawVarint32();
            if (size <= 0 || ((long) size) > currentRemaining()) {
                if (size > 0 && size <= remaining()) {
                    byte[] temp = new byte[size];
                    readRawBytesTo(temp, 0, size);
                    return ByteBuffer.wrap(temp);
                } else if (size == 0) {
                    return Internal.EMPTY_BYTE_BUFFER;
                } else {
                    if (size < 0) {
                        throw InvalidProtocolBufferException.negativeSize();
                    }
                    throw InvalidProtocolBufferException.truncatedMessage();
                }
            } else if (this.immutable || !this.enableAliasing) {
                byte[] bytes = new byte[size];
                UnsafeUtil.copyMemory(this.currentByteBufferPos, bytes, 0, (long) size);
                this.currentByteBufferPos += (long) size;
                return ByteBuffer.wrap(bytes);
            } else {
                this.currentByteBufferPos += (long) size;
                return slice((int) ((this.currentByteBufferPos - this.currentAddress) - ((long) size)), (int) (this.currentByteBufferPos - this.currentAddress));
            }
        }

        public int readUInt32() throws IOException {
            return readRawVarint32();
        }

        public int readEnum() throws IOException {
            return readRawVarint32();
        }

        public int readSFixed32() throws IOException {
            return readRawLittleEndian32();
        }

        public long readSFixed64() throws IOException {
            return readRawLittleEndian64();
        }

        public int readSInt32() throws IOException {
            return decodeZigZag32(readRawVarint32());
        }

        public long readSInt64() throws IOException {
            return decodeZigZag64(readRawVarint64());
        }

        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0092, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r2) < 0) goto L_0x000c;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int readRawVarint32() throws java.io.IOException {
            /*
                r12 = this;
                r10 = 1
                long r0 = r12.currentByteBufferPos
                long r6 = r12.currentByteBufferLimit
                long r8 = r12.currentByteBufferPos
                int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r6 != 0) goto L_0x0012
            L_0x000c:
                long r6 = r12.readRawVarint64SlowPath()
                int r4 = (int) r6
            L_0x0011:
                return r4
            L_0x0012:
                long r2 = r0 + r10
                byte r4 = com.google.protobuf.UnsafeUtil.getByte(r0)
                if (r4 < 0) goto L_0x0021
                long r6 = r12.currentByteBufferPos
                long r6 = r6 + r10
                r12.currentByteBufferPos = r6
                r0 = r2
                goto L_0x0011
            L_0x0021:
                long r6 = r12.currentByteBufferLimit
                long r8 = r12.currentByteBufferPos
                long r6 = r6 - r8
                r8 = 10
                int r6 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
                if (r6 >= 0) goto L_0x002e
                r0 = r2
                goto L_0x000c
            L_0x002e:
                long r0 = r2 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r2)
                int r6 = r6 << 7
                r4 = r4 ^ r6
                if (r4 >= 0) goto L_0x003e
                r4 = r4 ^ -128(0xffffffffffffff80, float:NaN)
            L_0x003b:
                r12.currentByteBufferPos = r0
                goto L_0x0011
            L_0x003e:
                long r2 = r0 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r0)
                int r6 = r6 << 14
                r4 = r4 ^ r6
                if (r4 < 0) goto L_0x004d
                r4 = r4 ^ 16256(0x3f80, float:2.278E-41)
                r0 = r2
                goto L_0x003b
            L_0x004d:
                long r0 = r2 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r2)
                int r6 = r6 << 21
                r4 = r4 ^ r6
                if (r4 >= 0) goto L_0x005d
                r6 = -2080896(0xffffffffffe03f80, float:NaN)
                r4 = r4 ^ r6
                goto L_0x003b
            L_0x005d:
                long r2 = r0 + r10
                byte r5 = com.google.protobuf.UnsafeUtil.getByte(r0)
                int r6 = r5 << 28
                r4 = r4 ^ r6
                r6 = 266354560(0xfe03f80, float:2.2112565E-29)
                r4 = r4 ^ r6
                if (r5 >= 0) goto L_0x0096
                long r0 = r2 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r2)
                if (r6 >= 0) goto L_0x003b
                long r2 = r0 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r0)
                if (r6 >= 0) goto L_0x0096
                long r0 = r2 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r2)
                if (r6 >= 0) goto L_0x003b
                long r2 = r0 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r0)
                if (r6 >= 0) goto L_0x0096
                long r0 = r2 + r10
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r2)
                if (r6 >= 0) goto L_0x003b
                goto L_0x000c
            L_0x0096:
                r0 = r2
                goto L_0x003b
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.CodedInputStream.IterableDirectByteBufferDecoder.readRawVarint32():int");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:34:0x00f9, code lost:
            if (((long) com.google.protobuf.UnsafeUtil.getByte(r2)) < 0) goto L_0x000a;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public long readRawVarint64() throws java.io.IOException {
            /*
                r12 = this;
                long r0 = r12.currentByteBufferPos
                long r8 = r12.currentByteBufferLimit
                long r10 = r12.currentByteBufferPos
                int r7 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                if (r7 != 0) goto L_0x000f
            L_0x000a:
                long r4 = r12.readRawVarint64SlowPath()
            L_0x000e:
                return r4
            L_0x000f:
                r8 = 1
                long r2 = r0 + r8
                byte r6 = com.google.protobuf.UnsafeUtil.getByte(r0)
                if (r6 < 0) goto L_0x0023
                long r8 = r12.currentByteBufferPos
                r10 = 1
                long r8 = r8 + r10
                r12.currentByteBufferPos = r8
                long r4 = (long) r6
                r0 = r2
                goto L_0x000e
            L_0x0023:
                long r8 = r12.currentByteBufferLimit
                long r10 = r12.currentByteBufferPos
                long r8 = r8 - r10
                r10 = 10
                int r7 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                if (r7 >= 0) goto L_0x0030
                r0 = r2
                goto L_0x000a
            L_0x0030:
                r8 = 1
                long r0 = r2 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r2)
                int r7 = r7 << 7
                r6 = r6 ^ r7
                if (r6 >= 0) goto L_0x0043
                r7 = r6 ^ -128(0xffffffffffffff80, float:NaN)
                long r4 = (long) r7
            L_0x0040:
                r12.currentByteBufferPos = r0
                goto L_0x000e
            L_0x0043:
                r8 = 1
                long r2 = r0 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r0)
                int r7 = r7 << 14
                r6 = r6 ^ r7
                if (r6 < 0) goto L_0x0055
                r7 = r6 ^ 16256(0x3f80, float:2.278E-41)
                long r4 = (long) r7
                r0 = r2
                goto L_0x0040
            L_0x0055:
                r8 = 1
                long r0 = r2 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r2)
                int r7 = r7 << 21
                r6 = r6 ^ r7
                if (r6 >= 0) goto L_0x0068
                r7 = -2080896(0xffffffffffe03f80, float:NaN)
                r7 = r7 ^ r6
                long r4 = (long) r7
                goto L_0x0040
            L_0x0068:
                long r8 = (long) r6
                r10 = 1
                long r2 = r0 + r10
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r0)
                long r10 = (long) r7
                r7 = 28
                long r10 = r10 << r7
                long r4 = r8 ^ r10
                r8 = 0
                int r7 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r7 < 0) goto L_0x0083
                r8 = 266354560(0xfe03f80, double:1.315966377E-315)
                long r4 = r4 ^ r8
                r0 = r2
                goto L_0x0040
            L_0x0083:
                r8 = 1
                long r0 = r2 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r2)
                long r8 = (long) r7
                r7 = 35
                long r8 = r8 << r7
                long r4 = r4 ^ r8
                r8 = 0
                int r7 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r7 >= 0) goto L_0x009d
                r8 = -34093383808(0xfffffff80fe03f80, double:NaN)
                long r4 = r4 ^ r8
                goto L_0x0040
            L_0x009d:
                r8 = 1
                long r2 = r0 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r0)
                long r8 = (long) r7
                r7 = 42
                long r8 = r8 << r7
                long r4 = r4 ^ r8
                r8 = 0
                int r7 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r7 < 0) goto L_0x00b8
                r8 = 4363953127296(0x3f80fe03f80, double:2.1560793202584E-311)
                long r4 = r4 ^ r8
                r0 = r2
                goto L_0x0040
            L_0x00b8:
                r8 = 1
                long r0 = r2 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r2)
                long r8 = (long) r7
                r7 = 49
                long r8 = r8 << r7
                long r4 = r4 ^ r8
                r8 = 0
                int r7 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r7 >= 0) goto L_0x00d3
                r8 = -558586000294016(0xfffe03f80fe03f80, double:NaN)
                long r4 = r4 ^ r8
                goto L_0x0040
            L_0x00d3:
                r8 = 1
                long r2 = r0 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r0)
                long r8 = (long) r7
                r7 = 56
                long r8 = r8 << r7
                long r4 = r4 ^ r8
                r8 = 71499008037633920(0xfe03f80fe03f80, double:6.838959413692434E-304)
                long r4 = r4 ^ r8
                r8 = 0
                int r7 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
                if (r7 >= 0) goto L_0x00fd
                r8 = 1
                long r0 = r2 + r8
                byte r7 = com.google.protobuf.UnsafeUtil.getByte(r2)
                long r8 = (long) r7
                r10 = 0
                int r7 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1))
                if (r7 >= 0) goto L_0x0040
                goto L_0x000a
            L_0x00fd:
                r0 = r2
                goto L_0x0040
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.CodedInputStream.IterableDirectByteBufferDecoder.readRawVarint64():long");
        }

        /* access modifiers changed from: package-private */
        public long readRawVarint64SlowPath() throws IOException {
            long result = 0;
            for (int shift = 0; shift < 64; shift += 7) {
                byte b = readRawByte();
                result |= ((long) (b & ByteCompanionObject.MAX_VALUE)) << shift;
                if ((b & 128) == 0) {
                    return result;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        public int readRawLittleEndian32() throws IOException {
            if (currentRemaining() < 4) {
                return (readRawByte() & 255) | ((readRawByte() & 255) << 8) | ((readRawByte() & 255) << Tnaf.POW_2_WIDTH) | ((readRawByte() & 255) << 24);
            }
            long tempPos = this.currentByteBufferPos;
            this.currentByteBufferPos += 4;
            return (UnsafeUtil.getByte(tempPos) & 255) | ((UnsafeUtil.getByte(1 + tempPos) & 255) << 8) | ((UnsafeUtil.getByte(2 + tempPos) & 255) << Tnaf.POW_2_WIDTH) | ((UnsafeUtil.getByte(3 + tempPos) & 255) << 24);
        }

        public long readRawLittleEndian64() throws IOException {
            if (currentRemaining() < 8) {
                return (((long) readRawByte()) & 255) | ((((long) readRawByte()) & 255) << 8) | ((((long) readRawByte()) & 255) << 16) | ((((long) readRawByte()) & 255) << 24) | ((((long) readRawByte()) & 255) << 32) | ((((long) readRawByte()) & 255) << 40) | ((((long) readRawByte()) & 255) << 48) | ((((long) readRawByte()) & 255) << 56);
            }
            long tempPos = this.currentByteBufferPos;
            this.currentByteBufferPos += 8;
            return (((long) UnsafeUtil.getByte(tempPos)) & 255) | ((((long) UnsafeUtil.getByte(1 + tempPos)) & 255) << 8) | ((((long) UnsafeUtil.getByte(2 + tempPos)) & 255) << 16) | ((((long) UnsafeUtil.getByte(3 + tempPos)) & 255) << 24) | ((((long) UnsafeUtil.getByte(4 + tempPos)) & 255) << 32) | ((((long) UnsafeUtil.getByte(5 + tempPos)) & 255) << 40) | ((((long) UnsafeUtil.getByte(6 + tempPos)) & 255) << 48) | ((((long) UnsafeUtil.getByte(7 + tempPos)) & 255) << 56);
        }

        public void enableAliasing(boolean enabled) {
            this.enableAliasing = enabled;
        }

        public void resetSizeCounter() {
            this.startOffset = (int) ((((long) this.totalBytesRead) + this.currentByteBufferPos) - this.currentByteBufferStartPos);
        }

        public int pushLimit(int byteLimit) throws InvalidProtocolBufferException {
            if (byteLimit < 0) {
                throw InvalidProtocolBufferException.negativeSize();
            }
            int byteLimit2 = byteLimit + getTotalBytesRead();
            int oldLimit = this.currentLimit;
            if (byteLimit2 > oldLimit) {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
            this.currentLimit = byteLimit2;
            recomputeBufferSizeAfterLimit();
            return oldLimit;
        }

        private void recomputeBufferSizeAfterLimit() {
            this.totalBufferSize += this.bufferSizeAfterCurrentLimit;
            int bufferEnd = this.totalBufferSize - this.startOffset;
            if (bufferEnd > this.currentLimit) {
                this.bufferSizeAfterCurrentLimit = bufferEnd - this.currentLimit;
                this.totalBufferSize -= this.bufferSizeAfterCurrentLimit;
                return;
            }
            this.bufferSizeAfterCurrentLimit = 0;
        }

        public void popLimit(int oldLimit) {
            this.currentLimit = oldLimit;
            recomputeBufferSizeAfterLimit();
        }

        public int getBytesUntilLimit() {
            if (this.currentLimit == Integer.MAX_VALUE) {
                return -1;
            }
            return this.currentLimit - getTotalBytesRead();
        }

        public boolean isAtEnd() throws IOException {
            return (((long) this.totalBytesRead) + this.currentByteBufferPos) - this.currentByteBufferStartPos == ((long) this.totalBufferSize);
        }

        public int getTotalBytesRead() {
            return (int) ((((long) (this.totalBytesRead - this.startOffset)) + this.currentByteBufferPos) - this.currentByteBufferStartPos);
        }

        public byte readRawByte() throws IOException {
            if (currentRemaining() == 0) {
                getNextByteBuffer();
            }
            long j = this.currentByteBufferPos;
            this.currentByteBufferPos = 1 + j;
            return UnsafeUtil.getByte(j);
        }

        public byte[] readRawBytes(int length) throws IOException {
            if (length >= 0 && ((long) length) <= currentRemaining()) {
                byte[] bytes = new byte[length];
                UnsafeUtil.copyMemory(this.currentByteBufferPos, bytes, 0, (long) length);
                this.currentByteBufferPos += (long) length;
                return bytes;
            } else if (length >= 0 && length <= remaining()) {
                byte[] bytes2 = new byte[length];
                readRawBytesTo(bytes2, 0, length);
                return bytes2;
            } else if (length > 0) {
                throw InvalidProtocolBufferException.truncatedMessage();
            } else if (length == 0) {
                return Internal.EMPTY_BYTE_ARRAY;
            } else {
                throw InvalidProtocolBufferException.negativeSize();
            }
        }

        private void readRawBytesTo(byte[] bytes, int offset, int length) throws IOException {
            if (length >= 0 && length <= remaining()) {
                int l = length;
                while (l > 0) {
                    if (currentRemaining() == 0) {
                        getNextByteBuffer();
                    }
                    int bytesToCopy = Math.min(l, (int) currentRemaining());
                    UnsafeUtil.copyMemory(this.currentByteBufferPos, bytes, (long) ((length - l) + offset), (long) bytesToCopy);
                    l -= bytesToCopy;
                    this.currentByteBufferPos += (long) bytesToCopy;
                }
            } else if (length > 0) {
                throw InvalidProtocolBufferException.truncatedMessage();
            } else if (length != 0) {
                throw InvalidProtocolBufferException.negativeSize();
            }
        }

        public void skipRawBytes(int length) throws IOException {
            if (length >= 0 && ((long) length) <= (((long) (this.totalBufferSize - this.totalBytesRead)) - this.currentByteBufferPos) + this.currentByteBufferStartPos) {
                int l = length;
                while (l > 0) {
                    if (currentRemaining() == 0) {
                        getNextByteBuffer();
                    }
                    int rl = Math.min(l, (int) currentRemaining());
                    l -= rl;
                    this.currentByteBufferPos += (long) rl;
                }
            } else if (length < 0) {
                throw InvalidProtocolBufferException.negativeSize();
            } else {
                throw InvalidProtocolBufferException.truncatedMessage();
            }
        }

        private void skipRawVarint() throws IOException {
            int i = 0;
            while (i < 10) {
                if (readRawByte() < 0) {
                    i++;
                } else {
                    return;
                }
            }
            throw InvalidProtocolBufferException.malformedVarint();
        }

        private int remaining() {
            return (int) ((((long) (this.totalBufferSize - this.totalBytesRead)) - this.currentByteBufferPos) + this.currentByteBufferStartPos);
        }

        private long currentRemaining() {
            return this.currentByteBufferLimit - this.currentByteBufferPos;
        }

        private ByteBuffer slice(int begin, int end) throws IOException {
            int prevPos = this.currentByteBuffer.position();
            int prevLimit = this.currentByteBuffer.limit();
            try {
                this.currentByteBuffer.position(begin);
                this.currentByteBuffer.limit(end);
                ByteBuffer slice = this.currentByteBuffer.slice();
                this.currentByteBuffer.position(prevPos);
                this.currentByteBuffer.limit(prevLimit);
                return slice;
            } catch (IllegalArgumentException e) {
                throw InvalidProtocolBufferException.truncatedMessage();
            } catch (Throwable th) {
                this.currentByteBuffer.position(prevPos);
                this.currentByteBuffer.limit(prevLimit);
                throw th;
            }
        }
    }
}
