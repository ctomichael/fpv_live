package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.SuppressWarnings;
import java.io.IOException;

public class ByteArrayReader extends RandomAccessReader {
    private final int _baseOffset;
    @NotNull
    private final byte[] _buffer;

    @SuppressWarnings(justification = "Design intent", value = "EI_EXPOSE_REP2")
    public ByteArrayReader(@NotNull byte[] buffer) {
        this(buffer, 0);
    }

    @SuppressWarnings(justification = "Design intent", value = "EI_EXPOSE_REP2")
    public ByteArrayReader(@NotNull byte[] buffer, int baseOffset) {
        if (buffer == null) {
            throw new NullPointerException();
        } else if (baseOffset < 0) {
            throw new IllegalArgumentException("Must be zero or greater");
        } else {
            this._buffer = buffer;
            this._baseOffset = baseOffset;
        }
    }

    public int toUnshiftedOffset(int localOffset) {
        return this._baseOffset + localOffset;
    }

    public long getLength() {
        return (long) (this._buffer.length - this._baseOffset);
    }

    public byte getByte(int index) throws IOException {
        validateIndex(index, 1);
        return this._buffer[this._baseOffset + index];
    }

    /* access modifiers changed from: protected */
    public void validateIndex(int index, int bytesRequested) throws IOException {
        if (!isValidIndex(index, bytesRequested)) {
            throw new BufferBoundsException(toUnshiftedOffset(index), bytesRequested, (long) this._buffer.length);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isValidIndex(int index, int bytesRequested) throws IOException {
        return bytesRequested >= 0 && index >= 0 && (((long) index) + ((long) bytesRequested)) - 1 < getLength();
    }

    @NotNull
    public byte[] getBytes(int index, int count) throws IOException {
        validateIndex(index, count);
        byte[] bytes = new byte[count];
        System.arraycopy(this._buffer, this._baseOffset + index, bytes, 0, count);
        return bytes;
    }
}
