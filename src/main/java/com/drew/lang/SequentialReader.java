package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.StringValue;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public abstract class SequentialReader {
    private boolean _isMotorolaByteOrder = true;

    public abstract int available();

    public abstract byte getByte() throws IOException;

    public abstract void getBytes(@NotNull byte[] bArr, int i, int i2) throws IOException;

    @NotNull
    public abstract byte[] getBytes(int i) throws IOException;

    public abstract long getPosition() throws IOException;

    public abstract void skip(long j) throws IOException;

    public abstract boolean trySkip(long j) throws IOException;

    public void setMotorolaByteOrder(boolean motorolaByteOrder) {
        this._isMotorolaByteOrder = motorolaByteOrder;
    }

    public boolean isMotorolaByteOrder() {
        return this._isMotorolaByteOrder;
    }

    public short getUInt8() throws IOException {
        return (short) (getByte() & 255);
    }

    public byte getInt8() throws IOException {
        return getByte();
    }

    public int getUInt16() throws IOException {
        if (this._isMotorolaByteOrder) {
            return ((getByte() << 8) & 65280) | (getByte() & 255);
        }
        return (getByte() & 255) | ((getByte() << 8) & 65280);
    }

    public short getInt16() throws IOException {
        if (this._isMotorolaByteOrder) {
            return (short) (((((short) getByte()) << 8) & -256) | (((short) getByte()) & 255));
        }
        return (short) ((((short) getByte()) & 255) | ((((short) getByte()) << 8) & -256));
    }

    public long getUInt32() throws IOException {
        if (this._isMotorolaByteOrder) {
            return ((((long) getByte()) << 24) & 4278190080L) | ((((long) getByte()) << 16) & 16711680) | ((((long) getByte()) << 8) & 65280) | (((long) getByte()) & 255);
        }
        return (((long) getByte()) & 255) | ((((long) getByte()) << 8) & 65280) | ((((long) getByte()) << 16) & 16711680) | ((((long) getByte()) << 24) & 4278190080L);
    }

    public int getInt32() throws IOException {
        if (this._isMotorolaByteOrder) {
            return ((getByte() << 24) & -16777216) | ((getByte() << Tnaf.POW_2_WIDTH) & 16711680) | ((getByte() << 8) & 65280) | (getByte() & 255);
        }
        return (getByte() & 255) | ((getByte() << 8) & 65280) | ((getByte() << Tnaf.POW_2_WIDTH) & 16711680) | ((getByte() << 24) & -16777216);
    }

    public long getInt64() throws IOException {
        if (this._isMotorolaByteOrder) {
            return ((((long) getByte()) << 56) & -72057594037927936L) | ((((long) getByte()) << 48) & 71776119061217280L) | ((((long) getByte()) << 40) & 280375465082880L) | ((((long) getByte()) << 32) & 1095216660480L) | ((((long) getByte()) << 24) & 4278190080L) | ((((long) getByte()) << 16) & 16711680) | ((((long) getByte()) << 8) & 65280) | (((long) getByte()) & 255);
        }
        return (((long) getByte()) & 255) | ((((long) getByte()) << 8) & 65280) | ((((long) getByte()) << 16) & 16711680) | ((((long) getByte()) << 24) & 4278190080L) | ((((long) getByte()) << 32) & 1095216660480L) | ((((long) getByte()) << 40) & 280375465082880L) | ((((long) getByte()) << 48) & 71776119061217280L) | ((((long) getByte()) << 56) & -72057594037927936L);
    }

    public float getS15Fixed16() throws IOException {
        if (this._isMotorolaByteOrder) {
            return (float) (((double) ((float) (((getByte() & 255) << 8) | (getByte() & 255)))) + (((double) (((getByte() & 255) << 8) | (getByte() & 255))) / 65536.0d));
        }
        return (float) (((double) ((float) ((getByte() & 255) | ((getByte() & 255) << 8)))) + (((double) ((getByte() & 255) | ((getByte() & 255) << 8))) / 65536.0d));
    }

    public float getFloat32() throws IOException {
        return Float.intBitsToFloat(getInt32());
    }

    public double getDouble64() throws IOException {
        return Double.longBitsToDouble(getInt64());
    }

    @NotNull
    public String getString(int bytesRequested) throws IOException {
        return new String(getBytes(bytesRequested));
    }

    @NotNull
    public String getString(int bytesRequested, String charset) throws IOException {
        byte[] bytes = getBytes(bytesRequested);
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            return new String(bytes);
        }
    }

    @NotNull
    public String getString(int bytesRequested, @NotNull Charset charset) throws IOException {
        return new String(getBytes(bytesRequested), charset);
    }

    @NotNull
    public StringValue getStringValue(int bytesRequested, @Nullable Charset charset) throws IOException {
        return new StringValue(getBytes(bytesRequested), charset);
    }

    @NotNull
    public String getNullTerminatedString(int maxLengthBytes, Charset charset) throws IOException {
        return getNullTerminatedStringValue(maxLengthBytes, charset).toString();
    }

    @NotNull
    public StringValue getNullTerminatedStringValue(int maxLengthBytes, Charset charset) throws IOException {
        return new StringValue(getNullTerminatedBytes(maxLengthBytes), charset);
    }

    @NotNull
    public byte[] getNullTerminatedBytes(int maxLengthBytes) throws IOException {
        byte[] buffer = new byte[maxLengthBytes];
        int length = 0;
        while (length < buffer.length) {
            byte b = getByte();
            buffer[length] = b;
            if (b == 0) {
                break;
            }
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
