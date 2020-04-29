package org.msgpack.value;

import java.nio.ByteBuffer;

public interface RawValue extends Value {
    byte[] asByteArray();

    ByteBuffer asByteBuffer();

    String asString();

    String toString();
}
