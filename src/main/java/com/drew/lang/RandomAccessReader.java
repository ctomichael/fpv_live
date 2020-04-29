package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.StringValue;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public abstract class RandomAccessReader {
    private boolean _isMotorolaByteOrder = true;

    public abstract byte getByte(int i) throws IOException;

    @NotNull
    public abstract byte[] getBytes(int i, int i2) throws IOException;

    public abstract long getLength() throws IOException;

    /* access modifiers changed from: protected */
    public abstract boolean isValidIndex(int i, int i2) throws IOException;

    public abstract int toUnshiftedOffset(int i);

    /* access modifiers changed from: protected */
    public abstract void validateIndex(int i, int i2) throws IOException;

    public void setMotorolaByteOrder(boolean motorolaByteOrder) {
        this._isMotorolaByteOrder = motorolaByteOrder;
    }

    public boolean isMotorolaByteOrder() {
        return this._isMotorolaByteOrder;
    }

    public boolean getBit(int index) throws IOException {
        int byteIndex = index / 8;
        validateIndex(byteIndex, 1);
        if (((getByte(byteIndex) >> (index % 8)) & 1) == 1) {
            return true;
        }
        return false;
    }

    public short getUInt8(int index) throws IOException {
        validateIndex(index, 1);
        return (short) (getByte(index) & 255);
    }

    public byte getInt8(int index) throws IOException {
        validateIndex(index, 1);
        return getByte(index);
    }

    public int getUInt16(int index) throws IOException {
        validateIndex(index, 2);
        if (this._isMotorolaByteOrder) {
            return ((getByte(index) << 8) & 65280) | (getByte(index + 1) & 255);
        }
        return ((getByte(index + 1) << 8) & 65280) | (getByte(index) & 255);
    }

    public short getInt16(int index) throws IOException {
        validateIndex(index, 2);
        if (this._isMotorolaByteOrder) {
            return (short) (((((short) getByte(index)) << 8) & -256) | (((short) getByte(index + 1)) & 255));
        }
        return (short) (((((short) getByte(index + 1)) << 8) & -256) | (((short) getByte(index)) & 255));
    }

    public int getInt24(int index) throws IOException {
        validateIndex(index, 3);
        if (this._isMotorolaByteOrder) {
            return ((getByte(index) << Tnaf.POW_2_WIDTH) & 16711680) | ((getByte(index + 1) << 8) & 65280) | (getByte(index + 2) & 255);
        }
        return ((getByte(index + 2) << Tnaf.POW_2_WIDTH) & 16711680) | ((getByte(index + 1) << 8) & 65280) | (getByte(index) & 255);
    }

    public long getUInt32(int index) throws IOException {
        validateIndex(index, 4);
        if (this._isMotorolaByteOrder) {
            return ((((long) getByte(index)) << 24) & 4278190080L) | ((((long) getByte(index + 1)) << 16) & 16711680) | ((((long) getByte(index + 2)) << 8) & 65280) | (((long) getByte(index + 3)) & 255);
        }
        return ((((long) getByte(index + 3)) << 24) & 4278190080L) | ((((long) getByte(index + 2)) << 16) & 16711680) | ((((long) getByte(index + 1)) << 8) & 65280) | (((long) getByte(index)) & 255);
    }

    public int getInt32(int index) throws IOException {
        validateIndex(index, 4);
        if (this._isMotorolaByteOrder) {
            return ((getByte(index) << 24) & -16777216) | ((getByte(index + 1) << Tnaf.POW_2_WIDTH) & 16711680) | ((getByte(index + 2) << 8) & 65280) | (getByte(index + 3) & 255);
        }
        return ((getByte(index + 3) << 24) & -16777216) | ((getByte(index + 2) << Tnaf.POW_2_WIDTH) & 16711680) | ((getByte(index + 1) << 8) & 65280) | (getByte(index) & 255);
    }

    public long getInt64(int index) throws IOException {
        validateIndex(index, 8);
        if (this._isMotorolaByteOrder) {
            return ((((long) getByte(index)) << 56) & -72057594037927936L) | ((((long) getByte(index + 1)) << 48) & 71776119061217280L) | ((((long) getByte(index + 2)) << 40) & 280375465082880L) | ((((long) getByte(index + 3)) << 32) & 1095216660480L) | ((((long) getByte(index + 4)) << 24) & 4278190080L) | ((((long) getByte(index + 5)) << 16) & 16711680) | ((((long) getByte(index + 6)) << 8) & 65280) | (((long) getByte(index + 7)) & 255);
        }
        return ((((long) getByte(index + 7)) << 56) & -72057594037927936L) | ((((long) getByte(index + 6)) << 48) & 71776119061217280L) | ((((long) getByte(index + 5)) << 40) & 280375465082880L) | ((((long) getByte(index + 4)) << 32) & 1095216660480L) | ((((long) getByte(index + 3)) << 24) & 4278190080L) | ((((long) getByte(index + 2)) << 16) & 16711680) | ((((long) getByte(index + 1)) << 8) & 65280) | (((long) getByte(index)) & 255);
    }

    public float getS15Fixed16(int index) throws IOException {
        validateIndex(index, 4);
        if (this._isMotorolaByteOrder) {
            return (float) (((double) ((float) (((getByte(index) & 255) << 8) | (getByte(index + 1) & 255)))) + (((double) (((getByte(index + 2) & 255) << 8) | (getByte(index + 3) & 255))) / 65536.0d));
        }
        return (float) (((double) ((float) (((getByte(index + 3) & 255) << 8) | (getByte(index + 2) & 255)))) + (((double) (((getByte(index + 1) & 255) << 8) | (getByte(index) & 255))) / 65536.0d));
    }

    public float getFloat32(int index) throws IOException {
        return Float.intBitsToFloat(getInt32(index));
    }

    public double getDouble64(int index) throws IOException {
        return Double.longBitsToDouble(getInt64(index));
    }

    @NotNull
    public StringValue getStringValue(int index, int bytesRequested, @Nullable Charset charset) throws IOException {
        return new StringValue(getBytes(index, bytesRequested), charset);
    }

    @NotNull
    public String getString(int index, int bytesRequested, @NotNull Charset charset) throws IOException {
        return new String(getBytes(index, bytesRequested), charset.name());
    }

    @NotNull
    public String getString(int index, int bytesRequested, @NotNull String charset) throws IOException {
        byte[] bytes = getBytes(index, bytesRequested);
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            return new String(bytes);
        }
    }

    @NotNull
    public String getNullTerminatedString(int index, int maxLengthBytes, @NotNull Charset charset) throws IOException {
        return new String(getNullTerminatedBytes(index, maxLengthBytes), charset.name());
    }

    @NotNull
    public StringValue getNullTerminatedStringValue(int index, int maxLengthBytes, @Nullable Charset charset) throws IOException {
        return new StringValue(getNullTerminatedBytes(index, maxLengthBytes), charset);
    }

    @NotNull
    public byte[] getNullTerminatedBytes(int index, int maxLengthBytes) throws IOException {
        byte[] buffer = getBytes(index, maxLengthBytes);
        int length = 0;
        while (length < buffer.length && buffer[length] != 0) {
            length++;
        }
        if (length == maxLengthBytes) {
            return buffer;
        }
        byte[] bytes = new byte[length];
        if (length > 0) {
            System.arraycopy(buffer, 0, bytes, 0, length);
        }
        return bytes;
    }
}
