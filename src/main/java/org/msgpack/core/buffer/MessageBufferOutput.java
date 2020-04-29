package org.msgpack.core.buffer;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public interface MessageBufferOutput extends Closeable, Flushable {
    void add(byte[] bArr, int i, int i2) throws IOException;

    MessageBuffer next(int i) throws IOException;

    void write(byte[] bArr, int i, int i2) throws IOException;

    void writeBuffer(int i) throws IOException;
}
