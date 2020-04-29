package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class RandomAccessStreamReader extends RandomAccessReader {
    static final /* synthetic */ boolean $assertionsDisabled = (!RandomAccessStreamReader.class.desiredAssertionStatus());
    public static final int DEFAULT_CHUNK_LENGTH = 2048;
    private final int _chunkLength;
    private final ArrayList<byte[]> _chunks;
    private boolean _isStreamFinished;
    @NotNull
    private final InputStream _stream;
    private long _streamLength;

    public RandomAccessStreamReader(@NotNull InputStream stream) {
        this(stream, 2048, -1);
    }

    public RandomAccessStreamReader(@NotNull InputStream stream, int chunkLength) {
        this(stream, chunkLength, -1);
    }

    public RandomAccessStreamReader(@NotNull InputStream stream, int chunkLength, long streamLength) {
        this._chunks = new ArrayList<>();
        if (stream == null) {
            throw new NullPointerException();
        } else if (chunkLength <= 0) {
            throw new IllegalArgumentException("chunkLength must be greater than zero");
        } else {
            this._chunkLength = chunkLength;
            this._stream = stream;
            this._streamLength = streamLength;
        }
    }

    public long getLength() throws IOException {
        if (this._streamLength != -1) {
            return this._streamLength;
        }
        isValidIndex(Integer.MAX_VALUE, 1);
        if ($assertionsDisabled || this._isStreamFinished) {
            return this._streamLength;
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: protected */
    public void validateIndex(int index, int bytesRequested) throws IOException {
        if (index < 0) {
            throw new BufferBoundsException(String.format("Attempt to read from buffer using a negative index (%d)", Integer.valueOf(index)));
        } else if (bytesRequested < 0) {
            throw new BufferBoundsException("Number of requested bytes must be zero or greater");
        } else if ((((long) index) + ((long) bytesRequested)) - 1 > 2147483647L) {
            throw new BufferBoundsException(String.format("Number of requested bytes summed with starting index exceed maximum range of signed 32 bit integers (requested index: %d, requested count: %d)", Integer.valueOf(index), Integer.valueOf(bytesRequested)));
        } else if (isValidIndex(index, bytesRequested)) {
        } else {
            if ($assertionsDisabled || this._isStreamFinished) {
                throw new BufferBoundsException(index, bytesRequested, this._streamLength);
            }
            throw new AssertionError();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isValidIndex(int index, int bytesRequested) throws IOException {
        if (index < 0 || bytesRequested < 0) {
            return false;
        }
        long endIndexLong = (((long) index) + ((long) bytesRequested)) - 1;
        if (endIndexLong > 2147483647L) {
            return false;
        }
        int endIndex = (int) endIndexLong;
        if (this._isStreamFinished) {
            return ((long) endIndex) < this._streamLength;
        }
        int chunkIndex = endIndex / this._chunkLength;
        while (chunkIndex >= this._chunks.size()) {
            if ($assertionsDisabled || !this._isStreamFinished) {
                byte[] chunk = new byte[this._chunkLength];
                int totalBytesRead = 0;
                while (!this._isStreamFinished && totalBytesRead != this._chunkLength) {
                    int bytesRead = this._stream.read(chunk, totalBytesRead, this._chunkLength - totalBytesRead);
                    if (bytesRead == -1) {
                        this._isStreamFinished = true;
                        int observedStreamLength = (this._chunks.size() * this._chunkLength) + totalBytesRead;
                        if (this._streamLength == -1) {
                            this._streamLength = (long) observedStreamLength;
                        } else if (this._streamLength != ((long) observedStreamLength) && !$assertionsDisabled) {
                            throw new AssertionError();
                        }
                        if (((long) endIndex) >= this._streamLength) {
                            this._chunks.add(chunk);
                            return false;
                        }
                    } else {
                        totalBytesRead += bytesRead;
                    }
                }
                this._chunks.add(chunk);
            } else {
                throw new AssertionError();
            }
        }
        return true;
    }

    public int toUnshiftedOffset(int localOffset) {
        return localOffset;
    }

    public byte getByte(int index) throws IOException {
        if ($assertionsDisabled || index >= 0) {
            int chunkIndex = index / this._chunkLength;
            return this._chunks.get(chunkIndex)[index % this._chunkLength];
        }
        throw new AssertionError();
    }

    @NotNull
    public byte[] getBytes(int index, int count) throws IOException {
        validateIndex(index, count);
        byte[] bytes = new byte[count];
        int remaining = count;
        int fromIndex = index;
        int toIndex = 0;
        while (remaining != 0) {
            int fromChunkIndex = fromIndex / this._chunkLength;
            int fromInnerIndex = fromIndex % this._chunkLength;
            int length = Math.min(remaining, this._chunkLength - fromInnerIndex);
            System.arraycopy(this._chunks.get(fromChunkIndex), fromInnerIndex, bytes, toIndex, length);
            remaining -= length;
            fromIndex += length;
            toIndex += length;
        }
        return bytes;
    }
}
