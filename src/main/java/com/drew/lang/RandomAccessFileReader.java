package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.SuppressWarnings;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileReader extends RandomAccessReader {
    static final /* synthetic */ boolean $assertionsDisabled = (!RandomAccessFileReader.class.desiredAssertionStatus());
    private final int _baseOffset;
    private int _currentIndex;
    @NotNull
    private final RandomAccessFile _file;
    private final long _length;

    @SuppressWarnings(justification = "Design intent", value = "EI_EXPOSE_REP2")
    public RandomAccessFileReader(@NotNull RandomAccessFile file) throws IOException {
        this(file, 0);
    }

    @SuppressWarnings(justification = "Design intent", value = "EI_EXPOSE_REP2")
    public RandomAccessFileReader(@NotNull RandomAccessFile file, int baseOffset) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        }
        this._file = file;
        this._baseOffset = baseOffset;
        this._length = this._file.length();
    }

    public int toUnshiftedOffset(int localOffset) {
        return this._baseOffset + localOffset;
    }

    public long getLength() {
        return this._length;
    }

    public byte getByte(int index) throws IOException {
        if (index != this._currentIndex) {
            seek(index);
        }
        int b = this._file.read();
        if (b < 0) {
            throw new BufferBoundsException("Unexpected end of file encountered.");
        } else if ($assertionsDisabled || b <= 255) {
            this._currentIndex++;
            return (byte) b;
        } else {
            throw new AssertionError();
        }
    }

    @NotNull
    public byte[] getBytes(int index, int count) throws IOException {
        validateIndex(index, count);
        if (index != this._currentIndex) {
            seek(index);
        }
        byte[] bytes = new byte[count];
        int bytesRead = this._file.read(bytes);
        this._currentIndex += bytesRead;
        if (bytesRead == count) {
            return bytes;
        }
        throw new BufferBoundsException("Unexpected end of file encountered.");
    }

    private void seek(int index) throws IOException {
        if (index != this._currentIndex) {
            this._file.seek((long) index);
            this._currentIndex = index;
        }
    }

    /* access modifiers changed from: protected */
    public boolean isValidIndex(int index, int bytesRequested) throws IOException {
        return bytesRequested >= 0 && index >= 0 && (((long) index) + ((long) bytesRequested)) - 1 < this._length;
    }

    /* access modifiers changed from: protected */
    public void validateIndex(int index, int bytesRequested) throws IOException {
        if (!isValidIndex(index, bytesRequested)) {
            throw new BufferBoundsException(index, bytesRequested, this._length);
        }
    }
}
