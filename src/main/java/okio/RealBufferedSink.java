package okio;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

final class RealBufferedSink implements BufferedSink {
    public final Buffer buffer = new Buffer();
    boolean closed;
    public final Sink sink;

    RealBufferedSink(Sink sink2) {
        if (sink2 == null) {
            throw new NullPointerException("sink == null");
        }
        this.sink = sink2;
    }

    public Buffer buffer() {
        return this.buffer;
    }

    public void write(Buffer source, long byteCount) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(source, byteCount);
        emitCompleteSegments();
    }

    public BufferedSink write(ByteString byteString) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(byteString);
        return emitCompleteSegments();
    }

    public BufferedSink writeUtf8(String string) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8(string);
        return emitCompleteSegments();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okio.Buffer.writeUtf8(java.lang.String, int, int):okio.Buffer
     arg types: [java.lang.String, int, int]
     candidates:
      okio.Buffer.writeUtf8(java.lang.String, int, int):okio.BufferedSink
      okio.BufferedSink.writeUtf8(java.lang.String, int, int):okio.BufferedSink
      okio.Buffer.writeUtf8(java.lang.String, int, int):okio.Buffer */
    public BufferedSink writeUtf8(String string, int beginIndex, int endIndex) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8(string, beginIndex, endIndex);
        return emitCompleteSegments();
    }

    public BufferedSink writeUtf8CodePoint(int codePoint) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8CodePoint(codePoint);
        return emitCompleteSegments();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okio.Buffer.writeString(java.lang.String, java.nio.charset.Charset):okio.Buffer
     arg types: [java.lang.String, java.nio.charset.Charset]
     candidates:
      okio.Buffer.writeString(java.lang.String, java.nio.charset.Charset):okio.BufferedSink
      okio.BufferedSink.writeString(java.lang.String, java.nio.charset.Charset):okio.BufferedSink
      okio.Buffer.writeString(java.lang.String, java.nio.charset.Charset):okio.Buffer */
    public BufferedSink writeString(String string, Charset charset) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeString(string, charset);
        return emitCompleteSegments();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okio.Buffer.writeString(java.lang.String, int, int, java.nio.charset.Charset):okio.Buffer
     arg types: [java.lang.String, int, int, java.nio.charset.Charset]
     candidates:
      okio.Buffer.writeString(java.lang.String, int, int, java.nio.charset.Charset):okio.BufferedSink
      okio.BufferedSink.writeString(java.lang.String, int, int, java.nio.charset.Charset):okio.BufferedSink
      okio.Buffer.writeString(java.lang.String, int, int, java.nio.charset.Charset):okio.Buffer */
    public BufferedSink writeString(String string, int beginIndex, int endIndex, Charset charset) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeString(string, beginIndex, endIndex, charset);
        return emitCompleteSegments();
    }

    public BufferedSink write(byte[] source) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(source);
        return emitCompleteSegments();
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okio.Buffer.write(byte[], int, int):okio.Buffer
     arg types: [byte[], int, int]
     candidates:
      okio.Buffer.write(byte[], int, int):okio.BufferedSink
      okio.BufferedSink.write(byte[], int, int):okio.BufferedSink
      okio.Buffer.write(byte[], int, int):okio.Buffer */
    public BufferedSink write(byte[] source, int offset, int byteCount) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(source, offset, byteCount);
        return emitCompleteSegments();
    }

    public int write(ByteBuffer source) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        int result = this.buffer.write(source);
        emitCompleteSegments();
        return result;
    }

    public long writeAll(Source source) throws IOException {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        long totalBytesRead = 0;
        while (true) {
            long readCount = source.read(this.buffer, PlaybackStateCompat.ACTION_PLAY_FROM_URI);
            if (readCount == -1) {
                return totalBytesRead;
            }
            totalBytesRead += readCount;
            emitCompleteSegments();
        }
    }

    public BufferedSink write(Source source, long byteCount) throws IOException {
        while (byteCount > 0) {
            long read = source.read(this.buffer, byteCount);
            if (read == -1) {
                throw new EOFException();
            }
            byteCount -= read;
            emitCompleteSegments();
        }
        return this;
    }

    public BufferedSink writeByte(int b) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeByte(b);
        return emitCompleteSegments();
    }

    public BufferedSink writeShort(int s) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeShort(s);
        return emitCompleteSegments();
    }

    public BufferedSink writeShortLe(int s) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeShortLe(s);
        return emitCompleteSegments();
    }

    public BufferedSink writeInt(int i) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeInt(i);
        return emitCompleteSegments();
    }

    public BufferedSink writeIntLe(int i) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeIntLe(i);
        return emitCompleteSegments();
    }

    public BufferedSink writeLong(long v) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeLong(v);
        return emitCompleteSegments();
    }

    public BufferedSink writeLongLe(long v) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeLongLe(v);
        return emitCompleteSegments();
    }

    public BufferedSink writeDecimalLong(long v) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeDecimalLong(v);
        return emitCompleteSegments();
    }

    public BufferedSink writeHexadecimalUnsignedLong(long v) throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeHexadecimalUnsignedLong(v);
        return emitCompleteSegments();
    }

    public BufferedSink emitCompleteSegments() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        long byteCount = this.buffer.completeSegmentByteCount();
        if (byteCount > 0) {
            this.sink.write(this.buffer, byteCount);
        }
        return this;
    }

    public BufferedSink emit() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        long byteCount = this.buffer.size();
        if (byteCount > 0) {
            this.sink.write(this.buffer, byteCount);
        }
        return this;
    }

    public OutputStream outputStream() {
        return new OutputStream() {
            /* class okio.RealBufferedSink.AnonymousClass1 */

            public void write(int b) throws IOException {
                if (RealBufferedSink.this.closed) {
                    throw new IOException("closed");
                }
                RealBufferedSink.this.buffer.writeByte((int) ((byte) b));
                RealBufferedSink.this.emitCompleteSegments();
            }

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: okio.Buffer.write(byte[], int, int):okio.Buffer
             arg types: [byte[], int, int]
             candidates:
              okio.Buffer.write(byte[], int, int):okio.BufferedSink
              okio.BufferedSink.write(byte[], int, int):okio.BufferedSink
              okio.Buffer.write(byte[], int, int):okio.Buffer */
            public void write(byte[] data, int offset, int byteCount) throws IOException {
                if (RealBufferedSink.this.closed) {
                    throw new IOException("closed");
                }
                RealBufferedSink.this.buffer.write(data, offset, byteCount);
                RealBufferedSink.this.emitCompleteSegments();
            }

            public void flush() throws IOException {
                if (!RealBufferedSink.this.closed) {
                    RealBufferedSink.this.flush();
                }
            }

            public void close() throws IOException {
                RealBufferedSink.this.close();
            }

            public String toString() {
                return RealBufferedSink.this + ".outputStream()";
            }
        };
    }

    public void flush() throws IOException {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (this.buffer.size > 0) {
            this.sink.write(this.buffer, this.buffer.size);
        }
        this.sink.flush();
    }

    public boolean isOpen() {
        return !this.closed;
    }

    public void close() throws IOException {
        if (!this.closed) {
            Throwable thrown = null;
            try {
                if (this.buffer.size > 0) {
                    this.sink.write(this.buffer, this.buffer.size);
                }
            } catch (Throwable e) {
                thrown = e;
            }
            try {
                this.sink.close();
            } catch (Throwable e2) {
                if (thrown == null) {
                    thrown = e2;
                }
            }
            this.closed = true;
            if (thrown != null) {
                Util.sneakyRethrow(thrown);
            }
        }
    }

    public Timeout timeout() {
        return this.sink.timeout();
    }

    public String toString() {
        return "buffer(" + this.sink + ")";
    }
}
