package org.msgpack.value;

public interface ExtensionValue extends Value {
    byte[] getData();

    byte getType();
}
