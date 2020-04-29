package org.msgpack.core;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.HashMap;
import kotlin.UShort;
import kotlin.jvm.internal.LongCompanionObject;
import org.msgpack.core.MessagePack;
import org.msgpack.core.buffer.MessageBuffer;
import org.msgpack.core.buffer.MessageBufferInput;
import org.msgpack.value.ImmutableValue;
import org.msgpack.value.Value;
import org.msgpack.value.ValueFactory;
import org.msgpack.value.Variable;

public class MessageUnpacker implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final MessageBuffer EMPTY_BUFFER = MessageBuffer.wrap(new byte[0]);
    private static final String EMPTY_STRING = "";
    private final CodingErrorAction actionOnMalformedString;
    private final CodingErrorAction actionOnUnmappableString;
    private final boolean allowReadingBinaryAsString;
    private final boolean allowReadingStringAsBinary;
    private MessageBuffer buffer = EMPTY_BUFFER;
    private CharBuffer decodeBuffer;
    private StringBuilder decodeStringBuffer;
    private CharsetDecoder decoder;

    /* renamed from: in  reason: collision with root package name */
    private MessageBufferInput f27in;
    private int nextReadPosition;
    private final MessageBuffer numberBuffer = MessageBuffer.allocate(8);
    private int position;
    private final int stringDecoderBufferSize;
    private final int stringSizeLimit;
    private long totalReadBytes;

    static {
        boolean z;
        if (!MessageUnpacker.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    protected MessageUnpacker(MessageBufferInput messageBufferInput, MessagePack.UnpackerConfig unpackerConfig) {
        this.f27in = (MessageBufferInput) Preconditions.checkNotNull(messageBufferInput, "MessageBufferInput is null");
        this.allowReadingStringAsBinary = unpackerConfig.getAllowReadingStringAsBinary();
        this.allowReadingBinaryAsString = unpackerConfig.getAllowReadingBinaryAsString();
        this.actionOnMalformedString = unpackerConfig.getActionOnMalformedString();
        this.actionOnUnmappableString = unpackerConfig.getActionOnUnmappableString();
        this.stringSizeLimit = unpackerConfig.getStringSizeLimit();
        this.stringDecoderBufferSize = unpackerConfig.getStringDecoderBufferSize();
    }

    public MessageBufferInput reset(MessageBufferInput messageBufferInput) throws IOException {
        MessageBufferInput messageBufferInput2 = this.f27in;
        this.f27in = (MessageBufferInput) Preconditions.checkNotNull(messageBufferInput, "MessageBufferInput is null");
        this.buffer = EMPTY_BUFFER;
        this.position = 0;
        this.totalReadBytes = 0;
        return messageBufferInput2;
    }

    public long getTotalReadBytes() {
        return this.totalReadBytes + ((long) this.position);
    }

    private MessageBuffer getNextBuffer() throws IOException {
        MessageBuffer next = this.f27in.next();
        if (next == null) {
            throw new MessageInsufficientBufferException();
        } else if ($assertionsDisabled || this.buffer != null) {
            this.totalReadBytes += (long) this.buffer.size();
            return next;
        } else {
            throw new AssertionError();
        }
    }

    private void nextBuffer() throws IOException {
        this.buffer = getNextBuffer();
        this.position = 0;
    }

    private MessageBuffer prepareNumberBuffer(int i) throws IOException {
        int i2;
        int size = this.buffer.size() - this.position;
        if (size >= i) {
            this.nextReadPosition = this.position;
            this.position += i;
            return this.buffer;
        }
        if (size > 0) {
            this.numberBuffer.putBytes(0, this.buffer.array(), this.buffer.arrayOffset() + this.position, size);
            i -= size;
            i2 = size + 0;
        } else {
            i2 = 0;
        }
        while (true) {
            nextBuffer();
            int size2 = this.buffer.size();
            if (size2 >= i) {
                this.numberBuffer.putBytes(i2, this.buffer.array(), this.buffer.arrayOffset(), i);
                this.position = i;
                this.nextReadPosition = 0;
                return this.numberBuffer;
            }
            this.numberBuffer.putBytes(i2, this.buffer.array(), this.buffer.arrayOffset(), size2);
            i -= size2;
            i2 += size2;
        }
    }

    private static int utf8MultibyteCharacterSize(byte b) {
        return Integer.numberOfLeadingZeros(((b & 255) ^ -1) << 24);
    }

    public boolean hasNext() throws IOException {
        while (this.buffer.size() <= this.position) {
            MessageBuffer next = this.f27in.next();
            if (next == null) {
                return false;
            }
            this.totalReadBytes += (long) this.buffer.size();
            this.buffer = next;
            this.position = 0;
        }
        return true;
    }

    public MessageFormat getNextFormat() throws IOException {
        if (hasNext()) {
            return MessageFormat.valueOf(this.buffer.getByte(this.position));
        }
        throw new MessageInsufficientBufferException();
    }

    private byte readByte() throws IOException {
        if (this.buffer.size() > this.position) {
            byte b = this.buffer.getByte(this.position);
            this.position++;
            return b;
        }
        nextBuffer();
        if (this.buffer.size() <= 0) {
            return readByte();
        }
        byte b2 = this.buffer.getByte(0);
        this.position = 1;
        return b2;
    }

    private short readShort() throws IOException {
        return prepareNumberBuffer(2).getShort(this.nextReadPosition);
    }

    private int readInt() throws IOException {
        return prepareNumberBuffer(4).getInt(this.nextReadPosition);
    }

    private long readLong() throws IOException {
        return prepareNumberBuffer(8).getLong(this.nextReadPosition);
    }

    private float readFloat() throws IOException {
        return prepareNumberBuffer(4).getFloat(this.nextReadPosition);
    }

    private double readDouble() throws IOException {
        return prepareNumberBuffer(8).getDouble(this.nextReadPosition);
    }

    public void skipValue() throws IOException {
        int i = 1;
        while (i > 0) {
            byte readByte = readByte();
            switch (MessageFormat.valueOf(readByte)) {
                case FIXMAP:
                    i += (readByte & 15) * 2;
                    break;
                case FIXARRAY:
                    i += readByte & 15;
                    break;
                case FIXSTR:
                    skipPayload(readByte & 31);
                    break;
                case INT8:
                case UINT8:
                    skipPayload(1);
                    break;
                case INT16:
                case UINT16:
                    skipPayload(2);
                    break;
                case INT32:
                case UINT32:
                case FLOAT32:
                    skipPayload(4);
                    break;
                case INT64:
                case UINT64:
                case FLOAT64:
                    skipPayload(8);
                    break;
                case BIN8:
                case STR8:
                    skipPayload(readNextLength8());
                    break;
                case BIN16:
                case STR16:
                    skipPayload(readNextLength16());
                    break;
                case BIN32:
                case STR32:
                    skipPayload(readNextLength32());
                    break;
                case FIXEXT1:
                    skipPayload(2);
                    break;
                case FIXEXT2:
                    skipPayload(3);
                    break;
                case FIXEXT4:
                    skipPayload(5);
                    break;
                case FIXEXT8:
                    skipPayload(9);
                    break;
                case FIXEXT16:
                    skipPayload(17);
                    break;
                case EXT8:
                    skipPayload(readNextLength8() + 1);
                    break;
                case EXT16:
                    skipPayload(readNextLength16() + 1);
                    break;
                case EXT32:
                    skipPayload(readNextLength32() + 1);
                    break;
                case ARRAY16:
                    i += readNextLength16();
                    break;
                case ARRAY32:
                    i += readNextLength32();
                    break;
                case MAP16:
                    i += readNextLength16() * 2;
                    break;
                case MAP32:
                    i += readNextLength32() * 2;
                    break;
                case NEVER_USED:
                    throw new MessageNeverUsedFormatException("Encountered 0xC1 \"NEVER_USED\" byte");
            }
            i--;
        }
    }

    private static MessagePackException unexpected(String str, byte b) {
        MessageFormat valueOf = MessageFormat.valueOf(b);
        if (valueOf == MessageFormat.NEVER_USED) {
            return new MessageNeverUsedFormatException(String.format("Expected %s, but encountered 0xC1 \"NEVER_USED\" byte", str));
        }
        String name = valueOf.getValueType().name();
        return new MessageTypeException(String.format("Expected %s, but got %s (%02x)", str, name.substring(0, 1) + name.substring(1).toLowerCase(), Byte.valueOf(b)));
    }

    public ImmutableValue unpackValue() throws IOException {
        MessageFormat nextFormat;
        int i = 0;
        switch (getNextFormat().getValueType()) {
            case NIL:
                readByte();
                return ValueFactory.newNil();
            case BOOLEAN:
                return ValueFactory.newBoolean(unpackBoolean());
            case INTEGER:
                switch (nextFormat) {
                    case UINT64:
                        return ValueFactory.newInteger(unpackBigInteger());
                    default:
                        return ValueFactory.newInteger(unpackLong());
                }
            case FLOAT:
                return ValueFactory.newFloat(unpackDouble());
            case STRING:
                return ValueFactory.newString(readPayload(unpackRawStringHeader()), true);
            case BINARY:
                return ValueFactory.newBinary(readPayload(unpackBinaryHeader()), true);
            case ARRAY:
                int unpackArrayHeader = unpackArrayHeader();
                Value[] valueArr = new Value[unpackArrayHeader];
                while (i < unpackArrayHeader) {
                    valueArr[i] = unpackValue();
                    i++;
                }
                return ValueFactory.newArray(valueArr, true);
            case MAP:
                int unpackMapHeader = unpackMapHeader();
                Value[] valueArr2 = new Value[(unpackMapHeader * 2)];
                while (i < unpackMapHeader * 2) {
                    valueArr2[i] = unpackValue();
                    int i2 = i + 1;
                    valueArr2[i2] = unpackValue();
                    i = i2 + 1;
                }
                return ValueFactory.newMap(valueArr2, true);
            case EXTENSION:
                ExtensionTypeHeader unpackExtensionTypeHeader = unpackExtensionTypeHeader();
                return ValueFactory.newExtension(unpackExtensionTypeHeader.getType(), readPayload(unpackExtensionTypeHeader.getLength()));
            default:
                throw new MessageNeverUsedFormatException("Unknown value type");
        }
    }

    public Variable unpackValue(Variable variable) throws IOException {
        MessageFormat nextFormat;
        int i = 0;
        switch (getNextFormat().getValueType()) {
            case NIL:
                readByte();
                variable.setNilValue();
                break;
            case BOOLEAN:
                variable.setBooleanValue(unpackBoolean());
                break;
            case INTEGER:
                switch (nextFormat) {
                    case UINT64:
                        variable.setIntegerValue(unpackBigInteger());
                        break;
                    default:
                        variable.setIntegerValue(unpackLong());
                        break;
                }
            case FLOAT:
                variable.setFloatValue(unpackDouble());
                break;
            case STRING:
                variable.setStringValue(readPayload(unpackRawStringHeader()));
                break;
            case BINARY:
                variable.setBinaryValue(readPayload(unpackBinaryHeader()));
                break;
            case ARRAY:
                int unpackArrayHeader = unpackArrayHeader();
                ArrayList arrayList = new ArrayList(unpackArrayHeader);
                while (i < unpackArrayHeader) {
                    arrayList.add(unpackValue());
                    i++;
                }
                variable.setArrayValue(arrayList);
                break;
            case MAP:
                int unpackMapHeader = unpackMapHeader();
                HashMap hashMap = new HashMap();
                while (i < unpackMapHeader) {
                    hashMap.put(unpackValue(), unpackValue());
                    i++;
                }
                variable.setMapValue(hashMap);
                break;
            case EXTENSION:
                ExtensionTypeHeader unpackExtensionTypeHeader = unpackExtensionTypeHeader();
                variable.setExtensionValue(unpackExtensionTypeHeader.getType(), readPayload(unpackExtensionTypeHeader.getLength()));
                break;
            default:
                throw new MessageFormatException("Unknown value type");
        }
        return variable;
    }

    public void unpackNil() throws IOException {
        byte readByte = readByte();
        if (readByte != -64) {
            throw unexpected("Nil", readByte);
        }
    }

    public boolean unpackBoolean() throws IOException {
        byte readByte = readByte();
        if (readByte == -62) {
            return false;
        }
        if (readByte == -61) {
            return true;
        }
        throw unexpected("boolean", readByte);
    }

    public byte unpackByte() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixInt(readByte)) {
            return readByte;
        }
        switch (readByte) {
            case -52:
                byte readByte2 = readByte();
                if (readByte2 >= 0) {
                    return readByte2;
                }
                throw overflowU8(readByte2);
            case -51:
                short readShort = readShort();
                if (readShort >= 0 && readShort <= 127) {
                    return (byte) readShort;
                }
                throw overflowU16(readShort);
            case -50:
                int readInt = readInt();
                if (readInt >= 0 && readInt <= 127) {
                    return (byte) readInt;
                }
                throw overflowU32(readInt);
            case -49:
                long readLong = readLong();
                if (readLong >= 0 && readLong <= 127) {
                    return (byte) ((int) readLong);
                }
                throw overflowU64(readLong);
            case -48:
                return readByte();
            case -47:
                short readShort2 = readShort();
                if (readShort2 >= -128 && readShort2 <= 127) {
                    return (byte) readShort2;
                }
                throw overflowI16(readShort2);
            case -46:
                int readInt2 = readInt();
                if (readInt2 >= -128 && readInt2 <= 127) {
                    return (byte) readInt2;
                }
                throw overflowI32(readInt2);
            case -45:
                long readLong2 = readLong();
                if (readLong2 >= -128 && readLong2 <= 127) {
                    return (byte) ((int) readLong2);
                }
                throw overflowI64(readLong2);
            default:
                throw unexpected("Integer", readByte);
        }
    }

    public short unpackShort() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixInt(readByte)) {
            return (short) readByte;
        }
        switch (readByte) {
            case -52:
                return (short) (readByte() & 255);
            case -51:
                short readShort = readShort();
                if (readShort >= 0) {
                    return readShort;
                }
                throw overflowU16(readShort);
            case -50:
                int readInt = readInt();
                if (readInt >= 0 && readInt <= 32767) {
                    return (short) readInt;
                }
                throw overflowU32(readInt);
            case -49:
                long readLong = readLong();
                if (readLong >= 0 && readLong <= 32767) {
                    return (short) ((int) readLong);
                }
                throw overflowU64(readLong);
            case -48:
                return (short) readByte();
            case -47:
                return readShort();
            case -46:
                int readInt2 = readInt();
                if (readInt2 >= -32768 && readInt2 <= 32767) {
                    return (short) readInt2;
                }
                throw overflowI32(readInt2);
            case -45:
                long readLong2 = readLong();
                if (readLong2 >= -32768 && readLong2 <= 32767) {
                    return (short) ((int) readLong2);
                }
                throw overflowI64(readLong2);
            default:
                throw unexpected("Integer", readByte);
        }
    }

    public int unpackInt() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixInt(readByte)) {
            return readByte;
        }
        switch (readByte) {
            case -52:
                return readByte() & 255;
            case -51:
                return readShort() & UShort.MAX_VALUE;
            case -50:
                int readInt = readInt();
                if (readInt >= 0) {
                    return readInt;
                }
                throw overflowU32(readInt);
            case -49:
                long readLong = readLong();
                if (readLong >= 0 && readLong <= 2147483647L) {
                    return (int) readLong;
                }
                throw overflowU64(readLong);
            case -48:
                return readByte();
            case -47:
                return readShort();
            case -46:
                return readInt();
            case -45:
                long readLong2 = readLong();
                if (readLong2 >= -2147483648L && readLong2 <= 2147483647L) {
                    return (int) readLong2;
                }
                throw overflowI64(readLong2);
            default:
                throw unexpected("Integer", readByte);
        }
    }

    public long unpackLong() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixInt(readByte)) {
            return (long) readByte;
        }
        switch (readByte) {
            case -52:
                return (long) (readByte() & 255);
            case -51:
                return (long) (readShort() & UShort.MAX_VALUE);
            case -50:
                int readInt = readInt();
                if (readInt < 0) {
                    return ((long) (readInt & Integer.MAX_VALUE)) + 2147483648L;
                }
                return (long) readInt;
            case -49:
                long readLong = readLong();
                if (readLong >= 0) {
                    return readLong;
                }
                throw overflowU64(readLong);
            case -48:
                return (long) readByte();
            case -47:
                return (long) readShort();
            case -46:
                return (long) readInt();
            case -45:
                return readLong();
            default:
                throw unexpected("Integer", readByte);
        }
    }

    public BigInteger unpackBigInteger() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixInt(readByte)) {
            return BigInteger.valueOf((long) readByte);
        }
        switch (readByte) {
            case -52:
                return BigInteger.valueOf((long) (readByte() & 255));
            case -51:
                return BigInteger.valueOf((long) (readShort() & UShort.MAX_VALUE));
            case -50:
                int readInt = readInt();
                if (readInt < 0) {
                    return BigInteger.valueOf(((long) (readInt & Integer.MAX_VALUE)) + 2147483648L);
                }
                return BigInteger.valueOf((long) readInt);
            case -49:
                long readLong = readLong();
                if (readLong < 0) {
                    return BigInteger.valueOf(readLong + LongCompanionObject.MAX_VALUE + 1).setBit(63);
                }
                return BigInteger.valueOf(readLong);
            case -48:
                return BigInteger.valueOf((long) readByte());
            case -47:
                return BigInteger.valueOf((long) readShort());
            case -46:
                return BigInteger.valueOf((long) readInt());
            case -45:
                return BigInteger.valueOf(readLong());
            default:
                throw unexpected("Integer", readByte);
        }
    }

    public float unpackFloat() throws IOException {
        byte readByte = readByte();
        switch (readByte) {
            case -54:
                return readFloat();
            case -53:
                return (float) readDouble();
            default:
                throw unexpected("Float", readByte);
        }
    }

    public double unpackDouble() throws IOException {
        byte readByte = readByte();
        switch (readByte) {
            case -54:
                return (double) readFloat();
            case -53:
                return readDouble();
            default:
                throw unexpected("Float", readByte);
        }
    }

    private void resetDecoder() {
        if (this.decoder == null) {
            this.decodeBuffer = CharBuffer.allocate(this.stringDecoderBufferSize);
            this.decoder = MessagePack.UTF8.newDecoder().onMalformedInput(this.actionOnMalformedString).onUnmappableCharacter(this.actionOnUnmappableString);
        } else {
            this.decoder.reset();
        }
        if (this.decodeStringBuffer == null) {
            this.decodeStringBuffer = new StringBuilder();
        } else {
            this.decodeStringBuffer.setLength(0);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
        r2.throwException();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0120, code lost:
        throw new org.msgpack.core.MessageFormatException("Unexpected UTF-8 multibyte sequence");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String unpackString() throws java.io.IOException {
        /*
            r7 = this;
            r5 = 0
            int r0 = r7.unpackRawStringHeader()
            if (r0 != 0) goto L_0x000b
            java.lang.String r0 = ""
        L_0x000a:
            return r0
        L_0x000b:
            int r1 = r7.stringSizeLimit
            if (r0 <= r1) goto L_0x002f
            org.msgpack.core.MessageSizeException r1 = new org.msgpack.core.MessageSizeException
            java.lang.String r2 = "cannot unpack a String of size larger than %,d: %,d"
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]
            int r4 = r7.stringSizeLimit
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            r3[r5] = r4
            r4 = 1
            java.lang.Integer r5 = java.lang.Integer.valueOf(r0)
            r3[r4] = r5
            java.lang.String r2 = java.lang.String.format(r2, r3)
            long r4 = (long) r0
            r1.<init>(r2, r4)
            throw r1
        L_0x002f:
            org.msgpack.core.buffer.MessageBuffer r1 = r7.buffer
            int r1 = r1.size()
            int r2 = r7.position
            int r1 = r1 - r2
            if (r1 < r0) goto L_0x003f
            java.lang.String r0 = r7.decodeStringFastPath(r0)
            goto L_0x000a
        L_0x003f:
            r7.resetDecoder()
        L_0x0042:
            if (r0 <= 0) goto L_0x0058
            org.msgpack.core.buffer.MessageBuffer r1 = r7.buffer     // Catch:{ CharacterCodingException -> 0x0065 }
            int r1 = r1.size()     // Catch:{ CharacterCodingException -> 0x0065 }
            int r2 = r7.position     // Catch:{ CharacterCodingException -> 0x0065 }
            int r1 = r1 - r2
            if (r1 < r0) goto L_0x005f
            java.lang.StringBuilder r1 = r7.decodeStringBuffer     // Catch:{ CharacterCodingException -> 0x0065 }
            java.lang.String r0 = r7.decodeStringFastPath(r0)     // Catch:{ CharacterCodingException -> 0x0065 }
            r1.append(r0)     // Catch:{ CharacterCodingException -> 0x0065 }
        L_0x0058:
            java.lang.StringBuilder r0 = r7.decodeStringBuffer     // Catch:{ CharacterCodingException -> 0x0065 }
            java.lang.String r0 = r0.toString()     // Catch:{ CharacterCodingException -> 0x0065 }
            goto L_0x000a
        L_0x005f:
            if (r1 != 0) goto L_0x006c
            r7.nextBuffer()     // Catch:{ CharacterCodingException -> 0x0065 }
            goto L_0x0042
        L_0x0065:
            r0 = move-exception
            org.msgpack.core.MessageStringCodingException r1 = new org.msgpack.core.MessageStringCodingException
            r1.<init>(r0)
            throw r1
        L_0x006c:
            org.msgpack.core.buffer.MessageBuffer r2 = r7.buffer     // Catch:{ CharacterCodingException -> 0x0065 }
            int r3 = r7.position     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.ByteBuffer r2 = r2.sliceAsByteBuffer(r3, r1)     // Catch:{ CharacterCodingException -> 0x0065 }
            int r3 = r2.position()     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.CharBuffer r4 = r7.decodeBuffer     // Catch:{ CharacterCodingException -> 0x0065 }
            r4.clear()     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.charset.CharsetDecoder r4 = r7.decoder     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.CharBuffer r5 = r7.decodeBuffer     // Catch:{ CharacterCodingException -> 0x0065 }
            r6 = 0
            java.nio.charset.CoderResult r4 = r4.decode(r2, r5, r6)     // Catch:{ CharacterCodingException -> 0x0065 }
            int r2 = r2.position()     // Catch:{ CharacterCodingException -> 0x0065 }
            int r2 = r2 - r3
            int r3 = r7.position     // Catch:{ CharacterCodingException -> 0x0065 }
            int r3 = r3 + r2
            r7.position = r3     // Catch:{ CharacterCodingException -> 0x0065 }
            int r0 = r0 - r2
            java.lang.StringBuilder r3 = r7.decodeStringBuffer     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.CharBuffer r5 = r7.decodeBuffer     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.Buffer r5 = r5.flip()     // Catch:{ CharacterCodingException -> 0x0065 }
            r3.append(r5)     // Catch:{ CharacterCodingException -> 0x0065 }
            boolean r3 = r4.isError()     // Catch:{ CharacterCodingException -> 0x0065 }
            if (r3 == 0) goto L_0x00a5
            r7.handleCoderError(r4)     // Catch:{ CharacterCodingException -> 0x0065 }
        L_0x00a5:
            boolean r3 = r4.isUnderflow()     // Catch:{ CharacterCodingException -> 0x0065 }
            if (r3 == 0) goto L_0x0042
            if (r2 >= r1) goto L_0x0042
            org.msgpack.core.buffer.MessageBuffer r1 = r7.buffer     // Catch:{ CharacterCodingException -> 0x0065 }
            int r2 = r7.position     // Catch:{ CharacterCodingException -> 0x0065 }
            byte r1 = r1.getByte(r2)     // Catch:{ CharacterCodingException -> 0x0065 }
            int r1 = utf8MultibyteCharacterSize(r1)     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.ByteBuffer r1 = java.nio.ByteBuffer.allocate(r1)     // Catch:{ CharacterCodingException -> 0x0065 }
            org.msgpack.core.buffer.MessageBuffer r2 = r7.buffer     // Catch:{ CharacterCodingException -> 0x0065 }
            int r3 = r7.position     // Catch:{ CharacterCodingException -> 0x0065 }
            org.msgpack.core.buffer.MessageBuffer r4 = r7.buffer     // Catch:{ CharacterCodingException -> 0x0065 }
            int r4 = r4.size()     // Catch:{ CharacterCodingException -> 0x0065 }
            int r5 = r7.position     // Catch:{ CharacterCodingException -> 0x0065 }
            int r4 = r4 - r5
            r2.getBytes(r3, r4, r1)     // Catch:{ CharacterCodingException -> 0x0065 }
        L_0x00cd:
            r7.nextBuffer()     // Catch:{ CharacterCodingException -> 0x0065 }
            int r2 = r1.remaining()     // Catch:{ CharacterCodingException -> 0x0065 }
            org.msgpack.core.buffer.MessageBuffer r3 = r7.buffer     // Catch:{ CharacterCodingException -> 0x0065 }
            int r3 = r3.size()     // Catch:{ CharacterCodingException -> 0x0065 }
            if (r3 < r2) goto L_0x012b
            org.msgpack.core.buffer.MessageBuffer r3 = r7.buffer     // Catch:{ CharacterCodingException -> 0x0065 }
            r4 = 0
            r3.getBytes(r4, r2, r1)     // Catch:{ CharacterCodingException -> 0x0065 }
            r7.position = r2     // Catch:{ CharacterCodingException -> 0x0065 }
            r2 = 0
            r1.position(r2)     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.CharBuffer r2 = r7.decodeBuffer     // Catch:{ CharacterCodingException -> 0x0065 }
            r2.clear()     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.charset.CharsetDecoder r2 = r7.decoder     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.CharBuffer r3 = r7.decodeBuffer     // Catch:{ CharacterCodingException -> 0x0065 }
            r4 = 0
            java.nio.charset.CoderResult r2 = r2.decode(r1, r3, r4)     // Catch:{ CharacterCodingException -> 0x0065 }
            boolean r3 = r2.isError()     // Catch:{ CharacterCodingException -> 0x0065 }
            if (r3 == 0) goto L_0x00ff
            r7.handleCoderError(r2)     // Catch:{ CharacterCodingException -> 0x0065 }
        L_0x00ff:
            boolean r3 = r2.isOverflow()     // Catch:{ CharacterCodingException -> 0x0065 }
            if (r3 != 0) goto L_0x0115
            boolean r3 = r2.isUnderflow()     // Catch:{ CharacterCodingException -> 0x0065 }
            if (r3 == 0) goto L_0x0140
            int r3 = r1.position()     // Catch:{ CharacterCodingException -> 0x0065 }
            int r4 = r1.limit()     // Catch:{ CharacterCodingException -> 0x0065 }
            if (r3 >= r4) goto L_0x0140
        L_0x0115:
            r2.throwException()     // Catch:{ Exception -> 0x0121 }
            org.msgpack.core.MessageFormatException r0 = new org.msgpack.core.MessageFormatException     // Catch:{ Exception -> 0x0121 }
            java.lang.String r1 = "Unexpected UTF-8 multibyte sequence"
            r0.<init>(r1)     // Catch:{ Exception -> 0x0121 }
            throw r0     // Catch:{ Exception -> 0x0121 }
        L_0x0121:
            r0 = move-exception
            org.msgpack.core.MessageFormatException r1 = new org.msgpack.core.MessageFormatException     // Catch:{ CharacterCodingException -> 0x0065 }
            java.lang.String r2 = "Unexpected UTF-8 multibyte sequence"
            r1.<init>(r2, r0)     // Catch:{ CharacterCodingException -> 0x0065 }
            throw r1     // Catch:{ CharacterCodingException -> 0x0065 }
        L_0x012b:
            org.msgpack.core.buffer.MessageBuffer r2 = r7.buffer     // Catch:{ CharacterCodingException -> 0x0065 }
            r3 = 0
            org.msgpack.core.buffer.MessageBuffer r4 = r7.buffer     // Catch:{ CharacterCodingException -> 0x0065 }
            int r4 = r4.size()     // Catch:{ CharacterCodingException -> 0x0065 }
            r2.getBytes(r3, r4, r1)     // Catch:{ CharacterCodingException -> 0x0065 }
            org.msgpack.core.buffer.MessageBuffer r2 = r7.buffer     // Catch:{ CharacterCodingException -> 0x0065 }
            int r2 = r2.size()     // Catch:{ CharacterCodingException -> 0x0065 }
            r7.position = r2     // Catch:{ CharacterCodingException -> 0x0065 }
            goto L_0x00cd
        L_0x0140:
            int r1 = r1.limit()     // Catch:{ CharacterCodingException -> 0x0065 }
            int r0 = r0 - r1
            java.lang.StringBuilder r1 = r7.decodeStringBuffer     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.CharBuffer r2 = r7.decodeBuffer     // Catch:{ CharacterCodingException -> 0x0065 }
            java.nio.Buffer r2 = r2.flip()     // Catch:{ CharacterCodingException -> 0x0065 }
            r1.append(r2)     // Catch:{ CharacterCodingException -> 0x0065 }
            goto L_0x0042
        */
        throw new UnsupportedOperationException("Method not decompiled: org.msgpack.core.MessageUnpacker.unpackString():java.lang.String");
    }

    private void handleCoderError(CoderResult coderResult) throws CharacterCodingException {
        if ((coderResult.isMalformed() && this.actionOnMalformedString == CodingErrorAction.REPORT) || (coderResult.isUnmappable() && this.actionOnUnmappableString == CodingErrorAction.REPORT)) {
            coderResult.throwException();
        }
    }

    private String decodeStringFastPath(int i) {
        if (this.actionOnMalformedString == CodingErrorAction.REPLACE && this.actionOnUnmappableString == CodingErrorAction.REPLACE) {
            String str = new String(this.buffer.array(), this.buffer.arrayOffset() + this.position, i, MessagePack.UTF8);
            this.position += i;
            return str;
        }
        resetDecoder();
        ByteBuffer sliceAsByteBuffer = this.buffer.sliceAsByteBuffer();
        sliceAsByteBuffer.limit(this.position + i);
        sliceAsByteBuffer.position(this.position);
        try {
            CharBuffer decode = this.decoder.decode(sliceAsByteBuffer);
            this.position += i;
            return decode.toString();
        } catch (CharacterCodingException e) {
            throw new MessageStringCodingException(e);
        }
    }

    public int unpackArrayHeader() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixedArray(readByte)) {
            return readByte & 15;
        }
        switch (readByte) {
            case -36:
                return readNextLength16();
            case -35:
                return readNextLength32();
            default:
                throw unexpected("Array", readByte);
        }
    }

    public int unpackMapHeader() throws IOException {
        byte readByte = readByte();
        if (MessagePack.Code.isFixedMap(readByte)) {
            return readByte & 15;
        }
        switch (readByte) {
            case -34:
                return readNextLength16();
            case -33:
                return readNextLength32();
            default:
                throw unexpected("Map", readByte);
        }
    }

    public ExtensionTypeHeader unpackExtensionTypeHeader() throws IOException {
        byte readByte = readByte();
        switch (readByte) {
            case -57:
                MessageBuffer prepareNumberBuffer = prepareNumberBuffer(2);
                return new ExtensionTypeHeader(prepareNumberBuffer.getByte(this.nextReadPosition + 1), prepareNumberBuffer.getByte(this.nextReadPosition) & 255);
            case -56:
                MessageBuffer prepareNumberBuffer2 = prepareNumberBuffer(3);
                return new ExtensionTypeHeader(prepareNumberBuffer2.getByte(this.nextReadPosition + 2), prepareNumberBuffer2.getShort(this.nextReadPosition) & UShort.MAX_VALUE);
            case -55:
                MessageBuffer prepareNumberBuffer3 = prepareNumberBuffer(5);
                int i = prepareNumberBuffer3.getInt(this.nextReadPosition);
                if (i >= 0) {
                    return new ExtensionTypeHeader(prepareNumberBuffer3.getByte(this.nextReadPosition + 4), i);
                }
                throw overflowU32Size(i);
            case -54:
            case -53:
            case -52:
            case -51:
            case -50:
            case -49:
            case -48:
            case -47:
            case -46:
            case -45:
            default:
                throw unexpected("Ext", readByte);
            case -44:
                return new ExtensionTypeHeader(readByte(), 1);
            case -43:
                return new ExtensionTypeHeader(readByte(), 2);
            case -42:
                return new ExtensionTypeHeader(readByte(), 4);
            case -41:
                return new ExtensionTypeHeader(readByte(), 8);
            case -40:
                return new ExtensionTypeHeader(readByte(), 16);
        }
    }

    private int tryReadStringHeader(byte b) throws IOException {
        switch (b) {
            case -39:
                return readNextLength8();
            case -38:
                return readNextLength16();
            case -37:
                return readNextLength32();
            default:
                return -1;
        }
    }

    private int tryReadBinaryHeader(byte b) throws IOException {
        switch (b) {
            case -60:
                return readNextLength8();
            case -59:
                return readNextLength16();
            case -58:
                return readNextLength32();
            default:
                return -1;
        }
    }

    public int unpackRawStringHeader() throws IOException {
        int tryReadBinaryHeader;
        byte readByte = readByte();
        if (MessagePack.Code.isFixedRaw(readByte)) {
            return readByte & 31;
        }
        int tryReadStringHeader = tryReadStringHeader(readByte);
        if (tryReadStringHeader >= 0) {
            return tryReadStringHeader;
        }
        if (this.allowReadingBinaryAsString && (tryReadBinaryHeader = tryReadBinaryHeader(readByte)) >= 0) {
            return tryReadBinaryHeader;
        }
        throw unexpected("String", readByte);
    }

    public int unpackBinaryHeader() throws IOException {
        int tryReadStringHeader;
        byte readByte = readByte();
        if (MessagePack.Code.isFixedRaw(readByte)) {
            return readByte & 31;
        }
        int tryReadBinaryHeader = tryReadBinaryHeader(readByte);
        if (tryReadBinaryHeader >= 0) {
            return tryReadBinaryHeader;
        }
        if (this.allowReadingStringAsBinary && (tryReadStringHeader = tryReadStringHeader(readByte)) >= 0) {
            return tryReadStringHeader;
        }
        throw unexpected("Binary", readByte);
    }

    private void skipPayload(int i) throws IOException {
        while (true) {
            int size = this.buffer.size() - this.position;
            if (size >= i) {
                this.position += i;
                return;
            }
            this.position += size;
            i -= size;
            nextBuffer();
        }
    }

    public void readPayload(ByteBuffer byteBuffer) throws IOException {
        while (true) {
            int remaining = byteBuffer.remaining();
            int size = this.buffer.size() - this.position;
            if (size >= remaining) {
                this.buffer.getBytes(this.position, remaining, byteBuffer);
                this.position = remaining + this.position;
                return;
            }
            this.buffer.getBytes(this.position, size, byteBuffer);
            this.position += size;
            nextBuffer();
        }
    }

    public void readPayload(byte[] bArr) throws IOException {
        readPayload(bArr, 0, bArr.length);
    }

    public byte[] readPayload(int i) throws IOException {
        byte[] bArr = new byte[i];
        readPayload(bArr);
        return bArr;
    }

    public void readPayload(byte[] bArr, int i, int i2) throws IOException {
        readPayload(ByteBuffer.wrap(bArr, i, i2));
    }

    public MessageBuffer readPayloadAsReference(int i) throws IOException {
        if (this.buffer.size() - this.position >= i) {
            MessageBuffer slice = this.buffer.slice(this.position, i);
            this.position += i;
            return slice;
        }
        MessageBuffer allocate = MessageBuffer.allocate(i);
        readPayload(allocate.sliceAsByteBuffer());
        return allocate;
    }

    private int readNextLength8() throws IOException {
        return readByte() & 255;
    }

    private int readNextLength16() throws IOException {
        return readShort() & UShort.MAX_VALUE;
    }

    private int readNextLength32() throws IOException {
        int readInt = readInt();
        if (readInt >= 0) {
            return readInt;
        }
        throw overflowU32Size(readInt);
    }

    public void close() throws IOException {
        this.buffer = EMPTY_BUFFER;
        this.position = 0;
        this.f27in.close();
    }

    private static MessageIntegerOverflowException overflowU8(byte b) {
        return new MessageIntegerOverflowException(BigInteger.valueOf((long) (b & 255)));
    }

    private static MessageIntegerOverflowException overflowU16(short s) {
        return new MessageIntegerOverflowException(BigInteger.valueOf((long) (65535 & s)));
    }

    private static MessageIntegerOverflowException overflowU32(int i) {
        return new MessageIntegerOverflowException(BigInteger.valueOf(((long) (Integer.MAX_VALUE & i)) + 2147483648L));
    }

    private static MessageIntegerOverflowException overflowU64(long j) {
        return new MessageIntegerOverflowException(BigInteger.valueOf(LongCompanionObject.MAX_VALUE + j + 1).setBit(63));
    }

    private static MessageIntegerOverflowException overflowI16(short s) {
        return new MessageIntegerOverflowException(BigInteger.valueOf((long) s));
    }

    private static MessageIntegerOverflowException overflowI32(int i) {
        return new MessageIntegerOverflowException(BigInteger.valueOf((long) i));
    }

    private static MessageIntegerOverflowException overflowI64(long j) {
        return new MessageIntegerOverflowException(BigInteger.valueOf(j));
    }

    private static MessageSizeException overflowU32Size(int i) {
        return new MessageSizeException(((long) (Integer.MAX_VALUE & i)) + 2147483648L);
    }
}
