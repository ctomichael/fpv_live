package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class StreamReader extends SequentialReader {
    static final /* synthetic */ boolean $assertionsDisabled = (!StreamReader.class.desiredAssertionStatus());
    private long _pos;
    @NotNull
    private final InputStream _stream;

    public long getPosition() {
        return this._pos;
    }

    public StreamReader(@NotNull InputStream stream) {
        if (stream == null) {
            throw new NullPointerException();
        }
        this._stream = stream;
        this._pos = 0;
    }

    public byte getByte() throws IOException {
        int value = this._stream.read();
        if (value == -1) {
            throw new EOFException("End of data reached.");
        }
        this._pos++;
        return (byte) value;
    }

    @NotNull
    public byte[] getBytes(int count) throws IOException {
        byte[] bytes = new byte[count];
        getBytes(bytes, 0, count);
        return bytes;
    }

    public void getBytes(@NotNull byte[] buffer, int offset, int count) throws IOException {
        int totalBytesRead = 0;
        while (totalBytesRead != count) {
            int bytesRead = this._stream.read(buffer, offset + totalBytesRead, count - totalBytesRead);
            if (bytesRead == -1) {
                throw new EOFException("End of data reached.");
            }
            totalBytesRead += bytesRead;
            if (!$assertionsDisabled && totalBytesRead > count) {
                throw new AssertionError();
            }
        }
        this._pos += (long) totalBytesRead;
    }

    public void skip(long n) throws IOException {
        if (n < 0) {
            throw new IllegalArgumentException("n must be zero or greater.");
        }
        long skippedCount = skipInternal(n);
        if (skippedCount != n) {
            throw new EOFException(String.format("Unable to skip. Requested %d bytes but skipped %d.", Long.valueOf(n), Long.valueOf(skippedCount)));
        }
    }

    public boolean trySkip(long n) throws IOException {
        if (n >= 0) {
            return skipInternal(n) == n;
        }
        throw new IllegalArgumentException("n must be zero or greater.");
    }

    public int available() {
        try {
            return this._stream.available();
        } catch (IOException e) {
            return 0;
        }
    }

    private long skipInternal(long n) throws IOException {
        long skippedTotal = 0;
        while (skippedTotal != n) {
            long skipped = this._stream.skip(n - skippedTotal);
            if ($assertionsDisabled || skipped >= 0) {
                skippedTotal += skipped;
                if (skipped == 0) {
                    break;
                }
            } else {
                throw new AssertionError();
            }
        }
        this._pos += skippedTotal;
        return skippedTotal;
    }
}
